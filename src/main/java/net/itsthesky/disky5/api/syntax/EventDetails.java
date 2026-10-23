package net.itsthesky.disky5.api.syntax;

import java.util.List;

public record EventDetails(boolean cancellable, List<String> intents, List<EventValueDetail> values,
                           List<EventExpressionDetail> expressions) {}
