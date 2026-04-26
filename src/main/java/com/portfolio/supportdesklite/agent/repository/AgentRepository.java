package com.portfolio.supportdesklite.agent.repository;

import com.portfolio.supportdesklite.agent.model.Agent;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, UUID> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<Agent> findByEmailIgnoreCase(String email);

    Page<Agent> findByActive(boolean active, Pageable pageable);
}
