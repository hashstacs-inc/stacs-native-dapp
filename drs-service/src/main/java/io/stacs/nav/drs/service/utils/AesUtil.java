package io.stacs.nav.drs.service.utils;

import com.google.common.base.Charsets;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author WangQuanzhou
 * @date 2018/2/24 15:53
 */
public class AesUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AesUtil.class);

    /**
     * @param content
     * @param password
     * @return
     */
    public static byte[] encryptToBytes(String content, String password) {
        try {
            SecretKey key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (Exception e) {
            LOGGER.error("Aes encrypt failed {} " + e.getMessage(), e);
            return null;
        }
    }

    /**
     *
     * @param content
     * @param password
     * @return
     */
    public static byte[] decryptToBytes(byte[] content, String password) {
        try {
            SecretKey key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            LOGGER.error("Aes decrypt failed {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     *
     * @param content
     * @param password
     * @param filePath
     */
    public static void encryptToFile(String content, String password, String filePath) {
        File file = new File(filePath);
        OutputStream output = null;
        BufferedOutputStream bufferedOutput = null;
        try {
            output = new FileOutputStream(file);
            bufferedOutput = new BufferedOutputStream(output);
            byte[] bytes = encryptToBytes(content, password);
            bufferedOutput.write(bytes);
        } catch (Exception e) {
            LOGGER.error("EncryptFile failed " + e.getMessage(), e);
        } finally {
            if (bufferedOutput != null) {
                try {
                    bufferedOutput.close();
                } catch (IOException e) {
                    LOGGER.error("BufferedOutput closed failed " + e.getMessage(), e);
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    LOGGER.error("FileOutput closed failed " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     *
     * @param password
     * @param filePath
     * @return
     */
    public static String decryptFromFile(String password, String filePath) {
        File file = new File(filePath);
        InputStream input = null;
        ByteArrayOutputStream bos = null;
        try {
            input = new FileInputStream(file);
            bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = input.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            byte[] contentBytes = bos.toByteArray();
            decryptToBytes(contentBytes, password);
            return new String(decryptToBytes(contentBytes, password));
        } catch (Exception e) {
            LOGGER.error("DecryptFromFile failed " + e.getMessage(), e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;

    }

    /**
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.decodeBase64(key);
    }

    /**
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return Base64.encodeBase64String(key);
    }

    public static String encryptToString(String content, String password) {
        byte[] encryptBase64Bytes = encryptToBytes(content, password);
        if (null == encryptBase64Bytes) {
            return null;
        }
        String encryptString = null;
        try {
            encryptString = encryptBASE64(encryptBase64Bytes);
        } catch (Exception e) {
            LOGGER.error("Base64 encrypt failed，encrypt data:{}" + content, e);
        }
        return encryptString;
    }

    public static String decryptToString(String content, String password) {
        byte[] decryptBase64Bytes = null;
        try {
            decryptBase64Bytes = decryptBASE64(content);
        } catch (Exception e) {
            LOGGER.error("Base64 decrypt failed，decrypt data:{}" + content, e);
        }
        if (null == decryptBase64Bytes) {
            return null;
        }

        byte[] decryptBytes = decryptToBytes(decryptBase64Bytes, password);
        if (null == decryptBytes) {
            return null;
        }
        return new String(decryptBytes, Charsets.UTF_8);
    }

}
