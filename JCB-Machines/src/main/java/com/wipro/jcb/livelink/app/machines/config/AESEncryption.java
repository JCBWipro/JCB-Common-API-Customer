package com.wipro.jcb.livelink.app.machines.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:25-10-2024
 */
public class AESEncryption {
    private static final Logger logger = LoggerFactory.getLogger(AESEncryption.class);
    private static SecretKeySpec secretKey;

    /**
     * This is to set pre decided key and create secretKey of SecretKeySpec
     *
     * @param myKey
     *            is pre decided secret key to encrypt and decrypt
     */
    public static void setKey(String myKey) {
        MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Unable to find secret key generation algo: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Encrypt the password by using secret
     *
     * @param strToEncrypt
     *            is credentials passed
     * @param secret
     *            is secret generated from setSecret
     * @return ecrypted crendentials
     */
    public static String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            logger.error("Encoding not supported: {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decrypt the password by using secret
     *
     * @param strToDecrypt
     *            encrypted string to decrypt
     * @param secret
     *            key used to decrypt
     * @return the decrypt credentials
     */
    public static String decrypt(String strToDecrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error while decrypting: {}", e.getMessage());
        }
        return null;
    }
}
