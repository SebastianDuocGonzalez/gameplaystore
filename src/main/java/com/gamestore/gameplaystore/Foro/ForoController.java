package com.gamestore.gameplaystore.Foro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Interfaces Repositorio (puedes ponerlas en sus propios archivos si prefieres)
interface NoticiaRepository extends JpaRepository<Noticia, Long> {}
interface EventoRepository extends JpaRepository<Evento, Long> {}

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*") // Ajustar según config CORS
public class ForoController {

    @Autowired private NoticiaRepository noticiaRepo;
    @Autowired private EventoRepository eventoRepo;

    // --- NOTICIAS ---
    @GetMapping("/noticias")
    public List<Noticia> listarNoticias() { return noticiaRepo.findAll(); }
    
    @PostMapping("/noticias") // Solo ADMIN (configurar en Security)
    public Noticia crearNoticia(@RequestBody Noticia n) { return noticiaRepo.save(n); }

    // --- EVENTOS ---
    @GetMapping("/eventos")
    public List<Evento> listarEventos() { return eventoRepo.findAll(); }
    
    @PostMapping("/eventos") // Solo ADMIN
    public Evento crearEvento(@RequestBody Evento e) { return eventoRepo.save(e); }
}