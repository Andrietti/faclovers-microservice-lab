package com.gabriel.faclovers.production_service;

import com.gabriel.faclovers.production_service.employee.Employee;
import com.gabriel.faclovers.production_service.lot.Lot;
import com.gabriel.faclovers.production_service.operation.OperationByModel;
import com.gabriel.faclovers.production_service.piecemodel.PieceModel;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class TestFixtures {

    private TestFixtures() {
    }

    public static Employee employee(Long id, Long companyId) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setCompanyId(companyId);
        employee.setName("Maria");
        employee.setRole("Costureira");
        employee.setActive(true);
        return employee;
    }

    public static PieceModel pieceModel(Long id, Long companyId) {
        PieceModel pieceModel = new PieceModel();
        pieceModel.setId(id);
        pieceModel.setCompanyId(companyId);
        pieceModel.setName("Calça Moletom");
        pieceModel.setActive(true);
        return pieceModel;
    }

    public static OperationByModel operation(Long id, Long companyId, PieceModel pieceModel) {
        OperationByModel operation = new OperationByModel();
        operation.setId(id);
        operation.setCompanyId(companyId);
        operation.setPieceModel(pieceModel);
        operation.setName("Costura lateral");
        operation.setDescription("Fechar lateral");
        operation.setDurationMinutes(10);
        operation.setSequenceOrder(1);
        operation.setActive(true);
        return operation;
    }

    public static Lot lot(Long id, Long companyId, PieceModel pieceModel) {
        Lot lot = new Lot();
        lot.setId(id);
        lot.setCompanyId(companyId);
        lot.setPieceModel(pieceModel);
        lot.setDescription("Lote Maio 01");
        lot.setPricePerPiece(new BigDecimal("12.50"));
        lot.setStartDate(LocalDate.of(2026, 5, 28));
        lot.setTotalQuantity(500);
        lot.setActive(true);
        return lot;
    }
}
