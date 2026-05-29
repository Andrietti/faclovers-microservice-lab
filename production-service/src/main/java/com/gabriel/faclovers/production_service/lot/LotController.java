package com.gabriel.faclovers.production_service.lot;

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
@RequestMapping("/lots")
@RequiredArgsConstructor
public class LotController {

    private final LotService lotService;

    @GetMapping
    public ResponseEntity<List<LotResponse>> findAll() {
        return ResponseEntity.ok(lotService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LotResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(lotService.findById(id));
    }

    @GetMapping("/model/{pieceModelId}")
    public ResponseEntity<List<LotResponse>> findByPieceModel(@PathVariable Long pieceModelId) {
        return ResponseEntity.ok(lotService.findByPieceModel(pieceModelId));
    }

    @PostMapping
    public ResponseEntity<LotResponse> create(@RequestBody @Valid LotRequest request) {
        LotResponse response = lotService.create(request);
        return ResponseEntity.created(URI.create("/lots/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LotResponse> update(@PathVariable Long id, @RequestBody @Valid LotUpdateRequest request) {
        return ResponseEntity.ok(lotService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lotService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
