package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/socios")
    public String mostrarSocios(@RequestParam String name) {
          return "Hola, " + name + "! Aquí están los socios.";
    }
}
