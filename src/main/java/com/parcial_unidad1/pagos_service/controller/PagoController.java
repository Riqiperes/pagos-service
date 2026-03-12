package com.parcial_unidad1.pagos_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // --- POST /pagos/procesar ---
    @PostMapping("/procesar")
    public Pago procesarPago(@RequestBody Pago pago) {
        logger.info("POST /pagos/procesar - Procesando pago para orden: {}", pago.getOrdenId());
        if (pago.getEstado() == null) pago.setEstado("PROCESADO");
        Pago guardado = pagoRepository.save(pago);
        cloudWatchService.enviarLog("Pago procesado ID: " + guardado.getId() + " para Orden: " + pago.getOrdenId());
        return guardado;
    }

    // --- GET /pagos/{id} ---
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable String id) {
        logger.info("GET /pagos/{} - Buscando pago.", id);
        return pagoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- GET /pagos/orden/{ordenId} ---
    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<Pago> obtenerPagoPorOrden(@PathVariable String ordenId) {
        logger.info("GET /pagos/orden/{} - Buscando pago de la orden.", ordenId);
        return pagoRepository.findByOrdenId(ordenId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- PUT /pagos/{id}/reembolso ---
    @PutMapping("/{id}/reembolso")
    public ResponseEntity<Pago> procesarReembolso(@PathVariable String id) {
        logger.info("PUT /pagos/{}/reembolso - Iniciando reembolso", id);
        return pagoRepository.findById(id)
                .map(pago -> {
                    pago.setEstado("REEMBOLSADO");
                    Pago actualizado = pagoRepository.save(pago);
                    cloudWatchService.enviarLog("Pago reembolsado ID: " + id);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}