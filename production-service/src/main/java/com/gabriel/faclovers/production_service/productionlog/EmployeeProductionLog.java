package com.gabriel.faclovers.production_service.productionlog;

import com.gabriel.faclovers.production_service.employee.Employee;
import com.gabriel.faclovers.production_service.lot.Lot;
import com.gabriel.faclovers.production_service.operation.OperationByModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "production_employee_production_log")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeProductionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lot_id", nullable = false)
    private Lot lot;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operation_id", nullable = false)
    private OperationByModel operation;

    @Column(nullable = false)
    private LocalDateTime producedAt;

    @Column(nullable = false)
    private Integer quantityProduced;

    private String notes;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        if (producedAt == null) {
            producedAt = createdAt;
        }
    }
}
