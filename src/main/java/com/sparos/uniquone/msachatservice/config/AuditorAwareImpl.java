package com.sparos.uniquone.msachatservice.config;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.domain.AuditorAware;

import javax.ws.rs.core.SecurityContext;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("Test");
    }
//    @Override
//    public Optional<String> getCurrentAuditor() {
//        return Optional.of("test");
//    }
}