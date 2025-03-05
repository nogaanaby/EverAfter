package com.example.everafter.generic_item;

public class Item {
    private int id;
    private String displayText;

    public Item(int id, String displayText) {
        this.id = id;
        this.displayText = displayText;
    }

    public int getId() {
        return id;
    }

    public String getDisplayText() {
        return displayText;
    }
}
