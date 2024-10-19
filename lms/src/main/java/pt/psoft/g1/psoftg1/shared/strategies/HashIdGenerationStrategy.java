package pt.psoft.g1.psoftg1.shared.strategies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.shared.model.IdGenerationStrategy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class HashIdGenerationStrategy implements IdGenerationStrategy<String> {
    @Value("${app.id.strategy.size}")
    private int idSize;

    @Override
    public String generateId() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            String uniqueInput = System.currentTimeMillis() + UUID.randomUUID().toString();
            byte[] hashBytes = digest.digest(uniqueInput.getBytes());

            // Convert to hex and take only 20 characters
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.substring(0, idSize);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash ID", e);
        }
    }
}
