package com.lipari.bank.customer.service;

import com.lipari.bank.customer.dto.CustomerCreateRequest;
import com.lipari.bank.customer.dto.CustomerResponse;
import com.lipari.bank.customer.dto.CustomerUpdateRequest;
import com.lipari.bank.customer.entity.Customer;
import com.lipari.bank.customer.entity.CustomerStatus;
import com.lipari.bank.customer.mapper.CustomerMapper;
import com.lipari.bank.customer.repository.CustomerRepository;
import com.lipari.bank.customer.specification.CustomerSpecification;
import com.lipari.bank.shared.exception.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public List<CustomerResponse> findAll() {
        log.debug("Retrieving all customers");

        return customerRepository.findAll().stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    public CustomerResponse findById(Long id) {
        log.debug("Retrieving customer with id: {}", id);

        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        return customerMapper.toResponse(customer);
    }

    public CustomerResponse create(CustomerCreateRequest request) {
        log.info("Creating new customer for fiscal code: {}", request.fiscalCode());

        Customer customer = customerMapper.toEntity(request);

        customer.setStatus(CustomerStatus.ACTIVE);

        Customer saved = customerRepository.save(customer);

        log.info("Customer created with id: {}", saved.getId());

        return customerMapper.toResponse(saved);
    }

    public CustomerResponse update(Long id, CustomerUpdateRequest request) {
        log.info("Updating customer with id: {}", id);

        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customerMapper.updateEntityFromRequest(request, customer);

        Customer updated = customerRepository.save(customer);

        return customerMapper.toResponse(updated);
    }

    public void delete(Long id) {
        log.info("Deleting customer with id: {}", id);

        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }

        customerRepository.deleteById(id);
    }

    public CustomerResponse findByFiscalCode(String fiscalCode) {
        log.debug("Retrieving customer with fiscal code: {}", fiscalCode);

        Customer customer = customerRepository
                .findByFiscalCode(fiscalCode)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with fiscal code: " + fiscalCode));

        return customerMapper.toResponse(customer);
    }

    public List<CustomerResponse> search(String lastName, CustomerStatus status) {
        Specification<Customer> specification = Specification.allOf();

        if (lastName != null) {
            specification = specification.and(CustomerSpecification.hasLastName(lastName));
        }

        if (status != null) {
            specification = specification.and(CustomerSpecification.hasStatus(status));
        }

        return customerRepository.findAll(specification).stream()
                .map(customerMapper::toResponse)
                .toList();
    }
}
