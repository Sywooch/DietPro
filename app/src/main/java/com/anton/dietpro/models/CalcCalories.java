package com.anton.dietpro.models;

import com.anton.dietpro.models.Calc;

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
        return 10 * weight + 6.25 * height - 5 * age + 5;
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
        return 10 * weight + 6.25 * height - 5 * age + 5;
    }

}
