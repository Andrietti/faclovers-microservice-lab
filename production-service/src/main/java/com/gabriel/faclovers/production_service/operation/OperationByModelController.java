package com.gabriel.faclovers.production_service.operation;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operations")
@RequiredArgsConstructor
public class OperationByModelController {

    private final OperationByModelService operationService;

    @GetMapping("/model/{pieceModelId}")
    public ResponseEntity<List<OperationByModelResponse>> findByPieceModel(@PathVariable Long pieceModelId) {
        return ResponseEntity.ok(operationService.findByPieceModel(pieceModelId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationByModelResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(operationService.findById(id));
    }

    @PostMapping
    public ResponseEntity<OperationByModelResponse> create(@RequestBody @Valid OperationByModelRequest request) {
        OperationByModelResponse response = operationService.create(request);
        return ResponseEntity.created(URI.create("/operations/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OperationByModelResponse> update(@PathVariable Long id, @RequestBody @Valid OperationByModelUpdateRequest request) {
        return ResponseEntity.ok(operationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        operationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
