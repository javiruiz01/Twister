package test;

import org.json.JSONException;
import org.json.JSONObject;

import service.user.LoginService;

public class TestLogin {

    public static void main(String[] args) {

        JSONObject obj = LoginService.login("username", "password");

        try {
            System.out.println(obj.toString(1));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}
