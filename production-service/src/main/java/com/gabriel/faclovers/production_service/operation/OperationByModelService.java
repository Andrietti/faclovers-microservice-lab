package com.gabriel.faclovers.production_service.operation;

import com.gabriel.faclovers.production_service.piecemodel.PieceModel;
import com.gabriel.faclovers.production_service.piecemodel.PieceModelRepository;
import com.gabriel.faclovers.production_service.security.CurrentCompany;
import com.gabriel.faclovers.production_service.shared.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OperationByModelService {

    private final OperationByModelRepository operationRepository;
    private final PieceModelRepository pieceModelRepository;
    private final OperationByModelMapper operationMapper;
    private final CurrentCompany currentCompany;

    @Transactional
    public OperationByModelResponse create(OperationByModelRequest request) {
        Long companyId = currentCompany.id();
        PieceModel pieceModel = findPieceModel(request.pieceModelId(), companyId);
        OperationByModel operation = operationMapper.toEntity(request, pieceModel, companyId);
        return operationMapper.toResponse(operationRepository.save(operation));
    }

    @Transactional(readOnly = true)
    public List<OperationByModelResponse> findByPieceModel(Long pieceModelId) {
        Long companyId = currentCompany.id();
        findPieceModel(pieceModelId, companyId);
        return operationRepository.findByCompanyIdAndPieceModelIdAndActiveTrueOrderBySequenceOrder(companyId, pieceModelId)
                .stream()
                .map(operationMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OperationByModelResponse findById(Long id) {
        return operationMapper.toResponse(findActiveEntity(id));
    }

    @Transactional
    public OperationByModelResponse update(Long id, OperationByModelUpdateRequest request) {
        Long companyId = currentCompany.id();
        OperationByModel operation = findActiveEntity(id);
        PieceModel pieceModel = findPieceModel(request.pieceModelId(), companyId);

        operation.setPieceModel(pieceModel);
        operation.setName(request.name());
        operation.setDescription(request.description());
        operation.setDurationMinutes(request.durationMinutes());
        operation.setSequenceOrder(request.sequenceOrder());

        return operationMapper.toResponse(operationRepository.save(operation));
    }

    @Transactional
    public void delete(Long id) {
        OperationByModel operation = findActiveEntity(id);
        operation.setActive(false);
        operationRepository.save(operation);
    }

    public OperationByModel findActiveEntity(Long id) {
        return operationRepository.findByIdAndCompanyIdAndActiveTrue(id, currentCompany.id())
                .orElseThrow(() -> new NotFoundException("Operação não encontrada"));
    }

    private PieceModel findPieceModel(Long pieceModelId, Long companyId) {
        return pieceModelRepository.findByIdAndCompanyIdAndActiveTrue(pieceModelId, companyId)
                .orElseThrow(() -> new NotFoundException("Modelo de peça não encontrado"));
    }
}
