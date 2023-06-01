package com.example.somc.data;

public class produccionData {
    private String numeroProduccion;

    private String numeroLote;

    private String numeroPedido;
    private String status;

    public produccionData(String numeroProduccion, String numeroLote, String numeroPedido, String status) {
        this.numeroProduccion = numeroProduccion;
        this.numeroLote = numeroLote;
        this.numeroPedido = numeroPedido;
        this.status = status;

    }

    public String getStatus() {
        return status;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public String getNumeroProduccion() {
        return numeroProduccion;
    }
}
