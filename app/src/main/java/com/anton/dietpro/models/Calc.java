package com.anton.dietpro.models;

/**
 * Created by Anton Vasilev on 02.11.16.
 * Родительский класс для расчетов, связанных с параметрами тела
 */
public abstract class Calc {

    private int age;///< Возраст человека
    private double weight; ///< Масса человека в килограммах
    private double height; ///< Рост в см
    public Calc() {
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
    public abstract double calc(); /// < метод с расчетной частью


}
