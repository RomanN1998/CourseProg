package com.example.courseprog;

import java.io.Serializable;

public class Car implements Serializable {
    private String id, name, price, specification;
    private String urlImage = "";
    private boolean status;
    private String idPeople;

    Car() {}

    Car(String id, String name, String price, String specification, String urlImage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.specification = specification;
        this.urlImage = urlImage;
        this.status = false;
        this.idPeople = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getIdPeople() {
        return idPeople;
    }

    public void setIdPeople(String idPeople) {
        this.idPeople = idPeople;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }
}
