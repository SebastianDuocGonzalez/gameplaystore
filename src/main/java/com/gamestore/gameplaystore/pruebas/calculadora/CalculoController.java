package com.gamestore.gameplaystore.pruebas.calculadora;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; 

@RestController
@RequestMapping("/calculadora")
public class CalculoController {
    private int count = 0; // contador
    
    @GetMapping("contador")
    public String contador() {
        count++;
        return "estoy en el contador, hast entrado/recargado a esta ruta un numero de " + count + " veces";
    }
public class SumadorController {
    @GetMapping("/sumar")
    public String sumar(@RequestParam int a, @RequestParam int b) {
        int suma = a + b;
        return "La suma de " + a + " y " + b + " es: " + suma;
    }}


    
}

