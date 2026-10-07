package com.lipari.bank.customer.repository;

import com.lipari.bank.customer.entity.Customer;
import com.lipari.bank.customer.entity.CustomerStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Override
  @EntityGraph(attributePaths = "accounts")
  List<Customer> findAll();

  Optional<Customer> findByFiscalCode(String fiscalCode);

  List<Customer> findByStatus(CustomerStatus status);

  List<Customer> findByLastName(String lastName);

  List<Customer> findByLastNameAndStatus(
      String lastName,
      CustomerStatus status
  );
}