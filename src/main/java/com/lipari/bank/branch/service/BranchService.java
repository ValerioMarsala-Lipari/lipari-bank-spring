package com.lipari.bank.branch.service;

import com.lipari.bank.branch.dto.BranchCreateRequest;
import com.lipari.bank.branch.dto.BranchResponse;
import com.lipari.bank.branch.entity.Branch;
import com.lipari.bank.branch.mapper.BranchMapper;
import com.lipari.bank.branch.repository.BranchRepository;
import com.lipari.bank.shared.exception.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Cacheable("branches")
    public List<BranchResponse> findAll() {
        log.debug("Retrieving all branches");

        return branchRepository.findAll().stream().map(branchMapper::toResponse).toList();
    }

    public BranchResponse findById(Long id) {
        log.debug("Retrieving branch with id: {}", id);

        Branch branch = branchRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + id));

        return branchMapper.toResponse(branch);
    }

    @Transactional
    @CacheEvict(value = "branches", allEntries = true)
    public BranchResponse create(BranchCreateRequest request) {
        log.info("Creating new branch");

        Branch branch = branchMapper.toEntity(request);
        Branch saved = branchRepository.save(branch);

        log.info("Branch created with id: {}", saved.getId());

        return branchMapper.toResponse(saved);
    }

    @Transactional
    @CacheEvict(value = "branches", allEntries = true)
    public BranchResponse update(Long id, BranchCreateRequest request) {
        log.info("Updating branch with id: {}", id);

        Branch branch = branchRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + id));

        branch.setName(request.name());
        branch.setAddress(request.address());
        branch.setCity(request.city());
        branch.setPhone(request.phone());

        Branch updated = branchRepository.save(branch);

        return branchMapper.toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting branch with id: {}", id);

        if (!branchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Branch not found: " + id);
        }

        branchRepository.deleteById(id);
    }
}
