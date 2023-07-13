package com.example.somc.data;

public class inventarioData {


    private String id;

    private String nombre;

    private String medida;

    private String cantidad;

    private String precio_unitario;

    public inventarioData(String id, String nombre, String medida, String cantidad, String precio_unitario) {
        this.id = id;
        this.nombre = nombre;
        this.medida = medida;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(String precio_unitario) {
        this.precio_unitario = precio_unitario;
    }
}
