package com.parcial_unidad1.pagos_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // <-- Ajusta tu paquete
import org.springframework.web.bind.annotation.RequestBody; // <-- Ajusta tu paquete
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parcial_unidad1.pagos_service.model.Pago;
import com.parcial_unidad1.pagos_service.repository.PagoRepository;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    private static final Logger logger = LoggerFactory.getLogger(PagoController.class);

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private CloudWatchService cloudWatchService;

    @GetMapping
    public List<Pago> obtenerPagos() {
        logger.info("Solicitud recibida: Consultando todos los pagos");
        return pagoRepository.findAll();
    }

    @PostMapping
    public Pago procesarPago(@RequestBody Pago pago) {
        pago.setEstado("PROCESADO"); // Asumimos que el pago pasa directo
        logger.info("Solicitud recibida: Procesando pago por ${} para la orden ID: {}", pago.getMonto(), pago.getOrdenId());
        cloudWatchService.enviarLog("¡Éxito! Procesando nuevo pago para la orden: " + pago.getOrdenId());
        return pagoRepository.save(pago);
    }
}