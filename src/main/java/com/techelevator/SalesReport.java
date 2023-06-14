package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class SalesReport {
    private final SimpleDateFormat FILENAME_FORMAT = new SimpleDateFormat("yyyyMMdd-hhmmss");
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    private final DecimalFormat MONEY_FORMAT = new DecimalFormat("$0.00");
    private Map<String, BigDecimal> sales;
    private VendingMachine vendingMachine;

    public SalesReport(VendingMachine vendingMachine){
        this.vendingMachine = vendingMachine;
        sales = new HashMap<>();
    }

    public Map<String, BigDecimal> getSales() {
        return sales;
    }

    //add the readTotalSales() method here
    private BigDecimal readTotalSales() {
        File totalSalesFile = new File("total_sales.txt");
        if (totalSalesFile.exists()) {
            try (Scanner scanner = new Scanner(totalSalesFile)) {
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    return new BigDecimal(line);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error reading total sales file: " + e.getMessage());
            }
        }

        return BigDecimal.ZERO;
    }

    //add the writeTotalSales() method here
    private void writeTotalSales() {
        BigDecimal totalSales = BigDecimal.ZERO;
        for (BigDecimal sale : sales.values()) {
            totalSales = totalSales.add(sale);
        }
        try (FileWriter writer = new FileWriter("total_sales.txt")) {
            writer.write(totalSales.toString());
        } catch (IOException e) {
            System.out.println("Error writing total sales file: " + e.getMessage());
        }
    }


    public void generateReport(Map<String, Item> inventory) {
        BigDecimal totalSales = readTotalSales();
        StringBuilder reportBuilder = new StringBuilder();
        int initialQuantity = 5;

        reportBuilder.append("Sales Report\n");
        reportBuilder.append("--------------------\n");
        reportBuilder.append("Date: ").append(DATE_FORMAT.format(new Date())).append("\n\n");
        for (Map.Entry<String, Item> entry: inventory.entrySet()) {
            Item item = entry.getValue();
           BigDecimal itemSales = sales.get(item.getName());
           if (itemSales == null){
               itemSales = BigDecimal.ZERO;
           }
           int itemCount = item.getInitialQuantity() - item.getQuantity();
            totalSales = totalSales.add(itemSales);
            reportBuilder.append(item.getName()).append(" | ").append(itemCount).append("\n");
        }
        reportBuilder.append("\nTotal Sales: ").append(MONEY_FORMAT.format(totalSales)).append("\n");
        String filename = FILENAME_FORMAT.format(new Date()) + "Log.txt";
        writeToFile(filename, reportBuilder.toString());
        }

        public void updateSales(Transaction transaction) {
        Item item = transaction.getItem();
        int quantitySold = transaction.getQuantity();
            BigDecimal currentSales = sales.get(item.getName());
        if (currentSales == null) {
            currentSales = BigDecimal.ZERO;
        }
        BigDecimal updatedSales = currentSales.add(item.getPrice().multiply(BigDecimal.valueOf(quantitySold)));
        sales.put(item.getName(), updatedSales);

        int updatedQuantity = item.getQuantity() - quantitySold;
        item.setQuantity(updatedQuantity);

        writeTotalSales();
        vendingMachine.addTransaction(transaction);

        }

        private void writeToFile(String filename, String content) {
        try {
            File file = new File(filename);
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
            System.out.println("Sales report saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error writing sales report to file.");
            e.printStackTrace();
        }
    }


}
