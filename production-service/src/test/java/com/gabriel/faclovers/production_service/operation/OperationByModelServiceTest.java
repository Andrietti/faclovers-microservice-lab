package com.gabriel.faclovers.production_service.operation;

import static com.gabriel.faclovers.production_service.TestFixtures.operation;
import static com.gabriel.faclovers.production_service.TestFixtures.pieceModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.faclovers.production_service.piecemodel.PieceModelRepository;
import com.gabriel.faclovers.production_service.security.CurrentCompany;
import com.gabriel.faclovers.production_service.shared.NotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OperationByModelServiceTest {

    @Mock
    private OperationByModelRepository operationRepository;

    @Mock
    private PieceModelRepository pieceModelRepository;

    @Spy
    private OperationByModelMapper operationMapper;

    @Mock
    private CurrentCompany currentCompany;

    @InjectMocks
    private OperationByModelService operationService;

    @Test
    void shouldCreateOperationWhenModelBelongsToLoggedCompany() {
        var model = pieceModel(1L, 1L);
        when(currentCompany.id()).thenReturn(1L);
        when(pieceModelRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(model));
        when(operationRepository.save(org.mockito.ArgumentMatchers.any(OperationByModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var request = new OperationByModelRequest(1L, "Costura lateral", "Fechar lateral", 10, 1);
        OperationByModelResponse response = operationService.create(request);

        assertThat(response.companyId()).isEqualTo(1L);
        assertThat(response.pieceModelId()).isEqualTo(1L);
    }

    @Test
    void shouldThrowWhenModelDoesNotBelongToLoggedCompany() {
        when(currentCompany.id()).thenReturn(1L);
        when(pieceModelRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.empty());

        var request = new OperationByModelRequest(1L, "Costura lateral", null, 10, 1);

        assertThatThrownBy(() -> operationService.create(request)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldListOperationsByModelAndCompanyId() {
        var model = pieceModel(1L, 1L);
        when(currentCompany.id()).thenReturn(1L);
        when(pieceModelRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(model));
        when(operationRepository.findByCompanyIdAndPieceModelIdAndActiveTrueOrderBySequenceOrder(1L, 1L))
                .thenReturn(List.of(operation(1L, 1L, model)));

        List<OperationByModelResponse> responses = operationService.findByPieceModel(1L);

        assertThat(responses).hasSize(1);
        verify(operationRepository).findByCompanyIdAndPieceModelIdAndActiveTrueOrderBySequenceOrder(1L, 1L);
    }
}
