package com.gabriel.faclovers.production_service.lot;

import static com.gabriel.faclovers.production_service.TestFixtures.lot;
import static com.gabriel.faclovers.production_service.TestFixtures.pieceModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.faclovers.production_service.piecemodel.PieceModelRepository;
import com.gabriel.faclovers.production_service.security.CurrentCompany;
import com.gabriel.faclovers.production_service.shared.NotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LotServiceTest {

    @Mock
    private LotRepository lotRepository;

    @Mock
    private PieceModelRepository pieceModelRepository;

    @Spy
    private LotMapper lotMapper;

    @Mock
    private CurrentCompany currentCompany;

    @InjectMocks
    private LotService lotService;

    @Test
    void shouldCreateLotWhenModelBelongsToLoggedCompany() {
        var model = pieceModel(1L, 1L);
        when(currentCompany.id()).thenReturn(1L);
        when(pieceModelRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(model));
        when(lotRepository.save(org.mockito.ArgumentMatchers.any(Lot.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var request = new LotRequest(1L, "Lote Maio 01", new BigDecimal("12.50"), LocalDate.of(2026, 5, 28), null, 500);
        LotResponse response = lotService.create(request);

        assertThat(response.companyId()).isEqualTo(1L);
        assertThat(response.pieceModelId()).isEqualTo(1L);
    }

    @Test
    void shouldThrowWhenModelDoesNotBelongToLoggedCompany() {
        when(currentCompany.id()).thenReturn(1L);
        when(pieceModelRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.empty());

        var request = new LotRequest(1L, "Lote Maio 01", BigDecimal.ONE, LocalDate.of(2026, 5, 28), null, 500);

        assertThatThrownBy(() -> lotService.create(request)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldListLotsByCompanyId() {
        var model = pieceModel(1L, 1L);
        when(currentCompany.id()).thenReturn(1L);
        when(lotRepository.findByCompanyIdAndActiveTrueOrderByStartDateDesc(1L)).thenReturn(List.of(lot(1L, 1L, model)));

        List<LotResponse> responses = lotService.findAll();

        assertThat(responses).hasSize(1);
        verify(lotRepository).findByCompanyIdAndActiveTrueOrderByStartDateDesc(1L);
    }
}
