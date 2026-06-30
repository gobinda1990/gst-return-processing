package comtax.gov.webapp.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EncryptionUtil {

	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES/GCM/NoPadding";

	private static final int IV_LENGTH = 12;
	private static final int TAG_LENGTH = 128;

	@Value("${app.crypto.secret-key}")
	private String secretKeyValue;

	private SecretKeySpec secretKey;

	@PostConstruct
	public void init() {

		byte[] key = Base64.getDecoder().decode(secretKeyValue);
		secretKey = new SecretKeySpec(key, ALGORITHM);

		log.info("Encryption Utility Initialized.");
	}

	public String encrypt(String plainText) {

		try {

			byte[] iv = new byte[IV_LENGTH];
			SecureRandom secureRandom = new SecureRandom();
			secureRandom.nextBytes(iv);

			Cipher cipher = Cipher.getInstance(TRANSFORMATION);

			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH, iv));

			byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

			ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);

			buffer.put(iv);
			buffer.put(encrypted);

			return Base64.getEncoder().encodeToString(buffer.array());

		} catch (Exception ex) {

			log.error("Encryption Failed", ex);
			throw new RuntimeException("Unable to encrypt data.", ex);

		}

	}

	public String decrypt(String encryptedText) {

		try {

			byte[] decoded = Base64.getDecoder().decode(encryptedText);

			ByteBuffer buffer = ByteBuffer.wrap(decoded);

			byte[] iv = new byte[IV_LENGTH];
			buffer.get(iv);

			byte[] cipherText = new byte[buffer.remaining()];
			buffer.get(cipherText);

			Cipher cipher = Cipher.getInstance(TRANSFORMATION);

			cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH, iv));

			byte[] decrypted = cipher.doFinal(cipherText);

			return new String(decrypted, StandardCharsets.UTF_8);

		} catch (Exception ex) {

			log.error("Decryption Failed", ex);
			throw new RuntimeException("Unable to decrypt data.", ex);

		}

	}

//	public static void main(String[] args) throws Exception {
//
//		KeyGenerator generator = KeyGenerator.getInstance("AES");
//		generator.init(256);
//		SecretKey key = generator.generateKey();
//		System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
//	}

}
