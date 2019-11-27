package io.stacs.nav.dapp.maven.mojo.utils;

import com.alipay.sofa.ark.common.util.FileUtils;
import com.alipay.sofa.ark.tools.JarWriter;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Properties;

import static com.alipay.sofa.ark.spi.constant.Constants.ARK_CONF_BASE_DIR;
import static com.alipay.sofa.ark.spi.constant.Constants.ARK_CONF_FILE;
import static io.stacs.nav.dapp.maven.mojo.Constants.DRS.ARK_CONFIG_KEY;
import static io.stacs.nav.dapp.maven.mojo.Constants.DRS.ARK_CONFIG_VALUE;
import static io.stacs.nav.dapp.maven.mojo.Constants.PROPERTY_SEPARATOR;

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
                    if (entryName.startsWith(File.separator)) {
                        entryName = entryName.substring(1);
                    }
                    jarWriter.writeEntry(FileUtils.getCompatiblePath(entryName), new FileInputStream(subFile));
                }
            }
        }
        // write boot config
        jarWriter.writeEntry(FileUtils.getCompatiblePath(ARK_CONF_BASE_DIR + File.separator + ARK_CONF_FILE),
            buildArkConfInputStream(
                new File(confDir.getAbsolutePath() + File.separator + "ark" + File.separator + ARK_CONF_FILE)));
    }

    private static InputStream buildArkConfInputStream(@Nonnull File configFile) throws IOException {
        byte[] confBytes = (ARK_CONFIG_KEY + PROPERTY_SEPARATOR + ARK_CONFIG_VALUE).getBytes();
        Path temp = java.nio.file.Files.createTempFile("resource-", ".tmp");
        if (configFile.exists()) {
            FileInputStream in = new FileInputStream(configFile);
            java.nio.file.Files.copy(in, temp, StandardCopyOption.REPLACE_EXISTING);
            Properties prop = new Properties();
            prop.load(in);
            prop.setProperty(ARK_CONFIG_KEY, ARK_CONFIG_KEY);
            prop.store(new FileOutputStream(temp.toFile()), null);
        } else {
            InputStream inStream = WriteHelper.class.getClassLoader().getResourceAsStream(ARK_CONF_FILE);
            assert inStream != null;
            java.nio.file.Files.copy(inStream, temp, StandardCopyOption.REPLACE_EXISTING);
            FileOutputStream out = new FileOutputStream(temp.toFile());
            out.write(confBytes);
            out.close();
        }

        return new FileInputStream(temp.toFile());
    }

    private static String getEntryName(File entryFile, File baseDir) {
        String entryName = entryFile.getPath().substring(baseDir.getPath().length());
        return entryName.startsWith(File.separator) ? entryName.substring(1) : entryName;
    }

}
