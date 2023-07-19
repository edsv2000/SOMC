package com.example.somc.data;

import org.json.JSONArray;

public class produccionData {
    private String numeroProduccion;

    private String numeroLote;

    private String numeroPedido;
    private String status;

    private JSONArray details;

    public produccionData(String numeroProduccion, String numeroLote, String numeroPedido, String status,JSONArray details) {
        this.numeroProduccion = numeroProduccion;
        this.numeroLote = numeroLote;
        this.numeroPedido = numeroPedido;
        this.status = status;
        this.details = details;

    }

    public JSONArray getDetails() {
        return details;
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
