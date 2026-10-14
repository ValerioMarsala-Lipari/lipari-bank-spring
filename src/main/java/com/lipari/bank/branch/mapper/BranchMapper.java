package com.lipari.bank.branch.mapper;

import com.lipari.bank.branch.dto.BranchCreateRequest;
import com.lipari.bank.branch.dto.BranchResponse;
import com.lipari.bank.branch.entity.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    @Mapping(target = "id", ignore = true)
    Branch toEntity(BranchCreateRequest request);

    BranchResponse toResponse(Branch branch);
}
