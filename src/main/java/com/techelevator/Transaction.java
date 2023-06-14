package com.techelevator;

import java.util.Date;

public class Transaction {

    private final Item item;
    private final Date timestamp;
    private final int quantity;

    public Transaction(Item item, Date timestamp, int quantity) {
        this.item = item;
        this.timestamp = timestamp;
        this.quantity = quantity;

    }

    public Item getItem() {
        return item;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getQuantity() {
        return quantity;
    }


}
