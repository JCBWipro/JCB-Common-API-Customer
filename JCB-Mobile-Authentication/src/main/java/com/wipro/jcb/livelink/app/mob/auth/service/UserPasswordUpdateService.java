package com.wipro.jcb.livelink.app.mob.auth.service;

import com.wipro.jcb.livelink.app.mob.auth.entity.User;
import com.wipro.jcb.livelink.app.mob.auth.exception.UsernameNotFoundException;
import com.wipro.jcb.livelink.app.mob.auth.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:31-07-2024
 * project: JCB-Common-API-Customer
 */
@Service
public class UserPasswordUpdateService {

    private static final Logger log = LoggerFactory.getLogger(UserPasswordUpdateService.class);
    @Autowired
    UserRepository userRepository;

    public int isFirstLogin(String userName) {
        return userRepository.checkSysGenPassByUserID(userName);
    }

    public void updatePassword(String username, String newPassword) throws UsernameNotFoundException {
        User user = userRepository.findByUserUserId(username);
        if (user != null) {
            user.setPassword(newPassword);
            log.info("New Password is :{}", newPassword);
            // Reset the first login flag after updating the password
            user.setSysGenPass(0);
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.save(user);
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
