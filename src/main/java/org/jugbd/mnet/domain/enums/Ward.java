package org.jugbd.mnet.domain.enums;

/**
 * Created by rokonoid on 12/24/14.
 */
public enum Ward {
    ONE_O_NINE("109"),
    ONE_ONE_O("110"),
    TWO_ONE_SEVEN("217"),
    THREE_ONE_O("310"),
    CABIN("Cabin"),
    OTHER("Other");

    private String label;

    Ward(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
