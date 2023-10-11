package com.example.ams.model.alert;

import lombok.Getter;

@Getter
public class AlertConfig {

    AlertType type;
    long count;
    long windowSize;
}
