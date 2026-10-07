package com.lipari.bank.account.mapper;

import com.lipari.bank.account.dto.AccountCreateRequest;
import com.lipari.bank.account.dto.AccountResponse;
import com.lipari.bank.account.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

  @Mapping(source = "initialBalance", target = "balance")
  @Mapping(target = "customer", ignore = true)
  Account toEntity(AccountCreateRequest request);

  @Mapping(source = "customer.fiscalCode", target = "fiscalCode")
  AccountResponse toResponse(Account account);
}