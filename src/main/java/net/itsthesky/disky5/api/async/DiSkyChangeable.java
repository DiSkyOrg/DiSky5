package net.itsthesky.disky5.api.async;

// TODO: nothing reads this today. Meant to let a custom change effect tell a DiSky REST change apart
// from an ordinary Skript one, but that effect was never written - await carries the async context
// instead via a flag keyed on the event. Kept only because generated classes declare it.
public interface DiSkyChangeable {
}
