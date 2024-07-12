package com.wipro.jcb.livelink.app.auth.service;

import com.wipro.jcb.livelink.app.auth.exception.PasswordUpdateException;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResetPasswordService {

    private static final Logger log = LoggerFactory.getLogger(ResetPasswordService.class);

    @Autowired
    private ContactRepo contactRepo;

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$&*";

    private static final SecureRandom random = new SecureRandom();

    //Password Generation
    public String generatePassword() {
        List<Character> password = new ArrayList<>();
        // Ensure the password contains at least one of each type of character
        password.add(LOWER.charAt(random.nextInt(LOWER.length())));
        password.add(UPPER.charAt(random.nextInt(UPPER.length())));
        password.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.add(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // Fill the remaining length of the password with a mix of all character sets
        String allCharacters = LOWER + UPPER + DIGITS + SPECIAL;
        // Generate a random length between 12 and 15
        int randomLength = 12 + random.nextInt(4);
        for (int i = password.size(); i < randomLength; i++) {
            password.add(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        // Convert the list to a string and return it
        StringBuilder result = new StringBuilder(password.size());
        for (Character c : password) {
            result.append(c);
        }

        log.info("Result  is :{}", result);
        log.info("Password size is :{}", password);
        return result.toString();
    }

    public void updateResetPassword(String password, String userName) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        try {
            contactRepo.updatePasswordWithContactID(encryptedPassword, userName);
            log.info("Encrypted Password saved to DB and value is : {}", encryptedPassword);
        } catch (Exception e) {
            log.error("Error updating password for username {}: {}", userName, e.getMessage(), e);
            throw new PasswordUpdateException("Failed to update password", e);
        }

    }

}
