package com.techelevator;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

public class VendingMachineTest {
    private VendingMachine vendingMachine;
    private ByteArrayOutputStream testOutput;

    @Before
    public void setUp() throws IOException {
        // Use the provided inventory file
        String inventoryFilename = "vendingmachine.csv";
        vendingMachine = new VendingMachine(inventoryFilename);

        // Set up System.in and System.out to test user input and output
        testOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOutput));
    }

    @Test
    public void testDisplayMainMenu() {
        String testInput = "3\n"; // User input to exit
        System.setIn(new ByteArrayInputStream(testInput.getBytes()));

        vendingMachine.displayMainMenu();

        String output = testOutput.toString();
        assertTrue(output.contains("Main Menu"));
        assertTrue(output.contains("(1) Display Vending Machine Items"));
        assertTrue(output.contains("(2) Purchase"));
        assertTrue(output.contains("(3) Exit"));
    }

    @Test
    public void testDisplayItems() {
        String testInput = "1\n3\n"; // User input to display items and exit
        System.setIn(new ByteArrayInputStream(testInput.getBytes()));

        vendingMachine.displayMainMenu();

        String output = testOutput.toString();
        assertTrue(output.contains("Current Inventory"));
        assertTrue(output.contains("A1"));
        assertTrue(output.contains("Potato Crisps"));
    }

    // You can add more test cases for other functionalities like purchasing, feeding money, etc.
}