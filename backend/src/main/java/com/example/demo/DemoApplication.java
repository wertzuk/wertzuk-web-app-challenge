package com.example.demo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {
  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @GetMapping("/calculate")
  public ResponseEntity<Denomination[]> calculate(
      @RequestParam String amount,
      @RequestParam(defaultValue = "0") String previousAmount) {

    validateAmount(amount);

    int amountInCents = convertParamToInt(amount);

    Denomination[] newDenominations = doCalculation(amountInCents);

    // If previous param is present, calculate previous denomations in order to be
    // able to get the differences
    if (previousAmount != "0" && !previousAmount.isEmpty()) {
      validateAmount(previousAmount);

      int oldAmount = convertParamToInt(previousAmount);
      Denomination[] oldDenominations = doCalculation(oldAmount);
      for (int i = 0; i < newDenominations.length; i++) {
        int difference = newDenominations[i].getCount() - oldDenominations[i].getCount();
        newDenominations[i].setDifference(difference);
      }
    }

    return ResponseEntity.ok().body(newDenominations);
  }

  @ExceptionHandler(InvalidAmountException.class)
  public ResponseEntity<Map<String, String>> handleInvalidAmount(InvalidAmountException e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", e.getMessage());
    return ResponseEntity.badRequest().body(error);
  }

  private void validateAmount(String amount) {
    if (amount == null || amount.isBlank()) {
      throw new InvalidAmountException("Amount cannot be empty.");
    }
    try {
      BigDecimal value = new BigDecimal(amount);
      if (value.compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidAmountException("Amount must be greater than zero.");
      }
      if (value.scale() > 2) {
        throw new InvalidAmountException("Amount cannot have more than 2 decimal places.");
      }
    } catch (NumberFormatException e) {
      throw new InvalidAmountException(
          "Invalid amount: " + amount + ". Use a positive number with up to 2 decimal places.");
    }
  }

  private int convertParamToInt(String amount) {
    return new BigDecimal(amount).movePointRight(2).intValue();
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

}