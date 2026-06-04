package com.example.demo;

public class Denomination {

    private final int valueInCents;
    private int count;
    private int difference;

    public Denomination(int value) {
        this.valueInCents = value;
        this.count = 0;
        this.difference = 0;
    }

    public int getValueInCents() {
        return valueInCents;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }
}
