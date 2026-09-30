package com.duoc.rabbitmqlogs;

import java.util.Set;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogProducerController {

    // Desafío SECURITY: se agrega al set de niveles válidos
    private static final Set<String> NIVELES_VALIDOS =
            Set.of("INFO", "WARNING", "ERROR", "SECURITY");

    private final RabbitTemplate rabbitTemplate;

    public LogProducerController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<String> sendLog(@RequestBody LogMessage logMessage) {
        if (logMessage.getLevel() == null || logMessage.getMessage() == null) {
            return ResponseEntity.badRequest().body("level y message son obligatorios");
        }

        String level = logMessage.getLevel().trim().toUpperCase();

        if (!NIVELES_VALIDOS.contains(level)) {
            return ResponseEntity.badRequest()
                    .body("Nivel invalido. Use INFO, WARNING, ERROR o SECURITY");
        }

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                level,
                logMessage.getMessage()
        );

        return ResponseEntity.ok("Log enviado con nivel: " + level);
    }
}
