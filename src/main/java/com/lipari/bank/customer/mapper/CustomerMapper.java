package com.lipari.bank.customer.mapper;

import com.lipari.bank.customer.dto.CustomerCreateRequest;
import com.lipari.bank.customer.dto.CustomerResponse;
import com.lipari.bank.customer.dto.CustomerUpdateRequest;
import com.lipari.bank.customer.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

  Customer toEntity(CustomerCreateRequest request);

  CustomerResponse toResponse(Customer customer);

  void updateEntityFromRequest(
      CustomerUpdateRequest request,
      @MappingTarget Customer customer
  );
}