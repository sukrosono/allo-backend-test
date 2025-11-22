package com.allo.sukrosono.intf;

public interface ResourceAcquire<T> {
    T fetch();
    String getType();
    default void initCache() {
    }
    T getResource();
}
