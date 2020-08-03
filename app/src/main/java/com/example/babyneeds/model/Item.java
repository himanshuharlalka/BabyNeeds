package com.example.babyneeds.model;

import java.sql.Date;

public class Item {
    private int id;
    private String itemName;
    private long itemQuantity;
    private String itemColor;
    private String itemSize;
    private String dateItemAdded;

    public Item() {
    }

    public Item(String itemName, long itemQuantity, String itemColor, String itemSize, String dateItemAdded) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemColor = itemColor;
        this.itemSize = itemSize;
        this.dateItemAdded=dateItemAdded;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(long itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }
}
