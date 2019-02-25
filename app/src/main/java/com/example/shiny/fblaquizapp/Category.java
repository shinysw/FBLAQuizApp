package com.example.shiny.fblaquizapp;

public class Category {

    //Categories for the program.
    public static final int Competitive_Events = 1;
    public static final int Business_Skills = 2;
    public static final int National_Officers = 3;
    public static final int Parliamentary_Procedure = 4;
    public static final int FBLA_History = 5;


    //Private instance variables
    private int id;
    private String name;

    //Empty Constructor
    public Category() {

    }

    //Constructor for just the name
    public Category(String name) {
        this.name = name;
    }


    //Gets and sets the ID and Name
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
