package com.azurealstn.alog.repository.email;

import com.azurealstn.alog.domain.email.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long>, EmailAuthRepositoryCustom {
}
