package com.anton.dietpro;

/**
 * Created by Anton Vasilev on 02.11.16.
 */

public class Product {

    private PFC pfc; /// < Содержит БЖУ
    private String name; /// < Название продукта
    private float weight; /// < масса продукта в граммах



    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public float getWeight() {
        return this.weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public PFC getPfc() {
        return pfc;
    }

    public void setPfc(PFC pfc) {
        this.pfc = pfc;
    }

    /**
     * Расчет калорийности продукта
     *
     * @return Количество калорий в указанной массе продукта
     */
    public double getCalories()
    {
        return this.weight * pfc.getCalories();
    }
    /**
     * Расчет калорийности продукта
     *
     * @return Количество калорий в указанной массе продукта
     */
    public double getCalories(float weight)
    {
        this.setWeight(weight);
        return this.weight * pfc.getCalories();
    }



}


