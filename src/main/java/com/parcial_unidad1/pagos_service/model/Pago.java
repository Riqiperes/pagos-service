package com.parcial_unidad1.pagos_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pagos")
public class Pago {

    @Id
    private String id;
    private String ordenId; // Aquí guardaremos el ID de la orden que se está pagando
    private Double monto;
    private String estado; // Ej: "PROCESADO", "RECHAZADO"

    public Pago() {}

    public Pago(String ordenId, Double monto, String estado) {
        this.ordenId = ordenId;
        this.monto = monto;
        this.estado = estado;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrdenId() { return ordenId; }
    public void setOrdenId(String ordenId) { this.ordenId = ordenId; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}