package com.wipro.jcb.livelink.app.user.web.service;

import com.wipro.jcb.livelink.app.user.web.entity.ContactEntity;
import com.wipro.jcb.livelink.app.user.web.repo.ContactRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:20-08-2024
 * project: JCB-Common-API-Customer
 */
@Service
public class UserRoleCheckService {

    @Autowired
    ContactRepo contactRepo;


    private static final Logger log = LoggerFactory.getLogger(UserRoleCheckService.class);

    public ResponseEntity<String> unlockAllAccounts() {
        log.info("Starting unlockAccounts job manually");
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
                        log.info("Unlocking user manually:- {}", contactEntity.getContactId());
                        contactRepo.unlockAllUserAccount(contactEntity.getContactId());
                        log.info("Manually Unlocking completed for the user:- {}", contactEntity.getContactId());
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
            else {
                log.info("No errorLogCounter or reset_pass_count > 0 found in the DB ...!!");
            }

            if (!accountsUnlocked && !countersReset) {
                log.info("No locked accounts found and no errorLogCounter or reset_pass_count > 0");
                return ResponseEntity.ok("No locked accounts found and no errorLogCounter or reset_pass_count > 0");
            } else if (accountsUnlocked) {
                log.info("Successfully Unlocked Accounts.");
                return ResponseEntity.ok("Locked Accounts have been Successfully Unlocked");
            } else {
                log.info("Successfully reset errorLogCounter or reset_pass_count.");
                return ResponseEntity.ok(" Successfully reset errorLogCounter or reset_pass_count.");
            }
        } catch (Exception e) {
            log.error("An error occurred while unlocking accounts.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to unlock accounts.");
        }
    }

    public void unlockAccountByUserID(String contactID) {
        log.info("Starting unlockAccount job manually..");
        LocalDateTime now = LocalDateTime.now();
        String lockedUser = contactRepo.findLockedUserByID(contactID);

        if (lockedUser != null) { // Check if a locked user was found
            ContactEntity contactEntity = contactRepo.findByUserContactId(contactID);
            if (contactEntity != null) { // Check if the ContactEntity was found
                LocalDateTime lastLocked = contactEntity.getLockedOutTime().toLocalDateTime();
                if (lastLocked != null && lastLocked.isBefore(now)) {
                    log.info("Manually Unlocking user: {}", contactEntity.getContactId());
                    contactRepo.unlockUserAccountByID(contactEntity.getContactId());
                    log.info("Manually User {} unlocked successfully.", contactEntity.getContactId());
                } else if (lastLocked != null) {
                    log.debug("User {} is not eligible for unlocking yet", contactEntity.getContactId());
                } else {
                    log.warn("User {} not found in database..", lockedUser);
                }
            } else {
                log.warn("User {} not found in database.", lockedUser);
            }
        } else {
            log.info("No locked user found with ID: {}", contactID);
        }
    }
}
