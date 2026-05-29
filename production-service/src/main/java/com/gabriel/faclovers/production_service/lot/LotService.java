package com.gabriel.faclovers.production_service.lot;

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
public class LotService {

    private final LotRepository lotRepository;
    private final PieceModelRepository pieceModelRepository;
    private final LotMapper lotMapper;
    private final CurrentCompany currentCompany;

    @Transactional
    public LotResponse create(LotRequest request) {
        Long companyId = currentCompany.id();
        PieceModel pieceModel = findPieceModel(request.pieceModelId(), companyId);
        Lot lot = lotMapper.toEntity(request, pieceModel, companyId);
        return lotMapper.toResponse(lotRepository.save(lot));
    }

    @Transactional(readOnly = true)
    public List<LotResponse> findAll() {
        return lotRepository.findByCompanyIdAndActiveTrueOrderByStartDateDesc(currentCompany.id())
                .stream()
                .map(lotMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LotResponse> findByPieceModel(Long pieceModelId) {
        Long companyId = currentCompany.id();
        findPieceModel(pieceModelId, companyId);
        return lotRepository.findByCompanyIdAndPieceModelIdAndActiveTrueOrderByStartDateDesc(companyId, pieceModelId)
                .stream()
                .map(lotMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LotResponse findById(Long id) {
        return lotMapper.toResponse(findActiveEntity(id));
    }

    @Transactional
    public LotResponse update(Long id, LotUpdateRequest request) {
        Long companyId = currentCompany.id();
        Lot lot = findActiveEntity(id);
        PieceModel pieceModel = findPieceModel(request.pieceModelId(), companyId);

        lot.setPieceModel(pieceModel);
        lot.setDescription(request.description());
        lot.setPricePerPiece(request.pricePerPiece());
        lot.setStartDate(request.startDate());
        lot.setEndDate(request.endDate());
        lot.setTotalQuantity(request.totalQuantity());

        return lotMapper.toResponse(lotRepository.save(lot));
    }

    @Transactional
    public void delete(Long id) {
        Lot lot = findActiveEntity(id);
        lot.setActive(false);
        lotRepository.save(lot);
    }

    public Lot findActiveEntity(Long id) {
        return lotRepository.findByIdAndCompanyIdAndActiveTrue(id, currentCompany.id())
                .orElseThrow(() -> new NotFoundException("Lote não encontrado"));
    }

    private PieceModel findPieceModel(Long pieceModelId, Long companyId) {
        return pieceModelRepository.findByIdAndCompanyIdAndActiveTrue(pieceModelId, companyId)
                .orElseThrow(() -> new NotFoundException("Modelo de peça não encontrado"));
    }
}
