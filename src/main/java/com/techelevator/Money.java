package com.techelevator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Money {
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00");;
    private BigDecimal balance;

    public Money() {
        balance = BigDecimal.ZERO;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void addMoney(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public boolean subtractMoney(BigDecimal amount) {
        if (balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
            return true;
        }
        return false;
    }



    public Map<String, Integer> giveChange() {
        int quarters = 0;
        int dimes = 0;
        int nickels = 0;

        BigDecimal quarterValue = new BigDecimal("0.25");
        BigDecimal dimeValue = new BigDecimal("0.10");
        BigDecimal nickelValue = new BigDecimal("0.05");
        BigDecimal remainingBalance = balance;

        while (balance.compareTo(quarterValue) >= 0) {
            balance = remainingBalance.subtract(quarterValue);
            quarters++;
        }

        while (balance.compareTo(dimeValue) >= 0) {
            balance = remainingBalance.subtract(dimeValue);
            dimes++;
        }

        while (balance.compareTo(nickelValue) >= 0) {
            balance = remainingBalance.subtract(nickelValue);
            nickels++;
        }

        balance = BigDecimal.ZERO;

        Map<String, Integer> change = new HashMap<>();
        change.put("Quarters", quarters);
        change.put("Dimes", dimes);
        change.put("Nickels", nickels);


        return change;
    }


    public Map<String, Integer> calculateChange() {
        return giveChange();
    }

    public void resetBalance() {
        balance = BigDecimal.ZERO;
    }
    //added to show proper change balance.
    public Map<String, Integer> getChangeMap() {
        int quarters = 0;
        int dimes = 0;
        int nickels = 0;

        BigDecimal quarterValue = new BigDecimal("0.25");
        BigDecimal dimeValue = new BigDecimal("0.10");
        BigDecimal nickelValue = new BigDecimal("0.05");

        BigDecimal remainingBalance = balance;

        while (remainingBalance.compareTo(quarterValue) >= 0) {
            remainingBalance = remainingBalance.subtract(quarterValue);
            quarters++;
        }

        while (remainingBalance.compareTo(dimeValue) >= 0) {
            remainingBalance = remainingBalance.subtract(dimeValue);
            dimes++;
        }

        while (remainingBalance.compareTo(nickelValue) >= 0) {
            remainingBalance = remainingBalance.subtract(nickelValue);
            nickels++;
        }

        Map<String, Integer> changeMap = new HashMap<>();
        changeMap.put("Quarters", quarters);
        changeMap.put("Dimes", dimes);
        changeMap.put("Nickels", nickels);

        return changeMap;
    }
}