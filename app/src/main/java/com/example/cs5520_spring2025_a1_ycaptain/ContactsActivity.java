package com.example.cs5520_spring2025_a1_ycaptain;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cs5520_spring2025_a1_ycaptain.databinding.ActivityContactsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private ActivityContactsBinding binding;
    private ContactsAdapter adapter;
    private List<Contact> contacts;
    private static final int CALL_PERMISSION_REQUEST_CODE = 123;
    private Contact selectedContact;
    private ContactStorage contactStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        contactStorage = new ContactStorage(this);
        contacts = contactStorage.loadContacts();
        setupRecyclerView();
        
        binding.fab.setOnClickListener(view -> showAddContactDialog());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupRecyclerView() {
        adapter = new ContactsAdapter(contacts, 
            contact -> makePhoneCall(contact),
            this::showEditContactDialog,
            this::deleteContact
        );
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void showAddContactDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null);
        EditText nameInput = dialogView.findViewById(R.id.editTextName);
        EditText phoneInput = dialogView.findViewById(R.id.editTextPhone);

        new AlertDialog.Builder(this)
            .setTitle("Add New Contact")
            .setView(dialogView)
            .setPositiveButton("Add", (dialog, which) -> {
                String name = nameInput.getText().toString();
                String phone = phoneInput.getText().toString();
                if (!name.isEmpty() && !phone.isEmpty()) {
                    addContact(name, phone);
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void addContact(String name, String phone) {
        Contact contact = new Contact(name, phone);
        contacts.add(contact);
        adapter.notifyItemInserted(contacts.size() - 1);
        contactStorage.saveContacts(contacts);
        showSnackbar("Contact added successfully", "Call", () -> makePhoneCall(contact));
    }

    private void showEditContactDialog(Contact contact) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null);
        EditText nameInput = dialogView.findViewById(R.id.editTextName);
        EditText phoneInput = dialogView.findViewById(R.id.editTextPhone);

        nameInput.setText(contact.getName());
        phoneInput.setText(contact.getPhone());

        new AlertDialog.Builder(this)
            .setTitle("Edit Contact")
            .setView(dialogView)
            .setPositiveButton("Save", (dialog, which) -> {
                String name = nameInput.getText().toString();
                String phone = phoneInput.getText().toString();
                if (!name.isEmpty() && !phone.isEmpty()) {
                    updateContact(contact, name, phone);
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void updateContact(Contact contact, String name, String phone) {
        int position = contacts.indexOf(contact);
        contact.setName(name);
        contact.setPhone(phone);
        adapter.notifyItemChanged(position);
        contactStorage.saveContacts(contacts);
        showSnackbar("Contact updated successfully", "Undo", () -> {
            // Implementation for undo
        });
    }

    private void deleteContact(Contact contact) {
        int position = contacts.indexOf(contact);
        contacts.remove(contact);
        adapter.notifyItemRemoved(position);
        contactStorage.saveContacts(contacts);
        showSnackbar("Contact deleted", "Undo", () -> {
            contacts.add(position, contact);
            adapter.notifyItemInserted(position);
            contactStorage.saveContacts(contacts);
        });
    }

    private void makePhoneCall(Contact contact) {
        selectedContact = contact;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PERMISSION_REQUEST_CODE);
        } else {
            startCall();
        }
    }

    private void startCall() {
        if (selectedContact != null) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + selectedContact.getPhone()));
            startActivity(intent);
        }
    }

    private void showSnackbar(String message, String actionText, Runnable action) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG)
                .setAction(actionText, v -> action.run())
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCall();
            }
        }
    }
} 