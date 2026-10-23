package net.itsthesky.disky5.api.commands.application;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.ColorRGB;
import ch.njol.skript.util.SkriptColor;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class SlashArgumentTypes {

    private static final Map<Class<?>, SlashArgumentType<?>> ENTRIES = new ConcurrentHashMap<>();
    private static volatile boolean enumsRegistered;

    static {
        register(String.class, SlashArgumentType.of(OptionType.STRING, String.class, OptionMapping::getAsString));
        register(Long.class, SlashArgumentType.of(OptionType.INTEGER, Long.class, OptionMapping::getAsLong));
        register(Integer.class, SlashArgumentType.of(OptionType.INTEGER, Integer.class, OptionMapping::getAsInt));
        register(Number.class, SlashArgumentType.of(OptionType.NUMBER, Number.class, OptionMapping::getAsDouble));
        register(Double.class, SlashArgumentType.of(OptionType.NUMBER, Double.class, OptionMapping::getAsDouble));
        register(Boolean.class, SlashArgumentType.of(OptionType.BOOLEAN, Boolean.class, OptionMapping::getAsBoolean));
        register(User.class, SlashArgumentType.of(OptionType.USER, User.class, OptionMapping::getAsUser));
        register(Member.class, SlashArgumentType.of(OptionType.USER, Member.class, OptionMapping::getAsMember));
        register(Role.class, SlashArgumentType.of(OptionType.ROLE, Role.class, OptionMapping::getAsRole));
        register(IMentionable.class, SlashArgumentType.of(OptionType.MENTIONABLE, IMentionable.class, OptionMapping::getAsMentionable));
        register(Message.Attachment.class, SlashArgumentType.of(OptionType.ATTACHMENT, Message.Attachment.class, OptionMapping::getAsAttachment));
        register(Channel.class, channel(Channel.class));
        register(Color.class, SlashArgumentType.of(OptionType.STRING, Color.class, m -> parseColor(m.getAsString()))
            .withCompleter(SlashArgumentTypes::completeColor));
        register(Player.class, SlashArgumentType.of(OptionType.STRING, Player.class, m -> Bukkit.getPlayerExact(m.getAsString()))
            .withCompleter(SlashArgumentTypes::completePlayer));
        register(OfflinePlayer.class, SlashArgumentType.of(OptionType.STRING, OfflinePlayer.class, m -> {
            Player online = Bukkit.getPlayerExact(m.getAsString());
            return online != null ? online : Bukkit.getOfflinePlayerIfCached(m.getAsString());
        }).withCompleter(SlashArgumentTypes::completePlayer));
    }

    private SlashArgumentTypes() {}

    public static <T> void register(Class<T> type, SlashArgumentType<T> spec) {
        ENTRIES.put(type, spec);
    }

    public static @Nullable SlashArgumentType<?> of(ClassInfo<?> info) {
        Class<?> c = info.getC();
        if (c == null) return null;
        return of(c);
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable SlashArgumentType<? extends T> of(Class<T> c) {
        SlashArgumentType<?> exact = ENTRIES.get(c);
        if (exact != null) return (SlashArgumentType<? extends T>) exact;
        if (Channel.class.isAssignableFrom(c))
            return (SlashArgumentType<? extends T>) channel((Class<? extends Channel>) c);
        if (c.isEnum())
            return (SlashArgumentType<? extends T>) registerEnum((Class<? extends Enum>) c);
        for (Map.Entry<Class<?>, SlashArgumentType<?>> entry : ENTRIES.entrySet())
            if (entry.getKey().isAssignableFrom(c))
                return narrowed((SlashArgumentType<? super T>) entry.getValue(), c);
        for (Map.Entry<Class<?>, SlashArgumentType<?>> entry : ENTRIES.entrySet())
            if (c.isAssignableFrom(entry.getKey()))
                return (SlashArgumentType<? extends T>) entry.getValue();
        return null;
    }

    private static <T> SlashArgumentType<T> narrowed(SlashArgumentType<? super T> broad, Class<T> c) {
        return new SlashArgumentType<>(broad.optionType(), c,
            m -> { Object v = broad.convert(m); return c.isInstance(v) ? c.cast(v) : null; },
            broad.choices(), broad.completer(), broad.channelTypes());
    }

    private static <C extends Channel> SlashArgumentType<C> channel(Class<C> c) {
        Set<ChannelType> types = EnumSet.noneOf(ChannelType.class);
        for (ChannelType type : ChannelType.values())
            if (type != ChannelType.UNKNOWN && type.isGuild() && c.isAssignableFrom(type.getInterface()))
                types.add(type);
        return SlashArgumentType.of(OptionType.CHANNEL, c, m -> {
            Channel channel = m.getAsChannel();
            return c.isInstance(channel) ? c.cast(channel) : null;
        }).withChannelTypes(c == Channel.class ? Set.of() : types);
    }

    public static void registerSkriptEnums() {
        if (enumsRegistered) return;
        enumsRegistered = true;
        for (ClassInfo<?> info : Classes.getClassInfos()) {
            Class<?> c = info.getC();
            if (c != null && c.isEnum() && c.getEnumConstants().length > 0 && !ENTRIES.containsKey(c))
                registerEnum((Class<? extends Enum>) c);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <E extends Enum> SlashArgumentType<E> registerEnum(Class<E> c) {
        SlashArgumentType<?> existing = ENTRIES.get(c);
        if (existing != null) return (SlashArgumentType<E>) existing;
        E[] constants = c.getEnumConstants();
        Function<OptionMapping, E> converter = m -> parseEnum(c, m.getAsString());
        SlashArgumentType<E> spec = SlashArgumentType.of(OptionType.STRING, c, converter);
        if (constants.length <= OptionData.MAX_CHOICES) {
            List<Command.Choice> choices = new ArrayList<>();
            for (E constant : constants)
                choices.add(new Command.Choice(beautify(constant.name()), constant.name()));
            spec = spec.withChoices(() -> choices);
        } else {
            spec = spec.withCompleter(input -> {
                String prefix = input.toLowerCase(Locale.ROOT).replace('_', ' ');
                List<Command.Choice> choices = new ArrayList<>();
                for (E constant : constants) {
                    String label = beautify(constant.name());
                    if (label.toLowerCase(Locale.ROOT).startsWith(prefix))
                        choices.add(new Command.Choice(label, constant.name()));
                    if (choices.size() >= OptionData.MAX_CHOICES) break;
                }
                return choices;
            });
        }
        ENTRIES.put(c, spec);
        return spec;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <E extends Enum> @Nullable E parseEnum(Class<E> c, String raw) {
        String wanted = raw.trim();
        for (E constant : c.getEnumConstants())
            if (constant.name().equalsIgnoreCase(wanted) || beautify(constant.name()).equalsIgnoreCase(wanted))
                return constant;
        Object parsed = Classes.parse(wanted, c, ParseContext.DEFAULT);
        return c.isInstance(parsed) ? (E) parsed : null;
    }

    public static String beautify(String constant) {
        String lower = constant.replace('_', ' ').toLowerCase(Locale.ROOT);
        return lower.isEmpty() ? lower : Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    private static @Nullable Color parseColor(String raw) {
        String text = raw.trim();
        SkriptColor named = SkriptColor.fromName(text.toLowerCase(Locale.ROOT));
        if (named != null) return named;
        try {
            if (text.startsWith("#")) return ColorRGB.fromHexString(text.substring(1));
            return ColorRGB.fromString(text);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private static List<Command.Choice> completeColor(String input) {
        String prefix = input.toLowerCase(Locale.ROOT);
        List<Command.Choice> choices = new ArrayList<>();
        for (SkriptColor color : SkriptColor.values()) {
            String name = color.getName();
            if (name.startsWith(prefix))
                choices.add(new Command.Choice(name, name));
        }
        if (choices.isEmpty() && !input.isBlank())
            choices.add(new Command.Choice(input, input));
        return choices.size() > OptionData.MAX_CHOICES ? choices.subList(0, OptionData.MAX_CHOICES) : choices;
    }

    private static List<Command.Choice> completePlayer(String input) {
        String prefix = input.toLowerCase(Locale.ROOT);
        List<Command.Choice> choices = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase(Locale.ROOT).startsWith(prefix))
                choices.add(new Command.Choice(player.getName(), player.getName()));
            if (choices.size() >= OptionData.MAX_CHOICES) break;
        }
        return choices;
    }
}
