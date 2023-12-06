package com.samb.trs.Resources;

public enum FontSizes {
    F50(15), F100(20), F120(30), F150(45);

    private final int size;

    FontSizes(int size){
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
