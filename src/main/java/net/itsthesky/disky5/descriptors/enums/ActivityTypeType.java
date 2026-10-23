package net.itsthesky.disky5.descriptors.enums;

import net.dv8tion.jda.api.entities.Activity;
import net.itsthesky.disky5.annotations.SkriptEnum;
import net.itsthesky.disky5.annotations.docs.Documentation;

@SkriptEnum(name = "activitytype", jda = Activity.ActivityType.class)
@Documentation(
    name = "Activity Type",
    description = "The kind of a bot activity/presence: playing, streaming, listening, watching, competing.",
    since = "5.0.0-a1")
public final class ActivityTypeType {
}
