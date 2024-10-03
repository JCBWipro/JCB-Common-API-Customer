package com.wipro.jcb.livelink.app.mob.auth.commonutils;

import java.util.List;
import java.util.Random;

import com.wipro.jcb.livelink.app.mob.auth.entity.User;
import com.wipro.jcb.livelink.app.mob.auth.enums.UserType;
import com.wipro.jcb.livelink.app.mob.auth.response.UserResponse;


public class AuthCommonutils {
	
	public static User convertObjectToDTO(List<UserResponse> objects) {
		User user = new User();
		for(UserResponse response : objects) {
			user.setUserName(response.getUSER_ID());
			user.setPassword(response.getpassword());
			user.setUserType(UserType.valueOf(response.getuserType()));
			user.setRoleName(response.getroleName());
			user.setFirstName(response.getfirstName());
			user.setLastName(response.getlastName());
			user.setEmail(response.getemail());
			user.setPhoneNumber(response.getphoneNumber());
			user.setImage(response.getimage());
			user.setThumbnail(response.getthumbnail());
			user.setCountry(response.getcountry());
			user.setSmsLanguage(response.getsmsLanguage());
			user.setTimeZone(response.gettimeZone());
			user.setLanguage(response.getlanguage());
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
