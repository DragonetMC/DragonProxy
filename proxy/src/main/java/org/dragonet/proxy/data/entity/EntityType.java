package org.dragonet.proxy.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityType {
    PLAYER(63,1.8, 1.6, 1.6200000047683716f);

    private int type;
    private double height;
    private double width;
    private float offset;

    EntityType(int type, double height, double width) {
        this.type = type;
        this.height = height;
        this.width = width;
    }
}
