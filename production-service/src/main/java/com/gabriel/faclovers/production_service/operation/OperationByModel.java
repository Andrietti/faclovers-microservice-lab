package com.gabriel.faclovers.production_service.operation;

import com.gabriel.faclovers.production_service.piecemodel.PieceModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "production_operation_by_model")
@Getter
@Setter
@NoArgsConstructor
public class OperationByModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "piece_model_id", nullable = false)
    private PieceModel pieceModel;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private Integer sequenceOrder;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (active == null) {
            active = true;
        }
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
