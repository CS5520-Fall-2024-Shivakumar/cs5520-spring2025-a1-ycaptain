package com.example.cs5520_spring2025_a1_ycaptain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private List<Contact> contacts;
    private OnContactClickListener onContactClickListener;
    private OnContactEditListener onContactEditListener;
    private OnContactDeleteListener onContactDeleteListener;

    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    public interface OnContactEditListener {
        void onContactEdit(Contact contact);
    }

    public interface OnContactDeleteListener {
        void onContactDelete(Contact contact);
    }

    public ContactsAdapter(List<Contact> contacts, 
                         OnContactClickListener clickListener,
                         OnContactEditListener editListener,
                         OnContactDeleteListener deleteListener) {
        this.contacts = contacts;
        this.onContactClickListener = clickListener;
        this.onContactEditListener = editListener;
        this.onContactDeleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView phoneText;
        private ImageButton editButton;
        private ImageButton deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textName);
            phoneText = itemView.findViewById(R.id.textPhone);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }

        public void bind(Contact contact) {
            nameText.setText(contact.getName());
            phoneText.setText(contact.getPhone());
            
            itemView.setOnClickListener(v -> onContactClickListener.onContactClick(contact));
            editButton.setOnClickListener(v -> onContactEditListener.onContactEdit(contact));
            deleteButton.setOnClickListener(v -> onContactDeleteListener.onContactDelete(contact));
        }
    }
} 