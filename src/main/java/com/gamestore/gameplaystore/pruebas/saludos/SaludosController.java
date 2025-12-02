package com.gamestore.gameplaystore.pruebas.saludos;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/saludos")
public class SaludosController {

    @GetMapping("/hola")
    public String saludar() {
        return "¡Hola desde Gamestore!";
    }

}
