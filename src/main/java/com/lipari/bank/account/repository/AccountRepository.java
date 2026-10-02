package com.lipari.bank.account.repository;

import com.lipari.bank.account.entity.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccountRepository {

  private final Map<Long, Account> accounts = new HashMap<>();

  private long nextId = 1L;

  public Account save(Account account) {
    if (account.getId() == null) {
      account.setId(nextId++);
    }

    accounts.put(account.getId(), account);
    return account;
  }

  public Account findById(Long id) {
    return accounts.get(id);
  }

  public List<Account> findAll() {
    return new ArrayList<>(accounts.values());
  }

  public void deleteById(Long id) {
    accounts.remove(id);
  }

  public boolean existsById(Long id) {
    return accounts.containsKey(id);
  }
}