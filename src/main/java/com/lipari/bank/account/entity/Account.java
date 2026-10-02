package com.lipari.bank.account.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

  private Long id;
  private String iban;
  private String fiscalCode;
  private BigDecimal balance;
  private AccountStatus status;
  private LocalDateTime createdAt;
}