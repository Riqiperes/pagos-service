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

    @PostMapping("/procesar")
    public Pago procesarPago(@RequestBody Pago pago) {
        if (pago.getEstado() == null) pago.setEstado("PROCESADO");
        Pago guardado = pagoRepository.save(pago);
        cloudWatchService.enviarLog("Acceso a POST /pagos/procesar - Orden: " + pago.getOrdenId() + " (Monto: " + pago.getMonto() + ")");
        return guardado;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable String id) {
        cloudWatchService.enviarLog("Acceso a GET /pagos/" + id + " - Detalle de pago");
        return pagoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<Pago> obtenerPagoPorOrden(@PathVariable String ordenId) {
        cloudWatchService.enviarLog("Acceso a GET /pagos/orden/" + ordenId + " - Consultar pago de orden");
        return pagoRepository.findByOrdenId(ordenId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/reembolso")
    public ResponseEntity<Pago> procesarReembolso(@PathVariable String id) {
        return pagoRepository.findById(id)
                .map(pago -> {
                    pago.setEstado("REEMBOLSADO");
                    Pago actualizado = pagoRepository.save(pago);
                    cloudWatchService.enviarLog("Acceso a PUT /pagos/" + id + "/reembolso - Pago anulado exitosamente");
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}