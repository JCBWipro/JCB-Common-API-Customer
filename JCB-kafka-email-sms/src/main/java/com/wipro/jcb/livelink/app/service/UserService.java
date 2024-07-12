package com.wipro.jcb.livelink.app.service;

import com.wipro.jcb.livelink.app.entity.RegistrationResponse;
import com.wipro.jcb.livelink.app.entity.Contact;
import com.wipro.jcb.livelink.app.enums.ROLE;
import com.wipro.jcb.livelink.app.exception.PasswordUpdateException;
import com.wipro.jcb.livelink.app.model.OTP;
import com.wipro.jcb.livelink.app.model.UserRegistrationRequest;
import com.wipro.jcb.livelink.app.repository.UserRepository;
import com.wipro.jcb.livelink.app.service.sms.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:21-06-2024
 * project: JCB_NewRepo
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final long OTP_VALIDITY_MINUTES = 1;
    @Autowired
    private SmsService smsService;

    @Autowired
    private UserRepository userRepository;

    private String currentOtp;
    private long otpGenerationTime;

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$&*";

    private static final SecureRandom random = new SecureRandom();

    //Registration New User Details
    public RegistrationResponse registerUserDetails(UserRegistrationRequest request) {

        RegistrationResponse response = new RegistrationResponse();

        //checking for duplicate email
        if (userRepository.findByEmailId(request.getEmailId()) != null) {
            response.setError(new StringBuffer("Email Address already registered."));
            return response;
        }

        String otp = generateOTP();

        // Send OTP via SMS
        OTP sendOTP = new OTP(request.getMobileNumber(), otp);
        smsService.sendOTP(sendOTP);

        String plainPassword = generatePassword();

        // Store user data in DB with encryted password
        Contact user = new Contact();
        user.setRole(ROLE.valueOf(request.getRole()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMobileNumber(request.getMobileNumber());
        user.setEmailId(request.getEmailId());
        user.setPassword(new BCryptPasswordEncoder().encode(plainPassword));
        userRepository.save(user);

        //Password sent to the user mobile number,Send plain password via SMS
        smsService.sendPassword(plainPassword, request.getMobileNumber());

        response.setSuccess(true);
        response.setOtp(otp);
        return response;

    }

    public String generateOTP() {
        Random random = new Random();
        // Generates a number between 100000 and 999999
        int otpValue = 100000 + random.nextInt(900000);
        currentOtp = String.valueOf(otpValue);
        log.info("OTP is :{}", currentOtp);

        // Record the generation time for expiration handling
        otpGenerationTime = System.currentTimeMillis();

        return currentOtp;
    }

    public boolean isOtpValid(String userProvidedOtp) {
        if (currentOtp == null || userProvidedOtp == null) {
            log.info("OTP is not valid, please provide correct OTP");
            return false;
        }

        // Check if the OTP is expired
        long currentTime = System.currentTimeMillis();
        long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - otpGenerationTime);
        if (elapsedMinutes > OTP_VALIDITY_MINUTES) {
            log.info("OTP has Expired");
            return false;
        }
        // Compare the user-provided OTP with the generated one
        boolean isValid = currentOtp.equals(userProvidedOtp);
        log.info("OTP validation {}", isValid ? "Successful" : "Failed");
        return isValid;
    }

    // Call this method when generating a new OTP
    public void reGenerateOTP(String mobile) {
        String otp = generateOTP();
        OTP otp1 = new OTP(mobile, otp);
        smsService.sendOTP(otp1);
        log.info("Regenerated the OTP");
    }

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

    public void updateResetPassword(String password, String mobileNumber) {
        log.info("Mobile Number is :{}", mobileNumber);
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        log.info("Encrypted Password before saving to DB and value is : {}", encryptedPassword);

        try {
            userRepository.updatePasswordWithMobile(encryptedPassword, mobileNumber);
            log.info("Encrypted Password saved to DB and value is : {}", encryptedPassword);
        } catch (Exception e) {
            log.error("Error updating password for mobile number {}: {}", mobileNumber, e.getMessage(), e);
            throw new PasswordUpdateException("Failed to update password", e);
        }

    }

    //Registration of user with mobile number
    public RegistrationResponse registerUserByMobileNumber(String mobileNumber) {
        RegistrationResponse response = new RegistrationResponse();
        // 1. Check if user already exists
        if (userRepository.existsByMobileNumber(mobileNumber) != null) {
            response.setError(new StringBuffer("Mobile number already Exists"));
            return response;
        }
        // 2. Generate OTP (consider security and storage)
        String generatedOTP = generateOTP();

        // 3. Send OTP to the user
        OTP otp = new OTP(mobileNumber, generatedOTP);
        //smsService.sendOTP(otp);

        // 4. Returning success message
        response.setSuccess(true);
        response.setOtp(otp.getOtp());
        return response;

    }
}
