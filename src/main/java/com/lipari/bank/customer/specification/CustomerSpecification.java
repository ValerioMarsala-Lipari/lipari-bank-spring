package com.lipari.bank.customer.specification;

import com.lipari.bank.customer.entity.Customer;
import com.lipari.bank.customer.entity.CustomerStatus;
import org.springframework.data.jpa.domain.Specification;

public final class CustomerSpecification {

    private CustomerSpecification() {}

    public static Specification<Customer> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("lastName"), lastName);
    }

    public static Specification<Customer> hasStatus(CustomerStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }
}
