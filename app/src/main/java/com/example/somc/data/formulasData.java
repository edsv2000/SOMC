package com.example.somc.data;

public class formulasData {

    private String id;
    private String name;

    private String register;

    private String status;
    private String Ingredients;

    private Integer imgFormula;

    public formulasData(String id, String name, String Ingredients, String register, String status, Integer imgFormula) {
        this.id = id;
        this.name = name;
        this.Ingredients = Ingredients;
        this.status = status;
        this.register = register;
        this.imgFormula = imgFormula;
    }

    public String getId() {return id;}

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

    public Integer getImgFormula() {
        return imgFormula;
    }



}
