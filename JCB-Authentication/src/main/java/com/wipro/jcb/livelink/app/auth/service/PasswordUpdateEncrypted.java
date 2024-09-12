package com.wipro.jcb.livelink.app.auth.service;

import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:06-09-2024
 * project: JCB-Common-API-Customer
 */
@Service
public class PasswordUpdateEncrypted {

    @Autowired
    ContactRepo contactRepo;

    public void updatePasswordsFromExcel(String excelFilePath, int usernameColumn, int passwordColumn) throws IOException {

        // Open the Excel file
        FileInputStream excelFile = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);

        Map<String, String> userPasswords = new HashMap<>();

        for (Row row : sheet) {
            Cell usernameCell = row.getCell(usernameColumn);
            Cell passwordCell = row.getCell(passwordColumn);

            if (usernameCell != null && passwordCell != null && usernameCell.getCellType() == CellType.STRING && passwordCell.getCellType() == CellType.STRING) {

                String username = usernameCell.getStringCellValue();
                String password = passwordCell.getStringCellValue();

                // Encrypt the password
                String encryptedPassword = new BCryptPasswordEncoder().encode(password);

                // Store in the map
                userPasswords.put(username, encryptedPassword);
            }
        }

        workbook.close();

        userPasswords.forEach((username, encryptedPassword) -> {
            ContactEntity contact = contactRepo.findByUserContactId(username);
            if (contact != null) {
                contact.setPassword(encryptedPassword);
                contactRepo.save(contact); // Update the password in the database
            }
        });
    }
}
