package com.gabriel.faclovers.auth.login;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void shouldLoginReturningToken() throws Exception {
        var request = new LoginRequest("teste@faclovers.com", "123456");

        when(authService.login(any(LoginRequest.class))).thenReturn(new LoginResponse(
                "jwt-token",
                "Bearer",
                1440L,
                1L,
                "Facção Teste",
                "teste@faclovers.com"
        ));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresInMinutes").value(1440L))
                .andExpect(jsonPath("$.companyId").value(1L))
                .andExpect(jsonPath("$.companyEmail").value("teste@faclovers.com"));
    }

    @Test
    void shouldReturnBadRequestWhenLoginRequestIsInvalid() throws Exception {
        var request = new LoginRequest("invalid-email", "");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void shouldValidateTokenSuccessfully() throws Exception {
        when(authService.validateToken("Bearer valid-token")).thenReturn(new TokenValidationResponse(
                true,
                1L,
                "Facção Teste",
                "teste@faclovers.com"
        ));

        mockMvc.perform(get("/auth/validate")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.companyId").value(1L))
                .andExpect(jsonPath("$.companyName").value("Facção Teste"))
                .andExpect(jsonPath("$.companyEmail").value("teste@faclovers.com"));
    }

    @Test
    void shouldReturnInvalidWhenAuthorizationIsMissing() throws Exception {
        when(authService.validateToken(null)).thenReturn(new TokenValidationResponse(false, null, null, null));

        mockMvc.perform(get("/auth/validate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.companyId").doesNotExist())
                .andExpect(jsonPath("$.companyName").doesNotExist())
                .andExpect(jsonPath("$.companyEmail").doesNotExist());
    }

    @Test
    void shouldReturnInvalidWhenAuthorizationIsInvalid() throws Exception {
        when(authService.validateToken("Bearer invalid-token")).thenReturn(new TokenValidationResponse(false, null, null, null));

        mockMvc.perform(get("/auth/validate")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.companyId").doesNotExist())
                .andExpect(jsonPath("$.companyName").doesNotExist())
                .andExpect(jsonPath("$.companyEmail").doesNotExist());
    }
}
