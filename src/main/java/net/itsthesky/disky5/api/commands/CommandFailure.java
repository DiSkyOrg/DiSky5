package net.itsthesky.disky5.api.commands;

public enum CommandFailure {
    INVALID_ARGUMENTS("invalid arguments"),
    WRONG_CHANNEL("wrong channel"),
    MISSING_ROLE("missing role"),
    MISSING_PERMISSION("missing permission"),
    COOLDOWN("cooldown"),
    WRONG_BOT("wrong bot");

    private final String display;

    CommandFailure(String display) {
        this.display = display;
    }

    public String display() {
        return display;
    }
}
