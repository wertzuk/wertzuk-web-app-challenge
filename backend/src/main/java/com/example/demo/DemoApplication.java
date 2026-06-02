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
  public ResponseEntity<Denomination> calculate(
      @RequestParam String amount,
      @RequestParam(defaultValue = "0") String previousAmount) {

    int amountInCents = convertParamToInt(amount);

    return ResponseEntity.ok().body(null);
  }

  private int convertParamToInt(String amount) {
    return new BigDecimal(amount).movePointRight(2).intValue();
  }

}