package com.gabriel.faclovers.production_service.productionlog;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/production-logs")
@RequiredArgsConstructor
public class ProductionLogController {

    private final ProductionLogService productionLogService;

    @GetMapping
    public ResponseEntity<List<ProductionLogResponse>> findAll() {
        return ResponseEntity.ok(productionLogService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionLogResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productionLogService.findById(id));
    }

    @GetMapping("/lot/{lotId}")
    public ResponseEntity<List<ProductionLogResponse>> findByLot(@PathVariable Long lotId) {
        return ResponseEntity.ok(productionLogService.findByLot(lotId));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ProductionLogResponse>> findByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(productionLogService.findByEmployee(employeeId));
    }

    @GetMapping("/date")
    public ResponseEntity<List<ProductionLogResponse>> findByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(productionLogService.findByDate(date));
    }

    @PostMapping
    public ResponseEntity<ProductionLogResponse> create(@RequestBody @Valid ProductionLogRequest request) {
        ProductionLogResponse response = productionLogService.create(request);
        return ResponseEntity.created(URI.create("/production-logs/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productionLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
