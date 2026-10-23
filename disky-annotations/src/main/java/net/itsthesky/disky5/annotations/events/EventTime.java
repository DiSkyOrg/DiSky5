package net.itsthesky.disky5.annotations.events;

public enum EventTime {
    PAST(-1),
    PRESENT(0),
    FUTURE(1);

    private final int skriptTime;

    EventTime(int skriptTime) {
        this.skriptTime = skriptTime;
    }

    public int skriptTime() {
        return skriptTime;
    }
}
