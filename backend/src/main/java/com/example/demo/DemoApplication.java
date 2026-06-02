package com.example.demo;

import java.math.BigDecimal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
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

    int amountInCents = convertParamToInt(amount);

    Denomination[] newDenominations = doCalculation(amountInCents);

    // If previous param is present, calculate previous denomations in order to be
    // able to get the differences
    if (previousAmount != "0" && !previousAmount.isEmpty()) {
      int oldAmount = convertParamToInt(previousAmount);
      Denomination[] oldDenominations = doCalculation(oldAmount);
      for (int i = 0; i < newDenominations.length; i++) {
        int difference = newDenominations[i].getCount() - oldDenominations[i].getCount();
        newDenominations[i].setDifference(difference);
      }
    }

    return ResponseEntity.ok().body(newDenominations);
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