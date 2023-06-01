package com.example.somc.data;

public class pedidosData {
    private String numero;

    private String empresa;

    private String fecha;
    private String status;

    public pedidosData(String numero, String empresa, String fecha, String status) {
        this.numero = numero;
        this.empresa = empresa;
        this.fecha = fecha;
        this.status = status;

    }

    public String getFecha() {
        return fecha;
    }

    public String getStatus() {
        return status;
    }

    public String getNumero() {
        return numero;
    }

    public String getEmpresa() {
        return empresa;
    }
}
