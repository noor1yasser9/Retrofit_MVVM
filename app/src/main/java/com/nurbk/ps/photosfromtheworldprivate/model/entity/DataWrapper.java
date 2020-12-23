package com.nurbk.ps.photosfromtheworldprivate.model.entity;

import java.util.Observable;

public class DataWrapper<T> extends Observable {

    private T result;

    public DataWrapper() {

    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
        setChanged();
        notifyObservers(result);
    }

}
