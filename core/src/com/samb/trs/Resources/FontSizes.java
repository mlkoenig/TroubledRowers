package com.samb.trs.Resources;

public enum FontSizes {
    F50(50), F100(100), F120(120), F150(150);

    private final int size;

    FontSizes(int size){
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
