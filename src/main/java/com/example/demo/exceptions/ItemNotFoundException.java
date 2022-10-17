package com.example.demo.exceptions;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(Long id,String itemName) {
        super(String.format("%s with Id %d was not found",itemName, id));
    }
}