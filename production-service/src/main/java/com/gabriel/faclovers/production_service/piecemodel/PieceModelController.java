package com.gabriel.faclovers.production_service.piecemodel;

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
@RequestMapping("/piece-models")
@RequiredArgsConstructor
public class PieceModelController {

    private final PieceModelService pieceModelService;

    @GetMapping
    public ResponseEntity<List<PieceModelResponse>> findAll() {
        return ResponseEntity.ok(pieceModelService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PieceModelResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pieceModelService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PieceModelResponse> create(@RequestBody @Valid PieceModelRequest request) {
        PieceModelResponse response = pieceModelService.create(request);
        return ResponseEntity.created(URI.create("/piece-models/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PieceModelResponse> update(@PathVariable Long id, @RequestBody @Valid PieceModelUpdateRequest request) {
        return ResponseEntity.ok(pieceModelService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pieceModelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
