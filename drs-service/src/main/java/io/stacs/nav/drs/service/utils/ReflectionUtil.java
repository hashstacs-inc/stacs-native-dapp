package io.stacs.nav.drs.service.utils;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 * @author ganxiang
 * @date 10/29/0029
 */
public class ReflectionUtil {

    private static final Object lock = new Object();
    private static Reflections DEFAULT_REFLECTIONS;

    public static Reflections getDefaultReflections() {
        if (DEFAULT_REFLECTIONS == null) {
            synchronized (lock) {
                if (DEFAULT_REFLECTIONS == null) {
                    DEFAULT_REFLECTIONS = new Reflections(
                        new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage("io.stacs.nav"))
                            .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
                            .filterInputsBy(new FilterBuilder().include("(.*)?\\.class")));
                }
            }
        }
        return DEFAULT_REFLECTIONS;
    }

}
