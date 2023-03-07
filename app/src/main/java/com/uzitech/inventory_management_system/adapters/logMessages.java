package com.uzitech.inventory_management_system.adapters;

import java.util.HashMap;
import java.util.Map;

public class logMessages {
    Map<Integer, String> messages;

    public logMessages() {
        createMessages();
    }

    void createMessages() {
        messages = new HashMap<>();

        //Splash logs
        messages.put(0, "User tried to access using an unsupported app version");
        messages.put(1, "is_login status set to false for existing login");
        messages.put(2, "is_active status is set to false for existing login");

        //Login logs
        messages.put(3, "is_login status set to true for new login");
        messages.put(4, "is_active status is set to false for new login");
        messages.put(5, "successful login by user");
    }

    public String getMessage(int id) {
        return messages.get(id);
    }
}
