package com.gabriel.faclovers.auth.company;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.faclovers.auth.shared.BusinessException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CompanyController.class)
@AutoConfigureMockMvc(addFilters = false)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CompanyService companyService;

    @Test
    void shouldCreateCompany() throws Exception {
        var request = new CompanyRequest("Facção Teste", "teste@faclovers.com", "123456", BigDecimal.valueOf(1000));

        when(companyService.create(any(CompanyRequest.class))).thenReturn(companyResponse());

        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/companies/1"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Facção Teste"))
                .andExpect(jsonPath("$.email").value("teste@faclovers.com"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldFindCompanyById() throws Exception {
        when(companyService.findById(1L)).thenReturn(companyResponse());

        mockMvc.perform(get("/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("teste@faclovers.com"));
    }

    @Test
    void shouldReturnTrueWhenCompanyExistsById() throws Exception {
        when(companyService.existsById(1L)).thenReturn(true);

        mockMvc.perform(get("/companies/1/exists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void shouldReturnFalseWhenCompanyDoesNotExistById() throws Exception {
        when(companyService.existsById(1L)).thenReturn(false);

        mockMvc.perform(get("/companies/1/exists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    void shouldReturnTrueWhenCompanyExistsByEmail() throws Exception {
        when(companyService.existsByEmail("teste@faclovers.com")).thenReturn(true);

        mockMvc.perform(get("/companies/email/teste@faclovers.com/exists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void shouldReturnFalseWhenCompanyDoesNotExistByEmail() throws Exception {
        when(companyService.existsByEmail("teste@faclovers.com")).thenReturn(false);

        mockMvc.perform(get("/companies/email/teste@faclovers.com/exists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {
        var request = new CompanyRequest("", "invalid-email", "123", BigDecimal.valueOf(-1));

        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.path").value("/companies"));
    }

    @Test
    void shouldReturnStandardErrorForBusinessException() throws Exception {
        when(companyService.findById(99L)).thenThrow(new BusinessException("Empresa nao encontrada", HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/companies/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Empresa nao encontrada"))
                .andExpect(jsonPath("$.path").value("/companies/99"));
    }

    @Test
    void shouldReturnValidationDetails() throws Exception {
        var request = new CompanyRequest("", "invalid-email", "123", null);

        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0]", containsString(":")));
    }

    private CompanyResponse companyResponse() {
        return new CompanyResponse(
                1L,
                "Facção Teste",
                "teste@faclovers.com",
                BigDecimal.valueOf(1000),
                true,
                LocalDateTime.of(2026, 5, 26, 10, 0),
                LocalDateTime.of(2026, 5, 26, 10, 0)
        );
    }
}
