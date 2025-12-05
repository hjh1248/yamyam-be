package com.ssafy.yamyam_coach.domain.food;

import lombok.Getter;

@Getter
public enum BaseUnit {

    g("gram"), ml("milliliter");

    private final String description;

    BaseUnit(String description) {
        this.description = description;
    }
}
