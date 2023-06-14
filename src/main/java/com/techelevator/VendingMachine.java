package com.techelevator;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class VendingMachine {
    private final Scanner scanner;
    private final Inventory inventory;
    private final Money money;
    private final SalesReport salesReport;
    private final List<Transaction> transaction;
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00");
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");


    public VendingMachine(String inventoryFilename) throws IOException {
        scanner = new Scanner(System.in);
        inventory = new Inventory(inventoryFilename);
        transaction = new ArrayList<>();
        money = new Money();
        salesReport = new SalesReport(this);
        loadInventoryFromFile(inventoryFilename);
    }

    private void loadInventoryFromFile(String inventoryFilename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inventoryFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String slotId = parts[0];
                String name = parts[1];
                BigDecimal price = new BigDecimal(parts[2]);
                String type = parts[3];
                Item item = new Item(slotId, name, price, type);
                inventory.put(slotId, item);
            }
        }
    }

    public void run() {
        displayMainMenu();
    }
    public void displayMainMenu() {
        while (true) {
            System.out.println("Main Menu");
            System.out.println("---------");
            System.out.println("(1) Display Vending Machine Items");
            System.out.println("(2) Purchase");
            System.out.println("(3) Exit");
            System.out.println("(4) Hidden Menu");
            System.out.println("\nPlease Select an option: ");

            String option = scanner.nextLine();
            switch (option) {
                case "1" -> displayItems();
                case "2" -> purchaseMenu();
                case "3" -> exit();
                case "4" -> displayHiddenMenu();
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }


    private void displayHiddenMenu() {
        System.out.println("Generating sales report...");
        salesReport.generateReport(inventory.getItems());
        System.out.println("Sales report generated successfully.");
    }


    private void displayItems() {
        System.out.println("\nCurrent Inventory");
        System.out.println("--------------------");
        for (Map.Entry<String, Item> entry : inventory.getItems().entrySet()) {
            String slotId = entry.getKey();
            Item item = entry.getValue();
            String status = (item.getQuantity() == 0) ? "SOLD OUT" : "";
            System.out.printf("%s | %s | %s | %s\n", slotId, item.getName(), MONEY_FORMAT.format(item.getPrice().doubleValue()), status);
        }
        System.out.println();
    }

    private void purchaseMenu() {
        System.out.println("\nPurchase Menu");
        System.out.println("----------------");
        System.out.println();
        System.out.println("(1) Feed Money");
        System.out.println("(2) Select Product");
        System.out.println("(3) Finish Transaction");
        System.out.print("\nPlease select an option: ");

        String option = scanner.nextLine();
        switch (option) {
            case "1" -> feedMoney();
            case "2" -> selectProduct();
            case "3" -> finishTransaction();
            default -> System.out.println("Invalid option.Please enter a number: 1, 2, or 3.");
        }
        System.out.println();
    }

    public void addTransaction(Transaction transaction) {
        this.transaction.add(transaction);
    }
    // updated this so change is dispensed on this action
    private void recordTransaction(Item item, BigDecimal prevBalance) {
        Date transctionDate = new Date();
        Transaction transaction = new Transaction(item, transctionDate, 1);
        this.transaction.add(transaction);

        //create a log entry
        String logEntry = String.format("%s %s %s %s %s",
                DATE_FORMAT.format(transctionDate),
                item.getName(), item.getSlot(), MONEY_FORMAT.format(item.getPrice()),
                1, MONEY_FORMAT.format(money.getBalance()));

        //Write to log entry to Log.txt file
        writeToLogFile(logEntry);

        //update sales report
        salesReport.updateSales(transaction);
    }

    private void writeToLogFile(String content) {
        try (FileWriter writer = new FileWriter("Log.txt", true)) {
            writer.write(content + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }

    private void feedMoney() {
        System.out.print("Enter the amount in whole dollars to feed: ($1, $2, $5, $10, or $20) ");
        try {
            int amount = Integer.parseInt(scanner.nextLine());
            BigDecimal prevBalance = money.getBalance();
            money.addMoney(BigDecimal.valueOf(amount));
            System.out.println("Successfully fed $" + amount);

            //create log entry
            String logEntry = String.format("%s FEED MONEY: $%s $%s",
                    DATE_FORMAT.format(new Date()), MONEY_FORMAT.format(amount),
                    MONEY_FORMAT.format(money.getBalance()));

            //write to log
            writeToLogFile(logEntry);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a whole dollar amount.");
        }
    }

    public Map<String, Integer> giveChange() {
        return money.giveChange();
    }
    private void selectProduct() {
        System.out.print("Enter the product slot identifier: ");
        String slotId = scanner.nextLine();
        Item item = inventory.getItem(slotId);
        BigDecimal prevBalance = money.getBalance();

        if (item == null) {
            System.out.println("Invalid slot identifier. Please try again.");
            return;
        }

        if (item.getQuantity() == 0) {
            System.out.println("Sorry, the selected item is sold out.");
            return;
        }

        if (money.getBalance().compareTo(item.getPrice()) < 0) {
            System.out.println("Insufficient funds. Please feed more money.");
            return;
        }

        inventory.dispenseItem(slotId);
        recordTransaction(item, prevBalance);
        money.subtractMoney(item.getPrice());
        System.out.println("Dispensed: " + item.getName());
        System.out.println("Price: " + MONEY_FORMAT.format(item.getPrice()));
        System.out.println("Remaining balance: " + MONEY_FORMAT.format(money.getBalance()));

        //Print message to item type
        switch (item.getProductType()) {
            case "Chip" -> System.out.println("Crunch Crunch, Yum!");
            case "Candy" -> System.out.println("Munch Munch, Yum!");
            case "Drink" -> System.out.println("Glug Glug, Yum!");
            case "Gum" -> System.out.println("Chew Chew, Yum!");
            default -> System.out.println("Enjoy your purchase!");

        }
        purchaseMenu();
    }
    private void finishTransaction() {
        Map<String, Integer> change = money.getChangeMap();
        BigDecimal remainingBalance = money.getBalance();
        System.out.println("Returning change: $" + MONEY_FORMAT.format(remainingBalance));

        for (Map.Entry<String, Integer> entry : change.entrySet()) {
            System.out.printf("%s: %d\n", entry.getKey(), entry.getValue());
        }

        money.resetBalance();
    }


    private void exit() {
        System.out.println("Thank you for using the Vending Machine!");
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            VendingMachine vm = new VendingMachine("/Users/Quita/Documents/gitexercises/rameko-rumph-student-exercises/module-1-capstone/src/main/vendingmachine.csv");
            vm.run();
        } catch (IOException e) {
            System.out.println("Error initializing the vending machine: " + e.getMessage());
        }
    }
}