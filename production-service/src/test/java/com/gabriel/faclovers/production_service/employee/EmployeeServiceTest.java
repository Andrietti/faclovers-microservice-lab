package com.gabriel.faclovers.production_service.employee;

import static com.gabriel.faclovers.production_service.TestFixtures.employee;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private EmployeeMapper employeeMapper;

    @Mock
    private CurrentCompany currentCompany;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void shouldCreateEmployeeUsingCompanyIdFromContext() {
        when(currentCompany.id()).thenReturn(1L);
        when(employeeRepository.save(org.mockito.ArgumentMatchers.any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeResponse response = employeeService.create(new EmployeeRequest("Maria", "Costureira"));

        assertThat(response.companyId()).isEqualTo(1L);
        verify(employeeRepository).save(org.mockito.ArgumentMatchers.argThat(employee -> employee.getCompanyId().equals(1L)));
    }

    @Test
    void shouldListOnlyEmployeesFromLoggedCompany() {
        when(currentCompany.id()).thenReturn(1L);
        when(employeeRepository.findByCompanyIdAndActiveTrueOrderByName(1L)).thenReturn(List.of(employee(1L, 1L)));

        List<EmployeeResponse> responses = employeeService.findAll();

        assertThat(responses).hasSize(1);
        verify(employeeRepository).findByCompanyIdAndActiveTrueOrderByName(1L);
    }

    @Test
    void shouldFindEmployeeByIdAndCompanyId() {
        when(currentCompany.id()).thenReturn(1L);
        when(employeeRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(employee(1L, 1L)));

        EmployeeResponse response = employeeService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void shouldThrowWhenEmployeeDoesNotExistForCompany() {
        when(currentCompany.id()).thenReturn(1L);
        when(employeeRepository.findByIdAndCompanyIdAndActiveTrue(99L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.findById(99L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldSoftDeleteEmployee() {
        Employee employee = employee(1L, 1L);
        when(currentCompany.id()).thenReturn(1L);
        when(employeeRepository.findByIdAndCompanyIdAndActiveTrue(1L, 1L)).thenReturn(Optional.of(employee));

        employeeService.delete(1L);

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());
        assertThat(captor.getValue().getActive()).isFalse();
    }
}
