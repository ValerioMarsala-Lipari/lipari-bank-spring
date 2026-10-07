package com.lipari.bank.customer.repository;

import com.lipari.bank.customer.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository
    extends JpaRepository<Customer, Long>,
    JpaSpecificationExecutor<Customer> {

  @Override
  @EntityGraph(attributePaths = "accounts")
  List<Customer> findAll();

  Optional<Customer> findByFiscalCode(String fiscalCode);
}