package com.wipro.jcb.livelink.app.auth.commonutils;

import com.wipro.jcb.livelink.app.auth.entity.ContactEntity;
import com.wipro.jcb.livelink.app.auth.entity.RoleEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AuthCommonutils {

    public static String getRolesByID(String roleId) {
        Map<String, String> map = new HashMap<>();
        map.put("1", "JCB Account");
        map.put("6", "Dealer");
        map.put("8", "Customer");
        map.put("12", "Super Admin");
        return map.get(roleId);
    }

    public static ContactEntity convertObjectToDTO(List<Object[]> objects) {
        ContactEntity contactEntity = new ContactEntity();
        for (Object[] object : objects) {
            contactEntity.setContactId(object[0].toString());
            contactEntity.setPassword(object[1].toString());
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setRole_id((int) object[2]);
            contactEntity.setRole(roleEntity);
        }
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
