package com.gamestore.gameplaystore.Security;

import com.gamestore.gameplaystore.Categoria.Categoria;
import com.gamestore.gameplaystore.Categoria.CategoriaRepository;
import com.gamestore.gameplaystore.Subcategoria.Subcategoria;
import com.gamestore.gameplaystore.Subcategoria.SubcategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf; // IMPORTANTE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    private Long catId;
    private Long subId;

    @BeforeEach
    void setUp() {
        Categoria cat = categoriaRepository.save(Categoria.builder().nombre("SecCat").descripcion("Desc").build());
        Subcategoria sub = subcategoriaRepository.save(Subcategoria.builder().nombre("SecSub").categoria(cat).activa(true).build());
        this.catId = cat.getId();
        this.subId = sub.getId();
    }

    @Test
    void createProductShouldFailWithoutAuth() throws Exception {
        String json = "{}";

        // Aquí no hace falta CSRF porque debe fallar antes (401 o 403)
        mockMvc.perform(post("/api/v1/productos")
                .with(csrf()) // Agregamos CSRF para asegurar que el fallo sea por Auth y no por falta de token CSRF
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized()); // O isForbidden() dependiendo de tu config
    }

    @WithMockUser(username = "user", roles = {"CLIENTE"})
    @Test
    void createShouldFailForClientRole() throws Exception {
        String json = "{}";

        mockMvc.perform(post("/api/v1/productos")
                .with(csrf()) // <--- IMPORTANTE: Simula el token de seguridad
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN", "ADMIN"})
    @Test
    void createShouldWorkForAdmin() throws Exception {
        // Usamos IDs reales
        String json = """
                {
                    "nombre": "Test Admin",
                    "descripcion": "Test Producto",
                    "precio": 10.0,
                    "stock": 5,
                    "tipo": "ACCESORIO",
                    "categoriaId": %d,
                    "subcategoriaId": %d
                }
                """.formatted(catId, subId);

        mockMvc.perform(post("/api/v1/productos") // <--- USAMOS LA RUTA CORRECTA
                .with(csrf()) 
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated()); // AHORA DEBERÍA DAR 201
    }
}