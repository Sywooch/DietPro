package com.anton.dietpro;

/**
 * Created by Anton Vasilev on 03.11.16.
 */

public class PFC {
    final double DJ = 4.1868;
    private double protein;///< Количество белка
    private double fat;///< Количество жиров
    private double carbogidrate;///< Количество углеводов

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
        this.carbogidrate = carbogidrate;
    }

    /**
     * Расчет калорийности
     *
     * @return Количество калорий
     */
    public double getCalories()
    {
        return ( this.protein * 4 + this.fat * 10 + this.carbogidrate * 4 );
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
