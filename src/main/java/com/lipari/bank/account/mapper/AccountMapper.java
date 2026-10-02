package com.lipari.bank.account.mapper;

import com.lipari.bank.account.dto.AccountCreateRequest;
import com.lipari.bank.account.dto.AccountResponse;
import com.lipari.bank.account.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

  @Mapping(source = "initialBalance", target = "balance")
  @Mapping(source = "fiscalCode", target = "fiscalCode")
  Account toEntity(AccountCreateRequest request);

  AccountResponse toResponse(Account account);
}