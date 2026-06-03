package com.example.demo;

import java.math.BigDecimal;
import java.util.Arrays;
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

  public CalculationService service;

  public DemoApplication(CalculationService service) {
    this.service = service;
  }

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @GetMapping("/calculate")
  public ResponseEntity<DenominationResponse[]> calculate(
      @RequestParam String amount,
      @RequestParam(required = false) String previousAmount) {

    validateAmount(amount);

    boolean includeDifference = previousAmount != null && !previousAmount.isEmpty();
    if (includeDifference) {
      validateAmount(previousAmount);
    }

    Denomination[] newDenominations = service.calculate(amount, previousAmount);

    DenominationResponse[] response = Arrays.stream(newDenominations)
        .filter(d -> d.getCount() > 0 || d.getDifference() != 0)
        .map(d -> toResponse(d, includeDifference))
        .toArray(DenominationResponse[]::new);

    return ResponseEntity.ok().body(response);
  }

  @ExceptionHandler(InvalidAmountException.class)
  public ResponseEntity<Map<String, String>> handleInvalidAmount(InvalidAmountException e) {
    Map<String, String> error = new HashMap<>();
    error.put("error", e.getMessage());
    return ResponseEntity.badRequest().body(error);
  }

  private DenominationResponse toResponse(Denomination d, boolean includeDifference) {
    String value = new BigDecimal(d.getValue()).movePointLeft(2).toPlainString();
    return new DenominationResponse(value, d.getCount(), d.getDifference());
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
}