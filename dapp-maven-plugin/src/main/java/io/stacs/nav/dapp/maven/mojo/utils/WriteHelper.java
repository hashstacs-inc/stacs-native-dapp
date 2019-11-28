package io.stacs.nav.dapp.maven.mojo.utils;

import com.alipay.sofa.ark.common.util.FileUtils;
import com.alipay.sofa.ark.tools.JarWriter;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

import static com.alipay.sofa.ark.spi.constant.Constants.ARK_CONF_BASE_DIR;
import static com.alipay.sofa.ark.spi.constant.Constants.ARK_CONF_FILE;
import static io.stacs.nav.dapp.maven.mojo.Constants.DRS.ARK_CONFIG_KEY;
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

    public static Function<File, Optional<File>> createTempFile() {
        return file -> {
            try {
                return Optional.of(Files.createTempFile("resource-", ".tmp").toFile());
            } catch (IOException e) {
                return Optional.empty();
            }
        };
    }

    public static Function<File, Optional<Properties>> loadPropFromFile() {
        return file -> {
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(file));
                return Optional.of(prop);
            } catch (IOException e) {
                return Optional.empty();
            }
        };
    }

    public static Function<Optional<Properties>, Optional<Properties>> buildProperties() {
        return opt -> opt.map(prop -> {
            prop.setProperty(ARK_CONFIG_KEY, ARK_CONFIG_KEY);
            return opt;
        }).orElse(opt);
    }

    private static InputStream buildArkConfInputStream(@Nonnull File configFile) throws IOException {
        File temp = createTempFile().apply(configFile)
            .orElseThrow(() -> new IOException("can't found/access file:" + configFile.getAbsolutePath()));
        Properties prop = loadPropFromFile().andThen(buildProperties()).apply(temp)
            .orElseThrow(() -> new FileNotFoundException(configFile.getAbsolutePath()));
        prop.store(new FileOutputStream(temp), null);
        return new FileInputStream(temp);
    }


}
