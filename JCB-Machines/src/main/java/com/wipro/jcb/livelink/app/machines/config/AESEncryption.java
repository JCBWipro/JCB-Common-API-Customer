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
 * This class provides utility methods for AES encryption and decryption.
 */
public class AESEncryption {
    private static final Logger logger = LoggerFactory.getLogger(AESEncryption.class);
    private static SecretKeySpec secretKey; // Holds the secret key used for encryption/decryption

    /**
     * Sets the secret key for encryption/decryption.
     * This method generates a SHA-1 hash of the provided key and uses the first 16 bytes as the AES key.
     *
     * @param myKey The secret key string.
     */
    public static void setKey(String myKey) {
        MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8); // Convert key to bytes
            sha = MessageDigest.getInstance("SHA-1"); // Get SHA-1 message digest instance
            key = sha.digest(key); // Calculate SHA-1 hash of the key
            key = Arrays.copyOf(key, 16); // Use only the first 16 bytes of the hash
            secretKey = new SecretKeySpec(key, "AES"); // Create a SecretKeySpec for AES with the derived key
        } catch (NoSuchAlgorithmException e) {
            logger.error("Unable to find secret key generation algo: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Encrypts the given string using AES encryption.
     *
     * @param strToEncrypt The string to encrypt.
     * @param secret       The secret key to use for encryption.
     * @return The encrypted string encoded in Base64, or null if encryption fails.
     */
    public static String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret); // Set the secret key
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // Get AES cipher instance with ECB mode and PKCS5Padding
            cipher.init(Cipher.ENCRYPT_MODE, secretKey); // Initialize cipher in encryption mode
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8))); // Encrypt and encode in Base64
        } catch (Exception e) {
            logger.error("Encoding not supported: {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decrypts the given Base64 encoded string using AES decryption.
     *
     * @param strToDecrypt The Base64 encoded string to decrypt.
     * @param secret       The secret key to use for decryption.
     * @return The decrypted string, or null if decryption fails.
     */
    public static String decrypt(String strToDecrypt, String secret) {
        try {
            setKey(secret); // Set the secret key
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING"); // Get AES cipher instance with ECB mode and PKCS5Padding
            cipher.init(Cipher.DECRYPT_MODE, secretKey); // Initialize cipher in decryption mode
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt))); // Decrypt and convert to string
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error while decrypting: {}", e.getMessage());
        }
        return null;
    }
}