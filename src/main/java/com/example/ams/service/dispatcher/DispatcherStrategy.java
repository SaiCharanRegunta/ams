package com.example.ams.service.dispatcher;

public interface DispatcherStrategy {
    void triggerEvent(String message);
}
