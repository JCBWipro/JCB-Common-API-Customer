package com.wipro.jcb.livelink.app.auth.service;

import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.exception.UsernameNotFoundException;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:30-07-2024
 * project: JCB-Common-API-Customer
 */
@Service
public class ContactPasswordUpdateService {

    private static final Logger log = LoggerFactory.getLogger(ContactPasswordUpdateService.class);
    @Autowired
    private ContactRepo contactRepo;

    public int isFirstLogin(String userName) {
        return contactRepo.checkSysGenPassByContactID(userName);
    }
    public void updatePassword(String username, String newPassword) throws UsernameNotFoundException {
        ContactEntity contactEntity=contactRepo.findByUserContactId(username);
        if (contactEntity != null) {
            contactEntity.setPassword(newPassword);
            log.info("New Password is :{}",newPassword);
            // Reset the first login flag after updating the password
            contactEntity.setSysGeneratedPassword(0);
            contactEntity.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            contactRepo.save(contactEntity);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 12 || password.length() > 20) {
            return false;
        }

        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isWhitespace(c)) {
                hasSpecialChar = true;
            }
        }

        return hasLowercase && hasUppercase && hasDigit && hasSpecialChar && !password.toUpperCase().contains("JCB");
    }
        /*List<String> last5Passwords = passwordHistoryService.getLast5Passwords(username);
        return !last5Passwords.contains(password);*/
}
