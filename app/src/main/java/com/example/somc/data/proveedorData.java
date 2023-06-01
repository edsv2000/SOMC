package com.example.somc.data;

public class proveedorData {
    private String numeroProveedor;

    private String nombreProveedor;

    private String telefonoProveedor;

    private String emailProveedor;

    public proveedorData(String numeroProveedor, String nombreProveedor, String telefonoProveedor, String emailProveedor) {
        this.numeroProveedor = numeroProveedor;
        this.nombreProveedor = nombreProveedor;
        this.telefonoProveedor = telefonoProveedor;
        this.emailProveedor = emailProveedor;

    }

    public String getEmailProveedor() {
        return emailProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public String getNumeroProveedor() {
        return numeroProveedor;
    }

    public String getTelefonoProveedor() {
        return telefonoProveedor;
    }
}
