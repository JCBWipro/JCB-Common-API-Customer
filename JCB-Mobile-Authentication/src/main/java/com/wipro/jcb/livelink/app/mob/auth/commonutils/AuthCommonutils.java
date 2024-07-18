package com.wipro.jcb.livelink.app.mob.auth.commonutils;

import java.util.List;
import java.util.Random;

import com.wipro.jcb.livelink.app.mob.auth.entity.User;
import com.wipro.jcb.livelink.app.mob.auth.enums.UserType;


public class AuthCommonutils {
	
	public static User convertObjectToDTO(List<Object[]> objects) {
		User user = new User();
		for(Object[] object : objects) {
			user.setUserName(object[0].toString());
			user.setPassword(object[1].toString());
			user.setUserType(UserType.valueOf(object[2].toString()));
			user.setRoleName(object[3].toString());
		}
        return user;
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
