package com.parcial_unidad1.pagos_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pagos")
public class Pago {

    @Id
    private String id;
    private String ordenId;
    private String metodoPago;
    private Double monto;
    private String estado;

    public Pago() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrdenId() { return ordenId; }
    public void setOrdenId(String ordenId) { this.ordenId = ordenId; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}