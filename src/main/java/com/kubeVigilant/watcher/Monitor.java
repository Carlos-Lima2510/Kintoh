package com.kubeVigilant.watcher;

public interface Monitor<T> {
    void onEvent(T resource, String eventType);
}
