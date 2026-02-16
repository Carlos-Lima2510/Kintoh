package com.kintoh.watcher;

public interface Monitor<T> {
    void onEvent(T resource, String eventType);
}
