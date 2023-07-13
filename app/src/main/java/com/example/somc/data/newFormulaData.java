package com.example.somc.data;

public class newFormulaData {
    private String name;

    private String id;

    private String cantidad;

    public newFormulaData(String name, String id, String cantidad) {
        this.name = name;
        this.id = id;
        this.cantidad = cantidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
