package com.lipari.bank.customer.repository;

import com.lipari.bank.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  Optional<Customer> findByFiscalCode(String fiscalCode);
}