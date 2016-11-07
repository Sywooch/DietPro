package com.anton.dietpro.models;

import org.w3c.dom.Text;

/**
 * Created by admin on 02.11.16.
 */

public class Diet {
    public Diet(int id, String name, int length, String description) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    private int length;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public Diet() {
    }

    public Diet(String name, int length, String description) {
        this.name = name;
        this.length = length;
        this.description = description;
    }

}
