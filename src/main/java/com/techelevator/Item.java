package com.techelevator;

import java.math.BigDecimal;

public class Item {
    private String slot;
    private String name;
    private BigDecimal price;
    private String productType;

    private int quantity;

    private int initialQuantity;

    public Item(String slot, String name, BigDecimal price, String productType) {
        this.slot = slot;
        this.name = name;
        this.price = price;
        this.productType = productType;
        this.quantity = 5;
        this.initialQuantity = 5;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getInitialQuantity() {
        return initialQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}