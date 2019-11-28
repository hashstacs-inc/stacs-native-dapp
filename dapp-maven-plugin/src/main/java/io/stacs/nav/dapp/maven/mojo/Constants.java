package io.stacs.nav.dapp.maven.mojo;

/**
 * @author dekuofa <br>
 * @date 2019-11-26 <br>
 */
public interface Constants {

    int MIN_PRIORITY = 50;
    String WRAP = "\n";
    String PROPERTY_SEPARATOR = "=";

    String DRS_API_ARTIFACT_KEY = "io.stacs.nav:drs-api";
    String APPLICATION_PROPERTIES = "application.properties";
    String CONFIG_DIR = "conf/drs/";
    String APP_PROPERTIES_ENTITY = "conf/drs/application.properties";

    interface Ark {

        String GROUP_ID = "com.alipay.sofa";

        String ARTIFACT_ID = "sofa-ark-all";

        String CLASSIFIER = "";

        String SCOPE = "compile";

        String TYPE = "jar";

    }


    interface DRS {

        String GROUP_ID = "io.stacs.nav";

        String ARTIFACT_ID = "drs-boot";

        String CLASSIFIER = "dev";

        String SCOPE = "compile";

        String TYPE = "jar";

        String ARK_CONFIG_KEY = "com.alipay.sofa.ark.master.biz";
        String ARK_CONFIG_VALUE = "drs-boot";
        String DRS_CALL_BACK_URL_KEY = "drs.domain.callbackUrl";
        String DRS_CALL_BACK_URL_VALUE = "http://localhost:8000/drs/callback";
        String DRS_VERSION = "DRS-Version";
    }

    interface Plugin {
        interface DrsService {

            String GROUP_ID = "io.stacs.nav";

            String ARTIFACT_ID = "drs-service";

            String CLASSIFIER = "";

            String SCOPE = "compile";

            String TYPE = "jar";

        }
    }

}
