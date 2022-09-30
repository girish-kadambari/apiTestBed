package com.testsigma.api_bed_test.repository;

import com.testsigma.api_bed_test.model.SpringSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringSessionRepository extends PagingAndSortingRepository<SpringSession, String>, JpaSpecificationExecutor<SpringSession>, JpaRepository<SpringSession, String> {
  Optional<SpringSession> findBySessionId(String sessionId);
  Optional<SpringSession> findByPrimaryId(String id);
}
