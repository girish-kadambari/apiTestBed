package com.testsigma.api_bed_test.model;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SPRING_SESSION")
@Data
@Log4j2
public class SpringSession {

  @Id
  @Column(name = "PRIMARY_ID")
  private String primaryId;

  @Column(name = "SESSION_ID")
  private String sessionId;

  @Column(name = "CREATION_TIME")
  private Long creationTime;

  @Column(name = "LAST_ACCESS_TIME")
  private Long lastAccessTime;

  @Column(name = "MAX_INACTIVE_INTERVAL")
  private Long maxInactiveInterval;

  @Column(name = "EXPIRY_TIME")
  private Long expiryTime;

  @Column(name = "PRINCIPAL_NAME")
  private String principalName;
}
