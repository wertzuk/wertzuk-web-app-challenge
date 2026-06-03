package com.example.demo;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class CalculationService {
    public Denomination[] calculate(String amount, String previousAmount) {
        int newAmount = convertParamToInt(amount);
        Denomination[] denominations = doCalculation(newAmount);
        boolean includeDifference = previousAmount != null && !previousAmount.isEmpty();

        // If previous param is present, calculate previous denomations in order to be
        // able to get the differences
        if (includeDifference) {
            int oldAmount = convertParamToInt(previousAmount);
            Denomination[] oldDenominations = doCalculation(oldAmount);
            for (int i = 0; i < denominations.length; i++) {
                int difference = denominations[i].getCount() - oldDenominations[i].getCount();
                denominations[i].setDifference(difference);
            }
        }

        return denominations;
    }

    private Denomination[] doCalculation(int amount) {
        Denomination[] denominations = {
                new Denomination(20000),
                new Denomination(10000),
                new Denomination(5000),
                new Denomination(2000),
                new Denomination(1000),
                new Denomination(500),
                new Denomination(200),
                new Denomination(100),
                new Denomination(50),
                new Denomination(20),
                new Denomination(10),
                new Denomination(5),
                new Denomination(2),
                new Denomination(1),
        };

        for (Denomination denom : denominations) {
            int count = amount / denom.getValue();
            denom.setCount(count);
            denom.setDifference(count);
            amount = amount % denom.getValue();
        }

        return denominations;
    }

    private int convertParamToInt(String amount) {
        return new BigDecimal(amount).movePointRight(2).intValue();
    }
}
