package com.synway.vpay.repository;

import com.synway.vpay.entity.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, UUID>, JpaSpecificationExecutor<Monitor> {

    int countByAccountIdAndIdNotAndName(UUID accountId, UUID id, String name);

    List<Monitor> findByAccountId(UUID accountId);
}
