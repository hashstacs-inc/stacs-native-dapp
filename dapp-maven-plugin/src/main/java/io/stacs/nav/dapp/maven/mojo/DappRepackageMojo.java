package io.stacs.nav.dapp.maven.mojo;

import com.alipay.sofa.ark.common.util.StringUtils;
import com.alipay.sofa.ark.tools.Libraries;
import com.google.common.collect.Sets;
import io.stacs.nav.dapp.maven.mojo.Constants.Ark;
import io.stacs.nav.dapp.maven.mojo.Constants.DRS;
import io.stacs.nav.dapp.maven.mojo.Constants.Plugin;
import io.stacs.nav.dapp.maven.mojo.utils.ArtifactsLibraries;
import io.stacs.nav.dapp.maven.mojo.utils.Repackager;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.stacs.nav.dapp.maven.mojo.Constants.DRS_API_ARTIFACT_KEY;
import static io.stacs.nav.dapp.maven.mojo.Constants.MIN_PRIORITY;
import static io.stacs.nav.dapp.maven.mojo.utils.Artifacts.filterExcludeArtifacts;
import static io.stacs.nav.dapp.maven.mojo.utils.Files.getTargetFile;
import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;
import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

/**
 * @author dekuofa <br>
 * @date 2019-11-25 <br>
 */
@Mojo(name = "package", defaultPhase = PACKAGE, threadSafe = true, requiresDependencyResolution = COMPILE_PLUS_RUNTIME, requiresDependencyCollection = COMPILE_PLUS_RUNTIME)
public class DappRepackageMojo extends AbstractMojo {

    private final String arkVersion = "1.0.0";
    @Parameter(defaultValue = "${project}", readonly = true, required = true) private MavenProject project;
    @Component private MavenProjectHelper projectHelper;
    @Parameter(defaultValue = "${project.basedir}", required = true) private File baseDir;
    @Parameter(defaultValue = "${project.build.finalName}", required = true) private String finalName;
    @Parameter(defaultValue = "${project.build.directory}", required = true) private File outputDirectory;
    @Parameter(defaultValue = "${project.artifactId}") private String bizName;
    @Parameter(defaultValue = "${project.version}") private String bizVersion;
    /**
     * list of packages denied to be imported
     */
    @Parameter private LinkedHashSet<String> denyImportPackages;
    /**
     * list of classes denied to be imported
     */
    @Parameter private LinkedHashSet<String> denyImportClasses;
    /**
     * list of resources denied to be imported
     */
    @Parameter private LinkedHashSet<String> denyImportResources;
    /**
     * whether package provided dependencies into ark
     */
    @Parameter(defaultValue = "false") private boolean packageProvided;
    /**
     * whether to skip package ark-executable jar
     */
    @Parameter(defaultValue = "false") private boolean skipArkExecutable;
    @Parameter(defaultValue = "false", property = "stacs.package.skip") private boolean skip;
    @Parameter(defaultValue = "dapp", required = true) private String dappClassifier;
    @Parameter(defaultValue = "debug", required = true) private String debugClassifier;
    @Parameter(defaultValue = "100", property = "stacs.dapp.priority") private Integer priority;
    @Parameter(defaultValue = "true") private boolean keepArkBizJar;
    /**
     * The name of the main class. If not specified the first compiled class found that
     * contains a 'main' method will be used.
     *
     * @since 0.1.0
     */
    @Parameter(defaultValue = "false") private boolean attach;
    @Parameter private String mainClass;
    /**
     * Colon separated groupId, artifactId [and classifier] to exclude (exact match)
     */
    @Parameter private LinkedHashSet<String> excludes = new LinkedHashSet<>();
    /**
     * list of groupId names to exclude (exact match).
     */
    @Parameter private LinkedHashSet<String> excludeGroupIds;
    /**
     * list of artifact names to exclude (exact match).
     */
    @Parameter private LinkedHashSet<String> excludeArtifactIds;
    /**
     * A list of the libraries that must be unpacked from fat jars in order to run.
     * Specify each library as a <code>&lt;dependency&gt;</code> with a
     * <code>&lt;groupId&gt;</code> and a <code>&lt;artifactId&gt;</code> and they will be
     * unpacked at runtime.
     *
     * @since 0.1.0
     */
    @Parameter private List<Dependency> requiresUnpack;
    @Parameter(defaultValue = "/", required = true) private String webContextPath;
    @Component private MavenSession mavenSession;
    @Component private ArtifactFactory artifactFactory;
    @Component private ArtifactResolver artifactResolver;
    @Component private BuildPluginManager pluginManager;
    private String drsVersion;
    @Component private PluginDescriptor pluginDescriptor;

    @Override public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("start dapp repackage");
        if (!checkParam())
            return;

        Artifact drsApi = Optional.ofNullable(project.getArtifactMap().get(DRS_API_ARTIFACT_KEY))
            .orElseThrow(() -> new MojoExecutionException("can't repackage dapp without drs-api dependency"));
        drsVersion = drsApi.getVersion();
        // write drs config

        repackage();
    }

    private void repackage() throws MojoExecutionException {
        File sourceJar = this.project.getArtifact().getFile();
        File appTarget = getTargetFile(finalName, debugClassifier, outputDirectory, project);
        File moduleTarget = getTargetFile(finalName, dappClassifier, outputDirectory, project);

        Repackager repackager = buildRepackager(new Repackager(sourceJar));
        Libraries libraries = new ArtifactsLibraries(getAdditionalArtifact(), this.requiresUnpack, getLog());
        try {
            repackager.repackage(appTarget, moduleTarget, libraries, skipArkExecutable);
        } catch (IOException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
        if (attach)
            updateArtifact(appTarget, repackager.getModuleTargetFile());
    }

    private Repackager buildRepackager(Repackager repackager) {
        repackager.addMainClassTimeoutWarningListener(new LoggingMainClassTimeoutWarningListener());
        repackager.setMainClass(mainClass);
        repackager.setBizName(bizName);
        repackager.setBizVersion(bizVersion);
        repackager.setPriority(String.valueOf(priority));
        repackager.setArkVersion(arkVersion);
        repackager.setDenyImportClasses(denyImportClasses);
        repackager.setDenyImportPackages(denyImportPackages);
        repackager.setDenyImportResources(denyImportResources);
        repackager.setPackageProvided(packageProvided);
        repackager.setKeepArkBizJar(keepArkBizJar);
        repackager.setBaseDir(baseDir);
        repackager.setWebContextPath(webContextPath);
        repackager.setDrsVersion(drsVersion);
        return repackager;
    }

    private void updateArtifact(File repackaged, File modulePackaged) {
        attachArtifact(repackaged, debugClassifier);
        attachArtifact(modulePackaged, dappClassifier);
    }

    private void attachArtifact(File jarFile, String classifier) {
        getLog().info("Attaching archive:" + jarFile + ", with classifier: " + classifier);
        projectHelper.attachArtifact(this.project, this.project.getPackaging(), classifier, jarFile);
    }

    private Set<Artifact> getAdditionalArtifact() throws MojoExecutionException {
        Artifact ark = artifactFactory.createArtifact(Ark.GROUP_ID, Ark.ARTIFACT_ID, arkVersion, Ark.SCOPE, Ark.TYPE);
        Artifact drs = artifactFactory
            .createArtifactWithClassifier(DRS.GROUP_ID, DRS.ARTIFACT_ID, drsVersion, DRS.TYPE, DRS.CLASSIFIER);
        drs.setScope(DRS.SCOPE);
        Artifact service = artifactFactory
            .createArtifact(Plugin.DrsService.GROUP_ID, Plugin.DrsService.ARTIFACT_ID, drsVersion,
                Plugin.DrsService.SCOPE, Plugin.DrsService.TYPE);
        try {
            artifactResolver.resolve(ark, project.getRemoteArtifactRepositories(), mavenSession.getLocalRepository());
            artifactResolver.resolve(drs, project.getRemoteArtifactRepositories(), mavenSession.getLocalRepository());
            artifactResolver
                .resolve(service, project.getRemoteArtifactRepositories(), mavenSession.getLocalRepository());
            Set<Artifact> artifacts = Sets.newHashSet(ark, drs, service);
            artifacts
                .addAll(filterExcludeArtifacts(project.getArtifacts(), excludes, excludeGroupIds, excludeArtifactIds));
            return artifacts;
        } catch (ArtifactResolutionException | ArtifactNotFoundException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }

    private boolean checkParam() {
        Log log = getLog();
        if ("pom".equals(project.getPackaging()) || "war".equals(project.getPackaging())) {
            log.warn("package goal can't be applied to " + project.getPackaging() + " project");
            return false;
        }
        if (StringUtils.isSameStr(this.dappClassifier, this.debugClassifier)) {
            log.warn("dappClassifier can't same as debugClassifier.");
            return false;
        }
        if (MIN_PRIORITY > priority) {
            log.warn("priority can't less than: " + MIN_PRIORITY + " .");
            return false;
        }
        if (skip) {
            log.warn("skipping package, notice your configuration.");
            return false;
        }
        log.debug("param check success");
        return true;
    }

    private class LoggingMainClassTimeoutWarningListener implements Repackager.MainClassTimeoutWarningListener {
        @Override public void handleTimeoutWarning(long duration, String mainMethod) {
            getLog().warn(String.format(
                "Searching for the main-class is taking some time: %dms, consider using the mainClass configuration parameter",
                duration));
        }

    }
}
