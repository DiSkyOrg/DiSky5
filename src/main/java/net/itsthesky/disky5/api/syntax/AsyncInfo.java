package net.itsthesky.disky5.api.syntax;

public record AsyncInfo(boolean awaitable, boolean restBacked, boolean retrieveOnly) {

    public static final AsyncInfo NONE = new AsyncInfo(false, false, false);

    public boolean isNone() {
        return !awaitable && !restBacked && !retrieveOnly;
    }
}
