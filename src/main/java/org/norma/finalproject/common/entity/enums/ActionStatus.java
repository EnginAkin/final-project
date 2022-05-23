package org.norma.finalproject.common.entity.enums;

import lombok.Getter;

@Getter
public enum ActionStatus {

    INCOMING(+1), OUTGOING(-1);

    private final int value;


    ActionStatus(int value) {
        this.value = value;
    }
}
