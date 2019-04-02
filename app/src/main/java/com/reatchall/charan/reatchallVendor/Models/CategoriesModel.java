package com.reatchall.charan.reatchallVendor.Models;

/**
 * Created by NaNi on 01/03/18.
 */

public class CategoriesModel {

    private String name;
    private String id;

    public CategoriesModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CategoriesModel{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
