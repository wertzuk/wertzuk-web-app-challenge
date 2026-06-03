package com.example.demo;

public class Denomination {
    private final int valueInCents;
    private int count;
    private Integer difference;

    public Denomination(int value) {
        this.valueInCents = value;
        this.count = 0;
        this.difference = null;
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

    public Integer getDifference() {
        return difference;
    }

    public void setDifference(Integer difference) {
        this.difference = difference;
    }
}
