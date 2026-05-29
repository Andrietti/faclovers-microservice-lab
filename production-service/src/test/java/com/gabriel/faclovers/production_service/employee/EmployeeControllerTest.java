package com.gabriel.faclovers.production_service.employee;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.faclovers.production_service.config.SecurityConfig;
import com.gabriel.faclovers.production_service.security.CompanyContext;
import com.gabriel.faclovers.production_service.security.JwtAuthenticationFilter;
import com.gabriel.faclovers.production_service.security.JwtService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldReturnUnauthorizedWithoutToken() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnSuccessWithValidToken() throws Exception {
        String token = "valid-token";
        when(jwtService.extractCompanyContext(token)).thenReturn(Optional.of(new CompanyContext(1L, "FacLovers", "empresa@faclovers.com")));
        when(jwtService.isValid(token)).thenReturn(true);
        when(employeeService.findAll()).thenReturn(List.of(new EmployeeResponse(
                1L,
                1L,
                "Maria",
                "Costureira",
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        )));

        mockMvc.perform(get("/employees").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateEmployeeWithValidToken() throws Exception {
        String token = "valid-token";
        var request = new EmployeeRequest("Maria", "Costureira");
        var response = new EmployeeResponse(1L, 1L, "Maria", "Costureira", true, LocalDateTime.now(), LocalDateTime.now());
        when(jwtService.extractCompanyContext(token)).thenReturn(Optional.of(new CompanyContext(1L, "FacLovers", "empresa@faclovers.com")));
        when(jwtService.isValid(token)).thenReturn(true);
        when(employeeService.create(request)).thenReturn(response);

        mockMvc.perform(post("/employees")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}
