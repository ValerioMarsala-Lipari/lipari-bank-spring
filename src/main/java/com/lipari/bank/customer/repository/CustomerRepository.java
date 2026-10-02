package com.lipari.bank.customer.repository;

import com.lipari.bank.customer.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerRepository {

  private final Map<Long, Customer> customers = new HashMap<>();

  private long nextId = 1L;

  public Customer save(Customer customer) {
    if (customer.getId() == null) {
      customer.setId(nextId++);
    }

    customers.put(customer.getId(), customer);
    return customer;
  }

  public Customer findById(Long id) {
    return customers.get(id);
  }

  public List<Customer> findAll() {
    return new ArrayList<>(customers.values());
  }

  public void deleteById(Long id) {
    customers.remove(id);
  }

  public boolean existsById(Long id) {
    return customers.containsKey(id);
  }

  public Customer findByFiscalCode(String fiscalCode) {
    return customers.values()
        .stream()
        .filter(customer -> customer.getFiscalCode().equals(fiscalCode))
        .findFirst()
        .orElse(null);
  }
}