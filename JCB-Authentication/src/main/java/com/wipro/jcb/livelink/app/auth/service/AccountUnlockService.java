package com.wipro.jcb.livelink.app.auth.service;

import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Scheduled(cron = "${unlock.accounts.cron}")
    public void unlockAccounts() {
        log.info("Starting unlockAccounts job");
        LocalDateTime now = LocalDateTime.now();
        List<ContactEntity> lockedUsers = contactRepo.findLockedUsers();

        if (lockedUsers.isEmpty()) {
            log.info("No locked users found.");
            return;
        }

        log.info("Found {} locked users", lockedUsers.size());

        for (ContactEntity contactEntity : lockedUsers) {
            LocalDateTime lastLocked = contactEntity.getLockedOutTime().toLocalDateTime();
            if (lastLocked != null && lastLocked.isBefore(now)) {
                log.info("Unlocking user:- {}", contactEntity.getContactId());
                contactRepo.unlockUserAccount(contactEntity.getContactId());
            } else {
                if (lastLocked == null) {
                    log.warn("User {} has null lastLocked value", contactEntity.getContactId());
                } else {
                    log.debug("User {} is not eligible for unlocking yet", contactEntity.getContactId());
                }
            }
        }

        log.info("UnlockAccounts job completed.");
    }
}
