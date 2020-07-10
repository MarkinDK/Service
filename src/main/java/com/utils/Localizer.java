package com.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class Localizer {
    private ResourceBundle rb;

    public synchronized String getMessage(String language, String message) {
        if (language != null) {
            String[] local = language.split("-");
            rb = ResourceBundle.getBundle("messages",
                    new Locale(local[0], local[1]));
        } else rb = ResourceBundle.getBundle("messages");
        return rb.getString(message);
    }
}
