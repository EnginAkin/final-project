package org.norma.finalproject.account.entity.enums;

public enum Maturity {
    THIRTY_DAY(30),
    SIXTY_DAY(60),
    NINETY_DAY(90),
    ONE_HUNDRED_EIGHTY_DAY(180);

    private final int value;

    Maturity(int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }

}
