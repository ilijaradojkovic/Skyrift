package bees.io.Berzza.domain.enums;

public enum BetHighestType {

    BY_AMOUNT("cashOut"),
    BY_MULTIPLIER("multiplier");

    private final String type;

    BetHighestType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
