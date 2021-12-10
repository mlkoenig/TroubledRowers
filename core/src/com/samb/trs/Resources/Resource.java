package com.samb.trs.Resources;

public interface Resource<T> {
    String getIdentifier();
    Class<T> getType();
}