package com.wipro.jcb.livelink.app.user.web.service;

import com.wipro.jcb.livelink.app.user.web.entity.ContactEntity;
import com.wipro.jcb.livelink.app.user.web.repo.ContactRepo;
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
 * Date:15-08-2024
 * project: JCB-Common-API-Customer
 */
@EnableScheduling
@Service
public class AccountUnlockService {

    private static final Logger log = LoggerFactory.getLogger(AccountUnlockService.class);

    @Autowired
    ContactRepo contactRepo;

    /**
     * This method unlocks accounts that have been locked due to exceeding login attempts.
     * It also resets the errorLogCounter and reset_pass_count for all users.
     * This method is scheduled to run periodically based on the configured cron expression.
     *
     * @return ResponseEntity containing a message indicating the outcome of the operation.
     */
    @Scheduled(cron = "${unlock.accounts.cron}")
    public ResponseEntity<String> unlockAllAccounts() {
        log.info("Starting unlockAccounts job");
        try {
            boolean accountsUnlocked = false;
            boolean countersReset = false;

            LocalDateTime now = LocalDateTime.now();
            List<ContactEntity> lockedUsers = contactRepo.findLockedUsers();
            if (!lockedUsers.isEmpty()) {
                log.info("Found {} locked users", lockedUsers.size());
                for (ContactEntity contactEntity : lockedUsers) {
                    LocalDateTime lastLocked = contactEntity.getLockedOutTime().toLocalDateTime();
                    if (lastLocked != null && lastLocked.isBefore(now)) {
                        log.info("Unlocking user:- {}", contactEntity.getContactId());
                        contactRepo.unlockAllUserAccount(contactEntity.getContactId());
                        log.info("Unlocking completed for the user:- {}", contactEntity.getContactId());
                    }
                }
                accountsUnlocked = true;
            } else {
                log.info("No Locked users found in the DB ...!!");
            }

            /* Retrieve users with errorLogCounter or reset_pass_count > 0
             and then reset back it to Zero(0) */
            List<ContactEntity> usersToReset = contactRepo.findErrLogResetCount();
            if (!usersToReset.isEmpty()) {
                log.info("Found {} users with errorLogCounter or reset_pass_count > 0", usersToReset.size());
                for (ContactEntity contactEntity : usersToReset) {
                    log.info("Resetting counters for user:- {}", contactEntity.getContactId());
                    //Resetting back to Zero
                    contactRepo.resetToZero(contactEntity.getContactId());
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

