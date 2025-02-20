package com.example.cs5520_spring2025_a1_ycaptain;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ContactStorage {
    private static final String PREF_NAME = "ContactPrefs";
    private static final String CONTACTS_KEY = "contacts";
    private final SharedPreferences preferences;
    private final Gson gson;

    public ContactStorage(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveContacts(List<Contact> contacts) {
        String json = gson.toJson(contacts);
        preferences.edit().putString(CONTACTS_KEY, json).apply();
    }

    public List<Contact> loadContacts() {
        String json = preferences.getString(CONTACTS_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Contact>>() {}.getType();
        return gson.fromJson(json, type);
    }
} 