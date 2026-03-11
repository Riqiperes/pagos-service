package com.parcial_unidad1.pagos_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.parcial_unidad1.pagos_service.model.Pago; // <-- Ajusta tu paquete aquí

@Repository
public interface PagoRepository extends MongoRepository<Pago, String> {
}