package com.anton.dietpro.models;

import static java.lang.Math.round;

/**
 * Created by Anton Vasilev on 03.11.16.
 */

public class PFC {
    final double DJ = 4.1868;


    private double protein;///< Количество белка
    private double fat;///< Количество жиров
    private double carbohydrate;///< Количество углеводов

    /**
     * Конструктор класса БЖУ(Белки, Жиры, Углеводы)
     *
     */
    public PFC() {
    }

    /**
     * Конструктор с параметрами класса БЖУ(Белки, Жиры, Углеводы)
     *@param double protein
     *              Количество белка на 100 грамм
     * @param double fat
     *               Количество жира на 100 грамм
     *@param double carbogidrate
     *              Количество углеводов на 100 грамм
     *
     */
    public PFC(double protein, double fat, double carbogidrate) {
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbogidrate;
    }


    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public double getProtein() {

        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    /**
     * Расчет калорийности
     *
     * @return Количество калорий
     */
    public double getCalories(){
        return (double)round(( this.protein * 4 + this.fat * 10 + this.carbohydrate * 4 )*100)/100;
    }
    /**
     * Расчет энергетической ценности в Джоулях
     *
     * @return Количество Джоулей
     */
    public double getDjoyles()
    {
        return this.getCalories() * this.DJ;
    }

}
