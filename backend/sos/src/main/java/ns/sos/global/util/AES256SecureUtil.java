package ns.sos.global.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Component
public class AES256SecureUtil {

    private static final int KEY_LENGTH = 32; // 32 bytes for AES-256
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12; // 12 bytes for GCM IV
    private static AES256SecureUtil instance;

    private final String key;
    private final byte[] keyBytes;

    public AES256SecureUtil(@Value("${spring.jwt.user-info-secret}") String secretKey) {
        if (secretKey.length() != KEY_LENGTH) {
            throw new IllegalArgumentException("Key length should be 32 bytes for AES-256 encryption.");
        }
        this.key = secretKey;
        this.keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    }

    @PostConstruct
    private void init() {
        if (keyBytes.length != KEY_LENGTH) {
            throw new IllegalArgumentException("Key length should be 32 bytes for AES-256 encryption.");
        }
        instance = this;
    }

    public String encrypt(String data, String iv) {
        try {
            byte[] ivBytes = generateIv(iv);
//            log.info("Encryption IV: {}", Base64.getEncoder().encodeToString(ivBytes));
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String encryptedString = Base64.getEncoder().encodeToString(encrypted);
//            log.info("Encrypted data: {}", encryptedString);
            return encryptedString;
        } catch (Exception e) {
//            log.error("Encryption error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String encryptedData, String iv) {
        try {
            byte[] ivBytes = generateIv(iv);
//            log.info("Decryption IV: {}", Base64.getEncoder().encodeToString(ivBytes));
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
//            log.info("Decoded encrypted data length: {}", decoded.length);
            if (decoded.length < 16) {
                throw new IllegalArgumentException("Decoded data is too short to contain an expected tag length of 16 bytes");
            }
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH_BIT, ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

            byte[] decrypted = cipher.doFinal(decoded);
            String decryptedString = new String(decrypted, StandardCharsets.UTF_8);
//            log.info("Decrypted data: {}", decryptedString);
            return decryptedString;
        } catch (Exception e) {
//            log.error("Decryption error: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private byte[] generateIv(String iv) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(iv.getBytes(StandardCharsets.UTF_8));
        return Arrays.copyOf(hash, IV_LENGTH_BYTE); // 12 bytes for GCM IV
    }
}
