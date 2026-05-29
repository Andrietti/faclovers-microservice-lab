package com.gabriel.faclovers.production_service.employee;

import com.gabriel.faclovers.production_service.security.CurrentCompany;
import com.gabriel.faclovers.production_service.shared.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final CurrentCompany currentCompany;

    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        Employee employee = employeeMapper.toEntity(request, currentCompany.id());
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll() {
        return employeeRepository.findByCompanyIdAndActiveTrueOrderByName(currentCompany.id())
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) {
        return employeeMapper.toResponse(findActiveEntity(id));
    }

    @Transactional
    public EmployeeResponse update(Long id, EmployeeUpdateRequest request) {
        Employee employee = findActiveEntity(id);
        employee.setName(request.name());
        employee.setRole(request.role());
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    public void delete(Long id) {
        Employee employee = findActiveEntity(id);
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    public Employee findActiveEntity(Long id) {
        return employeeRepository.findByIdAndCompanyIdAndActiveTrue(id, currentCompany.id())
                .orElseThrow(() -> new NotFoundException("Funcionária não encontrada"));
    }
}
