package com.gabriel.faclovers.production_service.piecemodel;

import static com.gabriel.faclovers.production_service.TestFixtures.pieceModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
class PieceModelServiceTest {

    @Mock
    private PieceModelRepository pieceModelRepository;

    @Spy
    private PieceModelMapper pieceModelMapper;

    @Mock
    private CurrentCompany currentCompany;

    @InjectMocks
    private PieceModelService pieceModelService;

    @Test
    void shouldCreateModelUsingCompanyIdFromContext() {
        when(currentCompany.id()).thenReturn(1L);
        when(pieceModelRepository.save(org.mockito.ArgumentMatchers.any(PieceModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PieceModelResponse response = pieceModelService.create(new PieceModelRequest("Calça Moletom"));

        assertThat(response.companyId()).isEqualTo(1L);
    }

    @Test
    void shouldListOnlyModelsFromLoggedCompany() {
        when(currentCompany.id()).thenReturn(1L);
        when(pieceModelRepository.findByCompanyIdAndActiveTrueOrderByName(1L)).thenReturn(List.of(pieceModel(1L, 1L)));

        List<PieceModelResponse> responses = pieceModelService.findAll();

        assertThat(responses).hasSize(1);
        verify(pieceModelRepository).findByCompanyIdAndActiveTrueOrderByName(1L);
    }

    @Test
    void shouldFindModelByIdAndCompanyId() {
        when(currentCompany.id()).thenReturn(1L);
        when(pieceModelRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(pieceModel(1L, 1L)));

        PieceModelResponse response = pieceModelService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void shouldThrowWhenModelDoesNotExistForCompany() {
        when(currentCompany.id()).thenReturn(1L);
        when(pieceModelRepository.findByIdAndCompanyIdAndActiveTrue(99L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pieceModelService.findById(99L)).isInstanceOf(NotFoundException.class);
    }
}
