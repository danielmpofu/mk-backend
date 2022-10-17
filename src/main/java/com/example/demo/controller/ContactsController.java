package com.example.demo.controller;

import com.example.demo.service.impl.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entities.Contact;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contacts/")
public class ContactsController {

    @Autowired
    ContactServiceImpl contactService;

    @GetMapping("/{id}")
    public Contact getOneContact(@PathVariable int id) {
        return contactService.getContact(id);
    }

    @GetMapping("/")
    public List<Contact> getContacts() {
        return contactService.getAllContacts();
    }

    @PostMapping("/")
    public Contact saveContact(@RequestBody Contact contact) {
        return contactService.saveContact(contact);
    }

}
