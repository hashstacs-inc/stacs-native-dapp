package io.stacs.nav.drs.service.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @author dekuofa <br>
 * @date 2019-11-28 <br>
 */
public class ResourceLoader {

    public static Optional<Manifest> getManifest(Class<?> targetJarClass) {
        // get the full name of the application manifest file
        String appManifestFileName =
            targetJarClass.getProtectionDomain().getCodeSource().getLocation().toString() + JarFile.MANIFEST_NAME;

        Enumeration resEnum;
        try {
            // get a list of all manifest files found in the jars loaded by the app
            resEnum = Thread.currentThread().getContextClassLoader().getResources(JarFile.MANIFEST_NAME);
            while (resEnum.hasMoreElements()) {
                try {
                    URL url = (URL)resEnum.nextElement();
                    // is the app manifest file?
                    if (url.toString().equals(appManifestFileName)) {
                        // open the manifest
                        InputStream is = url.openStream();
                        if (is != null) {
                            // read the manifest and return it to the application
                            Manifest manifest = new Manifest(is);
                            return Optional.of(manifest);
                        }
                    }
                } catch (Exception e) {
                    // Silently ignore wrong manifests on classpath?
                }
            }
        } catch (IOException e1) {
            // Silently ignore wrong manifests on classpath?
        }
        return Optional.empty();
    }

}
