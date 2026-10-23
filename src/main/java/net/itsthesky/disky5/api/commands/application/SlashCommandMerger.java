package net.itsthesky.disky5.api.commands.application;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SlashCommandMerger {

    private SlashCommandMerger() {}

    public static SlashCommandHolder merge(String root, List<SlashCommandModel> models) {
        SlashCommandModel rootDescriptor = null, single = null;
        boolean nested = false;
        for (SlashCommandModel model : models) {
            if (model.depth() == 1) {
                if (model.descriptorOnly()) rootDescriptor = model;
                else single = model;
            } else {
                nested = true;
            }
        }
        if (single != null && nested)
            throw new IllegalStateException("'" + root + "' is declared both as a slash command and as a root of subcommands");

        SlashCommandModel head = rootDescriptor != null ? rootDescriptor : single;
        SlashCommandData data = Commands.slash(root, head != null ? head.description() : "Commands for " + root);
        SlashCommandHolder holder = new SlashCommandHolder(data);
        holder.models().addAll(models);

        if (head != null) {
            if (!head.descriptionLocalizations().isEmpty()) data.setDescriptionLocalizations(head.descriptionLocalizations());
            head.applyTo(data);
        }
        if (single != null) {
            for (SlashArgument argument : single.arguments())
                data.addOptions(argument.toOptionData());
            return holder;
        }

        Map<String, SubcommandGroupData> groups = new LinkedHashMap<>();
        Map<String, SlashCommandModel> groupDescriptors = new LinkedHashMap<>();
        for (SlashCommandModel model : models)
            if (model.depth() == 2 && model.descriptorOnly())
                groupDescriptors.put(model.leaf(), model);
        for (SlashCommandModel model : models) {
            if (model.descriptorOnly()) continue;
            SubcommandData subcommand = new SubcommandData(model.leaf(), model.description());
            if (!model.descriptionLocalizations().isEmpty()) subcommand.setDescriptionLocalizations(model.descriptionLocalizations());
            if (!model.nameLocalizations().isEmpty()) subcommand.setNameLocalizations(model.nameLocalizations());
            for (SlashArgument argument : model.arguments())
                subcommand.addOptions(argument.toOptionData());
            if (model.depth() == 2) {
                data.addSubcommands(subcommand);
            } else {
                String groupName = model.path()[1];
                SubcommandGroupData group = groups.computeIfAbsent(groupName, name -> {
                    SlashCommandModel descriptor = groupDescriptors.get(name);
                    SubcommandGroupData created = new SubcommandGroupData(name,
                        descriptor != null ? descriptor.description() : "Commands for " + name);
                    if (descriptor != null) {
                        if (!descriptor.descriptionLocalizations().isEmpty()) created.setDescriptionLocalizations(descriptor.descriptionLocalizations());
                        if (!descriptor.nameLocalizations().isEmpty()) created.setNameLocalizations(descriptor.nameLocalizations());
                    }
                    data.addSubcommandGroups(created);
                    return created;
                });
                group.addSubcommands(subcommand);
            }
        }
        if (rootDescriptor == null)
            applyGroupSettings(data, models);
        return holder;
    }

    private static void applyGroupSettings(CommandData data, List<SlashCommandModel> models) {
        Set<Permission> intersection = null;
        boolean anyDisabled = false, anyNsfw = false;
        SlashCommandModel first = null;
        for (SlashCommandModel model : models) {
            if (model.descriptorOnly()) continue;
            if (first == null) first = model;
            anyDisabled |= model.disabled();
            anyNsfw |= model.nsfw();
            if (!model.enabledFor().isEmpty()) {
                if (intersection == null) intersection = EnumSet.copyOf(model.enabledFor());
                else intersection.retainAll(model.enabledFor());
            }
        }
        if (intersection != null && !intersection.isEmpty())
            data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(intersection));
        else if (anyDisabled)
            data.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
        data.setNSFW(anyNsfw);
        if (first != null) {
            if (first.contexts() != null && !first.contexts().isEmpty()) data.setContexts(first.contexts());
            if (first.integrationTypes() != null && !first.integrationTypes().isEmpty()) data.setIntegrationTypes(first.integrationTypes());
        }
    }

    public static @Nullable ApplicationCommandHolder toHolder(ContextCommandModel model) {
        CommandData data = Commands.context(model.type(), model.name());
        model.applyTo(data);
        return ApplicationCommandHolders.from(data);
    }
}
