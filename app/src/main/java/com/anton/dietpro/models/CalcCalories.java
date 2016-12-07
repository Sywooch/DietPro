package com.anton.dietpro.models;

import android.util.Log;

import com.anton.dietpro.models.Calc;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import static java.lang.Math.round;

/**
 * Created by Anton Vasilev on 03.11.16.
 * Наследник от Calc
 * Класс CalcCalories учитывает параметры человека и на их основании высчитывает
 * необходимую норму БЖУ(Белков, Жиров, Углеводов)
 */
public class CalcCalories extends Calc {

    /**
     * Конструктор класса
     */
    public CalcCalories() {
    }

    /**
     * Конструктор класса c параметрами
     *
     * @param int age
     *            Возраст человека
     * @param double weight
     *               Масса человека в кг
     * @param double height
     *               Рост человека в см
     * @return Ничего
     */
    public CalcCalories(int age,double weight, double height) {
        this.setAge(age);
        this.setHeight(height);
        this.setWeight(weight);
    }

    /**
     * Переопределение метода из абстрактного класса
     * @return возвращает результат расчета по формуле Миффлина - Сан Жеора
     */
    @Override
    public double calc() {
        return this.calcMifflin();
    }

    /**
     * Производит расчет необходимого количества каллорий с учетом
     * массы, роста и возраста человека
     * Формула Миффлина — Сан Жеора для мужчин 10 * weight + 6.25 * height - 5 * age + 5
     * Формула Миффлина — Сан Жеора для женщин 10 * weight + 6.25 * height - 5 * age - 161
     * @return Количество калорий необходимых для чего-то в сутки
     */
    public double calcMifflin() {
        int age = this.getAge();
        double weight = this.getWeight();
        double height = this.getHeight();
        return this.roundTwoDecimals(10 * weight + 6.25 * height - 5 * age + 5);
    }

    /**
     * Производит расчет необходимого количества каллорий с учетом
     * массы, роста и возраста человека
     * Формула Харриса-Бенедикта для мужчин 66.5 + 13.75 * weight + 5.003 * height - 6.775 * age
     * Не забываем результат домножить на коэффициент физической активности
     * 1.2 - минимум или отсутствие
     * 1.375 - 3 р. в неделю
     * 1.4625 - 5 р. в неделю
     * 1.550 - интенсивно 5 р. в неделю
     * 1.6375 - каждый день
     * 1.725 - каждый день интенсивно или 2 р. в день
     * 1.9 - ежедневно + физическая работа
     * @return Количество калорий необходимых для чего-то в сутки
     */
    public double calcHarris(){
        int age = this.getAge();
        double weight = this.getWeight();
        double height = this.getHeight();
        return this.roundTwoDecimals(10 * weight + 6.25 * height - 5 * age + 5);
    }

    public double calcIMT(){
        double x = this.getWeight() / (Math.pow(this.getHeight()/100 , 2));
        Log.d("test_double", "x=" + x);
        x = this.roundTwoDecimals(x);
        return x;
    }

    public double calcMass(){
        return this.roundTwoDecimals(this.getWeight()*50);
    }

    public int calcIMTRes() {
        int res = 0; // 0 -ниже нормы, 1- соответствует, 2 - превышает
        int x = this.getAge();
        double cal = this.calcIMT();
        if (x<=24){
            if((cal>= 19) && (cal<=24)) {
                res = 1;
            }
            else if(cal>24){
                res = 2;
            }
        }
        else if(x<=34){
            if((cal>= 20) && (cal<=25)) {
                res = 1;
            }
            else if(cal>25){
                res = 2;
            }
        }
        else if(x<=44){

            if((cal>= 21) && (cal<=26)) {
                res = 1;
            }
            else if(cal>26){
                res = 2;
            }
        }
        else if(x<=54){

            if((cal>= 22) && (cal<=27)) {
                res = 1;
            }
            else if(cal>27){
                res = 2;
            }
        }
        else if(x<=64){

            if((cal>= 23) && (cal<=28)) {
                res = 1;
            }
            else if(cal>28){
                res = 2;
            }
        }
        else if(x>64){

            if((cal>= 24) && (cal<=29)) {
                res = 1;
            }
            else if(cal>29){
                res = 2;
            }
        }
        return res;
    }

    private double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);
        return Double.valueOf(twoDForm.format(d));
    }
}
