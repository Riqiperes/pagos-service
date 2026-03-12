package com.parcial_unidad1.pagos_service.controller;

import java.util.List;
import java.util.Optional;

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

    // --- GET ALL ---
    @GetMapping
    public List<Pago> obtenerPagos() {
        logger.info("GET /pagos - Obteniendo todos los pagos.");
        return pagoRepository.findAll();
    }

    // --- GET BY ID ---
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable String id) {
        logger.info("GET /pagos/{} - Buscando pago.", id);
        return pagoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- CREATE (POST) ---
    @PostMapping
    public Pago realizarPago(@RequestBody Pago pago) {
        logger.info("POST /pagos - Realizando pago de {} para la orden: {}", pago.getMonto(), pago.getOrdenId());
        cloudWatchService.enviarLog("Nuevo pago registrado para la orden: " + pago.getOrdenId());
        return pagoRepository.save(pago);
    }

    // --- UPDATE (PUT) ---
    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizarPago(@PathVariable String id, @RequestBody Pago pagoDetalles) {
        logger.info("PUT /pagos/{} - Actualizando pago.", id);
        return pagoRepository.findById(id)
                .map(pago -> {
                    pago.setOrdenId(pagoDetalles.getOrdenId());
                    pago.setMetodoPago(pagoDetalles.getMetodoPago());
                    pago.setMonto(pagoDetalles.getMonto());
                    pago.setEstado(pagoDetalles.getEstado());
                    Pago actualizado = pagoRepository.save(pago);
                    cloudWatchService.enviarLog("Pago actualizado: " + id);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable String id) {
        logger.info("DELETE /pagos/{} - Eliminando pago.", id);
        if (pagoRepository.existsById(id)) {
            pagoRepository.deleteById(id);
            cloudWatchService.enviarLog("Pago eliminado: " + id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}