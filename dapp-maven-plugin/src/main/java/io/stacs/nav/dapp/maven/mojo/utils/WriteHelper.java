package io.stacs.nav.dapp.maven.mojo.utils;

import com.alipay.sofa.ark.common.util.FileUtils;
import com.alipay.sofa.ark.tools.JarWriter;
import io.stacs.nav.dapp.maven.mojo.utils.LambdaExceptionUtil.BiConsumerWithExceptions;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.alipay.sofa.ark.spi.constant.Constants.ARK_CONF_BASE_DIR;
import static com.alipay.sofa.ark.spi.constant.Constants.ARK_CONF_FILE;
import static io.stacs.nav.dapp.maven.mojo.Constants.APPLICATION_PROPERTIES;
import static io.stacs.nav.dapp.maven.mojo.Constants.DRS.*;
import static java.io.File.separator;

/**
 * @author dekuofa <br>
 * @date 2019-11-27 <br>
 */
public class WriteHelper {

    public static void writeConfDir(int baseDirParentPathLength, File confDir, JarWriter jarWriter) throws IOException {
        File[] files = confDir.exists() ? confDir.listFiles() : null;
        if (Objects.nonNull(files)) {
            for (File subFile : files) {
                if (subFile.isDirectory()) {
                    writeConfDir(baseDirParentPathLength, subFile, jarWriter);
                } else {
                    if (ARK_CONF_FILE.equals(subFile.getName())) {
                        continue;
                    }
                    String entryName = subFile.getPath().substring(baseDirParentPathLength);
                    if (entryName.startsWith(separator)) {
                        entryName = entryName.substring(1);
                    }
                    jarWriter.writeEntry(FileUtils.getCompatiblePath(entryName), new FileInputStream(subFile));
                }
            }
        }
        // write boot config
        jarWriter.writeEntry(FileUtils.getCompatiblePath(ARK_CONF_BASE_DIR + separator + ARK_CONF_FILE),
            buildArkConfInputStream(
                new File(confDir.getAbsolutePath() + separator + "ark" + separator + ARK_CONF_FILE)));
    }

    public static File buildPropertiesFile(File confDir, Supplier<Properties> newProp,
        Function<Properties, Properties> buildProperties) throws IOException {
        Path path = Files.createTempFile("resource", "temp");
        Properties prop = newProp.get();
        File temp = path.toFile();
        if (confDir.exists() && confDir.isDirectory()) {
            File source = new File(confDir, "drs.properties");// todo check exist //yml 支持
            if (source.exists()) {
                FileInputStream in = new FileInputStream(source);
                java.nio.file.Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
                prop.load(new FileInputStream(temp));
            }
        }

        buildProperties.apply(prop);
        writePropertiesToFile().accept(prop, temp);
        return temp;
    }

    public static Function<Properties, Properties> drsPropertiesBuilder() {
        return prop -> {
            prop.setProperty(DRS_CALL_BACK_URL_KEY, DRS_CALL_BACK_URL_VALUE);
            return prop;
        };
    }

    public static BiConsumerWithExceptions<Properties, File, IOException> writePropertiesToFile() {
        return (prop, targetFile) -> prop.store(new FileOutputStream(targetFile), null);
    }

    private static InputStream buildArkConfInputStream(@Nonnull File configFile) throws IOException {
        // todo 优化
        Properties prop = new Properties();
        Path temp = java.nio.file.Files.createTempFile("resource-", ".tmp");
        if (configFile.exists()) {
            FileInputStream in = new FileInputStream(configFile);
            java.nio.file.Files.copy(in, temp, StandardCopyOption.REPLACE_EXISTING);
            prop.load(in);

        } else {
            InputStream inStream = WriteHelper.class.getClassLoader().getResourceAsStream(ARK_CONF_FILE);
            assert inStream != null;
            java.nio.file.Files.copy(inStream, temp, StandardCopyOption.REPLACE_EXISTING);

        }
        prop.setProperty(ARK_CONFIG_KEY, ARK_CONFIG_KEY);
        prop.store(new FileOutputStream(temp.toFile()), null);

        return new FileInputStream(temp.toFile());
    }

    public static void buildPropertiesFile(File bizDir, JarWriter jarWriter) throws IOException {

        // write boot config
        jarWriter.writeEntry(FileUtils.getCompatiblePath(ARK_CONF_BASE_DIR + separator + ARK_CONF_FILE),
            buildArkConfInputStream(new File(bizDir.getAbsolutePath() + separator + APPLICATION_PROPERTIES)));
    }

}
