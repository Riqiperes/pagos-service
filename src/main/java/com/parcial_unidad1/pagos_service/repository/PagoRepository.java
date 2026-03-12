package com.parcial_unidad1.pagos_service.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.parcial_unidad1.pagos_service.model.Pago;

public interface PagoRepository extends MongoRepository<Pago, String> {
    Optional<Pago> findByOrdenId(String ordenId);
}