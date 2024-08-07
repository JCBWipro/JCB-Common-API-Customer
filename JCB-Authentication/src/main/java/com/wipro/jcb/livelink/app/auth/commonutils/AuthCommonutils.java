package com.wipro.jcb.livelink.app.auth.commonutils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.entity.RoleEntity;
import com.wipro.jcb.livelink.app.auth.reponse.ContactResponse;

public class AuthCommonutils {

    public static String getRolesByID(String roleId) {
        Map<String, String> map = new HashMap<>();
        map.put("1", "JCB Account");
        map.put("6", "Dealer");
        map.put("8", "Customer");
        map.put("12", "Super Admin");
        return map.get(roleId);
    }

    public static ContactEntity convertObjectToDTO(ContactResponse contactReponse) {
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setContactId(contactReponse.getContactId());
        contactEntity.setPassword(contactReponse.getPassword());
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRole_id(contactReponse.getRoleId());
        contactEntity.setRole(roleEntity);
        
        return contactEntity;
    }

    private static final Random random = new Random();

    public static String generateUsername(String firstName) {
        int randomNumber = generateRandomNumber(6);
        if (firstName.length() < 4) {
            int length = firstName.length();
            return firstName.substring(0, length) + randomNumber;
        } else {

            return firstName.substring(0, 4) + randomNumber;
        }
    }

    private static int generateRandomNumber(int length) {
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;
        return random.nextInt(max - min + 1) + min;
    }

}
