package org.opencms.user.service;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.opencms.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorService {
    
    private final CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6);
    private final TimeProvider timeProvider = new SystemTimeProvider();
    private final CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
    private final SecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final QrGenerator qrGenerator = new ZxingPngQrGenerator();
    
    public String generateSecret() {
        return secretGenerator.generate();
    }
    
    public boolean verifyCode(String secret, String code) {
        return verifier.isValidCode(secret, code);
    }
    
    public boolean hasSecondFactor(User user) {
        return user.getAdditionalInfo().stream()
            .anyMatch(data -> "two_factor_auth".equals(data.getDataKey()));
    }
    
    public String getSecret(User user) {
        return user.getAdditionalInfo().stream()
            .filter(data -> "two_factor_auth".equals(data.getDataKey()))
            .findFirst()
            .map(data -> data.getDataValue())
            .orElse(null);
    }
    
    public String generateQrCodeUrl(String secret, String username) {
        QrData data = new QrData.Builder()
            .label(username)
            .secret(secret)
            .issuer("OpenCMS")
            .algorithm(HashingAlgorithm.SHA1)
            .digits(6)
            .period(30)
            .build();
        
        try {
            byte[] imageData = qrGenerator.generate(data);
            return "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(imageData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
}
