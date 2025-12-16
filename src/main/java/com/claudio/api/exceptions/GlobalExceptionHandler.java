package com.claudio.api.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
// Esta notación escucha errores globales en los controladores
@ControllerAdvice
public class GlobalExceptionHandler {

    // Manejo cuando fallan todos los reintentos (Concurrencia extrema)
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, Object>> handleConcurrencyFailure(ObjectOptimisticLockingFailureException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 409);
        body.put("error", "Conflict");
        body.put("message", "El sistema está saturado. Por favor, intenta de nuevo en unos segundos.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // Manejo cuando se acaba el stock (Tus RuntimeException)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessError(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        // Bad Request
        body.put("status", 400);
        body.put("error", "Business Error");
        // Aquí saldrá "Out of stock" o "Product not found"
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Captura cualquier otro error no previsto
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralError(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 500);
        body.put("error", "Internal Server Error");
        body.put("message", "Algo salió mal en el servidor.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
