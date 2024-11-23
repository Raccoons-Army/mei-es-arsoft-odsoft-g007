package pt.psoft.g1.psoftg1.shared.idGenerationStrategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.security.SecureRandom;

@PropertySource({"classpath:config/library.properties"})
public class HexIdGenerationStrategy implements IdGenerationStrategy<String> {
    private static final SecureRandom random = new SecureRandom();

    @Value("${charactersSize}")
    private int idSize;

    @Override
    public String generateId() {
        // If the size is odd, round up to the nearest even number
        // because each byte will be converted to 2 hex characters, eg. 24 characters = 12 bytes
        if (idSize % 2 != 0) {
            idSize++;
        }

        byte[] randomBytes = new byte[idSize];
        random.nextBytes(randomBytes);
        StringBuilder hexString = new StringBuilder();
        for (byte b : randomBytes) {
            hexString.append(String.format("%02x", b)); // Convert each byte to 2 hex chars
        }
        return hexString.toString();
    }
}
