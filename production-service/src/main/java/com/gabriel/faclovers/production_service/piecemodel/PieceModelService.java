package com.gabriel.faclovers.production_service.piecemodel;

import com.gabriel.faclovers.production_service.security.CurrentCompany;
import com.gabriel.faclovers.production_service.shared.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PieceModelService {

    private final PieceModelRepository pieceModelRepository;
    private final PieceModelMapper pieceModelMapper;
    private final CurrentCompany currentCompany;

    @Transactional
    public PieceModelResponse create(PieceModelRequest request) {
        PieceModel pieceModel = pieceModelMapper.toEntity(request, currentCompany.id());
        return pieceModelMapper.toResponse(pieceModelRepository.save(pieceModel));
    }

    @Transactional(readOnly = true)
    public List<PieceModelResponse> findAll() {
        return pieceModelRepository.findByCompanyIdAndActiveTrueOrderByName(currentCompany.id())
                .stream()
                .map(pieceModelMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PieceModelResponse findById(Long id) {
        return pieceModelMapper.toResponse(findActiveEntity(id));
    }

    @Transactional
    public PieceModelResponse update(Long id, PieceModelUpdateRequest request) {
        PieceModel pieceModel = findActiveEntity(id);
        pieceModel.setName(request.name());
        return pieceModelMapper.toResponse(pieceModelRepository.save(pieceModel));
    }

    @Transactional
    public void delete(Long id) {
        PieceModel pieceModel = findActiveEntity(id);
        pieceModel.setActive(false);
        pieceModelRepository.save(pieceModel);
    }

    public PieceModel findActiveEntity(Long id) {
        return pieceModelRepository.findByIdAndCompanyIdAndActiveTrue(id, currentCompany.id())
                .orElseThrow(() -> new NotFoundException("Modelo de peça não encontrado"));
    }
}
