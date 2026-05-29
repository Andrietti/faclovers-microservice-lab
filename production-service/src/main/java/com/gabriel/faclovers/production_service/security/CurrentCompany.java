package com.gabriel.faclovers.production_service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentCompany {

    public CompanyContext get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CompanyContext companyContext)) {
            throw new IllegalStateException("Empresa autenticada não encontrada");
        }

        return companyContext;
    }

    public Long id() {
        return get().companyId();
    }
}
