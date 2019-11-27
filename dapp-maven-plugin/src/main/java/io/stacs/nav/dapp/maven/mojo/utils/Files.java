package io.stacs.nav.dapp.maven.mojo.utils;

import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Objects;

/**
 * @author dekuofa <br>
 * @date 2019-11-26 <br>
 */
public class Files {
    public static void mkdir(File dir) {
        if (Objects.nonNull(dir) && !dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void remove(File dir) {
        if (Objects.nonNull(dir) && dir.exists()) {
            dir.delete();
        }
    }

    public static File getTargetFile(String finalName, String targetClassifier, File outputDirectory,
        MavenProject project) {
        mkdir(outputDirectory);
        String classifier = (targetClassifier == null ? "" : targetClassifier.trim());
        if (classifier.length() > 0 && !classifier.startsWith("-")) {
            classifier = "-" + classifier;
        }
        return new File(outputDirectory,
            finalName + classifier + "." + project.getArtifact().getArtifactHandler().getExtension());
    }
}
