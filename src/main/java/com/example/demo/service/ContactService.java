package com.example.demo.service;

import com.example.demo.entities.Contact;
import java.util.List;

public interface ContactService {
    List<Contact> getAllContacts();
    Contact getContact(int id);
    Contact saveContact(Contact contact);
}
