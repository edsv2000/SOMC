package com.example.somc.data;

public class FormulasData {
    private String name;

    private String register;

    private String status;
    private String Ingredients;

    public FormulasData (String name, String Ingredients,String register, String status) {
        this.name = name;
        this.Ingredients = Ingredients;
        this.status = status;
        this.register = register;
    }

    public String getName() {
        return name;
    }

    public String getRegister() {
        return register;
    }

    public String getStatus() {
        return status;
    }

    public String getIngredients() {
        return Ingredients;
    }
}
