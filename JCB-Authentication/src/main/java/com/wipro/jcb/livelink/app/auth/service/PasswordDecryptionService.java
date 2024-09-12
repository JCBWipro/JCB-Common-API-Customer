package com.wipro.jcb.livelink.app.auth.service;

import com.wipro.jcb.livelink.app.auth.repo.ContactRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:08-09-2024
 * project: JCB-Common-API-Customer
 */
@Slf4j
@Service
public class PasswordDecryptionService {

    @Autowired
    ContactRepo contactRepo;

    public ByteArrayInputStream decryptAndExportPasswords() throws IOException {
        // Fetch decrypted passwords from the database
        List<Map<String, Object>> decryptedPasswords = contactRepo.getDecryptedPasswords();

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("UsernameDecryptedPassword");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Contact_ID");
            headerRow.createCell(1).setCellValue("Password");

            // Populate data rows
            int rowNum = 1;
            for (Map<String, Object> entry : decryptedPasswords) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue((String) entry.get("contact_id"));
                row.createCell(1).setCellValue((String) entry.get("password_decrypt"));
            }

            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            log.error("Error creating Excel file: {}", e.getMessage(), e);
            throw new IOException("Failed to export decrypted passwords to Excel.", e);
        }
    }
}