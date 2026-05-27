package com.gabriel.faclovers.auth.company;

public final class CompanyMapper {

    private CompanyMapper() {
    }

    public static CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getEmail(),
                company.getDailyGoalAmount(),
                company.getActive(),
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }
}
