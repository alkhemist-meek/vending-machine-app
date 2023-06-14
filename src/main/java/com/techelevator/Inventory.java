package com.techelevator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

public class Inventory {
    private Map<String, Item> items;

    public Inventory(String inputFilePath) {
        items = new TreeMap<>();
        //Create the log.txt file
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            try {
                inputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadItems(inputFilePath);

    }


    private void loadItems(String inputFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] itemData = line.split("\\|");
                String slot = itemData[0];
                String name = itemData[1];
                BigDecimal price = BigDecimal.valueOf(Double.parseDouble(itemData[2]));
                String productType = itemData[3];

                items.put(slot, new Item(slot, name, price, productType));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Item getItem(String slot) {
        return items.get(slot);
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public void put(String slotId, Item item) {
        items.put(slotId, item);
    }


    public void dispenseItem(String slotId) {
    }

}

   