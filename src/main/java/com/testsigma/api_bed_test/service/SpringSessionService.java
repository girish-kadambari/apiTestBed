package com.testsigma.api_bed_test.service;


import com.testsigma.api_bed_test.model.SpringSession;
import com.testsigma.api_bed_test.repository.SpringSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpringSessionService {

  private final SpringSessionRepository springSessionRepository;

  public Optional<SpringSession> findBySessionId(String sessionId) {
    return springSessionRepository.findBySessionId(sessionId);
  }

  public Optional<SpringSession> findById(String id) {
    return springSessionRepository.findByPrimaryId(id);
  }
}
