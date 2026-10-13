
package com.lipari.bank.customer.service;

import com.lipari.bank.customer.dto.CustomerResponse;
import com.lipari.bank.customer.entity.Customer;
import com.lipari.bank.customer.mapper.CustomerMapper;
import com.lipari.bank.customer.repository.CustomerRepository;
import com.lipari.bank.shared.exception.ResourceNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private CustomerMapper customerMapper;

  @InjectMocks
  private CustomerService customerService;

  @Test
  void findById_whenCustomerExists_returnsCustomerResponse() {
    // Given
    Long customerId = 1L;
    Customer customer = new Customer();
    CustomerResponse expectedResponse =
        org.mockito.Mockito.mock(CustomerResponse.class);

    when(customerRepository.findById(customerId))
        .thenReturn(Optional.of(customer));
    when(customerMapper.toResponse(customer))
        .thenReturn(expectedResponse);

    // When
    CustomerResponse actualResponse = customerService.findById(customerId);

    // Then
    assertSame(expectedResponse, actualResponse);
    verify(customerRepository).findById(customerId);
    verify(customerMapper).toResponse(customer);
  }

  @Test
  void findById_whenCustomerDoesNotExist_throwsResourceNotFoundException() {
    // Given
    Long customerId = 99L;

    when(customerRepository.findById(customerId))
        .thenReturn(Optional.empty());

    // When / Then
    assertThrows(
        ResourceNotFoundException.class,
        () -> customerService.findById(customerId)
    );

    verify(customerRepository).findById(customerId);
    verify(customerMapper, never()).toResponse(
        org.mockito.ArgumentMatchers.any(Customer.class)
    );
  }
}