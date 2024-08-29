package com.wipro.jcb.livelink.app.mob.auth.service;

import com.wipro.jcb.livelink.app.mob.auth.entity.User;
import com.wipro.jcb.livelink.app.mob.auth.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:29-08-2024
 * project: JCB-Common-API-Customer
 */

@EnableScheduling
@Service
public class AccountUnlockMobileService {

    private static final Logger log = LoggerFactory.getLogger(AccountUnlockMobileService.class);
    @Autowired
    UserRepository userRepository;

    @Scheduled(cron = "${unlock.accounts.cron}")
    public ResponseEntity<String> unlockAccounts() {
        log.info("Starting unlockAccounts job");
        try {
            boolean accountsUnlocked = false;
            boolean countersReset = false;

            LocalDateTime now = LocalDateTime.now();
            List<User> lockedUsers = userRepository.findLockedUsers();
            if (!lockedUsers.isEmpty()) {
                log.info("Found {} locked users", lockedUsers.size());
                for (User user : lockedUsers) {
                    LocalDateTime lastLocked = user.getLockedOutTime().toLocalDateTime();
                    if (lastLocked != null && lastLocked.isBefore(now)) {
                        log.info("Unlocking user:- {}", user.getUserName());
                        userRepository.unlockUserAccount(user.getUserName());
                        log.info("Unlocking completed for the user:- {}", user.getUserName());
                    }
                }
                accountsUnlocked = true;
            } else {
                log.info("No Locked users found in the DB ...!!");
            }

            /* Retrieve users with login_failed_count or reset_pass_count > 0
             and then reset back it to Zero(0) */
            List<User> usersToReset = userRepository.findErrLogResetCount();
            if (!usersToReset.isEmpty()) {
                log.info("Found {} users with errorLogCounter or reset_pass_count > 0", usersToReset.size());
                for (User user : usersToReset) {
                    log.info("Resetting counters for user:- {}", user.getUserName());
                    //Resetting back to Zero
                    userRepository.resetToZero(user.getUserName());
                }
                countersReset = true;
            }

            if (!accountsUnlocked && !countersReset) {
                log.info("No locked accounts found and no errorLogCounter or reset_pass_count > 0");
                return ResponseEntity.ok("No actions taken. No locked accounts or counter resets needed.");
            } else if (accountsUnlocked) {
                log.info("Successfully unlocked accounts.");
                return ResponseEntity.ok("Accounts unlocked Successfully.");
            } else {
                log.info("Successfully reset errorLogCounter or reset_pass_count.");
                return ResponseEntity.ok("Counters reset successfully.");
            }
        } catch (Exception e) {
            log.error("An error occurred while unlocking accounts.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to unlock accounts.");
        }
    }

}
