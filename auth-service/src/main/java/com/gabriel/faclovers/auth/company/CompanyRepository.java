package com.gabriel.faclovers.auth.company;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByEmail(String email);

    Optional<Company> findByEmail(String email);
}
