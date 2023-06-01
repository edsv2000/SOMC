package com.example.somc.data;

public class OCData {
    private String numeroOC;

    private String fechaOC;

    private String status;

    public OCData(String numeroOC, String fechaOC, String status) {
        this.numeroOC = numeroOC;
        this.fechaOC = fechaOC;
        this.status = status;

    }

    public String getStatus() {
        return status;
    }

    public String getFechaOC() {
        return fechaOC;
    }

    public String getNumeroOC() {
        return numeroOC;
    }
}
