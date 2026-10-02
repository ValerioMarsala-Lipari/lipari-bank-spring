package com.lipari.bank.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

  private Long id;
  private String fiscalCode;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private CustomerStatus status;
}