package com.anton.dietpro.models;

/**
 * Created by admin on 01.12.16.
 */

//Результат работы Яндекс Спеллера
//класс для хранения данных после сериализации из Json
public class SpellResult {
    private Integer code;
    private Integer pos;
    private Integer row;
    private Integer col;
    private Integer len;
    private String word;
    private String[] s;

    public SpellResult() {
    }

    public SpellResult(Integer code, Integer pos, Integer row, Integer col, Integer len, String word, String[] s) {
        this.code = code;
        this.pos = pos;
        this.row = row;
        this.col = col;
        this.len = len;
        this.word = word;
        this.s = s;
    }

    public Integer getCode() {
        return code;
    }

    public Integer getPos() {
        return pos;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getCol() {
        return col;
    }

    public Integer getLen() {
        return len;
    }

    public String getWord() {
        return word;
    }

    public String[] getS() {
        return s;
    }
}
