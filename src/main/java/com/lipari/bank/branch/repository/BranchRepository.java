package com.lipari.bank.branch.repository;

import com.lipari.bank.branch.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {}
