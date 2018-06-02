package com.github.byskyxie.eluyouni;

import java.io.Serializable;

public class Illness implements Serializable{
    private String name;

    public Illness(){}
    public Illness(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
