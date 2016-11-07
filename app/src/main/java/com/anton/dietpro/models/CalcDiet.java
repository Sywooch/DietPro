package com.anton.dietpro.models;

/**
 * Created by Anton Vasilev on 03.11.16.
 * Покачто пустой класс для отображения наследования
 */

public class CalcDiet extends CalcCalories {

    public CalcDiet() {
    }

    public CalcDiet(int age, double weight, double height) {
        super(age, weight, height);
    }

    /**
     * Пустой метод, в котором, возможно,
     * будут какие-то расчеты
     * @return Возможно, диету
     */
    public void calcMe(){
        //some code
    }
}
