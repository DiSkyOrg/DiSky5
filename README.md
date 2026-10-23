# DiSky

DiSky is a [Skript](https://github.com/SkriptLang/Skript) addon for writing Discord bots in Skript, on your Minecraft server. It is built on [JDA](https://github.com/discord-jda/JDA): anything a JDA bot can do, DiSky aims to expose as Skript syntax.

This branch is **DiSky 5**, a rewrite of DiSky 4. It is in public beta: most of DiSky 4 is covered, the syntax is close but not identical, and V4 scripts need a few changes (see [Coming from DiSky 4](#coming-from-disky-4)).

- Syntax reference: [atlas.disky.me](https://atlas.disky.me)
- Download: [Modrinth](https://modrinth.com/plugin/disky)
- Bugs and requests: [GitHub issues](https://github.com/DiSkyOrg/DiSky5/issues)

## Requirements

- A Paper server, 1.21.5 or newer (or a Paper fork such as Purpur)
- Java 21 or newer
- Skript 2.16.1 or newer, with `use type properties: true` in its `config.sk` (the default)
- A bot token from the [Discord Developer Portal](https://discord.com/developers/applications)

## Installation

1. Put `DiSky-<version>.jar` in your server's `plugins/` folder, next to Skript.
2. Restart the server.

DiSky has no config file. At each boot it writes `plugins/DiSky/disky-syntax.json`, a list of every syntax it registered; you don't need to touch it.

## A first bot

Save this as `plugins/Skript/scripts/bot.sk`, put your token in it, and run `/sk reload bot`:

```applescript
options:
    token: "YOUR-BOT-TOKEN"

on load:
    set {_bot} to a new discord bot
    set token of {_bot} to {@token}
    login {_bot} as "my-bot"

on unload:
    shutdown the bot named "my-bot"

# Slash commands are declared in the script, then sent to Discord explicitly.
on guild ready:
    update the discord commands (all structured application commands) in event-guild

slash command ping:
    description: Check that the bot is alive
    trigger:
        reply with hidden "Pong!"

slash command ban <target: member> [<reason: string>]:
    description: Ban a member
    enabled for: ban members
    trigger:
        try:
            ban {_target} because of {_reason}
        catch {_errors::*} and stop:
            reply with hidden "Could not ban %{_target}%: %error meaning of {_errors::1}%"
        reply with hidden "%{_target}% was banned."

on message received seen by "my-bot":
    discord content of event-message is "!about"
    set {_e} to a new discord embed
    set title of {_e} to name of event-guild
    set description of {_e} to "%member count of event-guild% members"
    set color of {_e} to orange
    reply with {_e}
```

`hidden` makes a reply visible only to the user who ran the command. If the ban fails (missing permission, member above the bot), the `catch` block gets a readable error instead of a stack trace in the console.

## What you can do

- **Bots**: several bots at once, intents, cache settings, sharding, presence.
- **Messages**: send, reply, edit, delete, pin, react, read history; embeds, file uploads and downloads, polls.
- **Commands**: slash commands with typed options, autocomplete, subcommands, cooldowns and translations; user and message (right-click) commands; classic text commands with a prefix.
- **Components**: buttons, select menus, modals, and the newer layout components (containers, sections, galleries).
- **Server management**: members, roles and their order, channels and threads, permissions and channel overrides, emotes and stickers, invites, scheduled events, welcome screen, AutoMod rules, webhooks.
- **Moderation**: ban, kick, timeout, purge, audit log reasons.
- **Events**: about 130 Discord events, from messages and reactions to voice, roles and AutoMod.

The [Atlas](https://atlas.disky.me) lists every syntax with its patterns and examples.

## Three things that differ from plain Skript

**Talking to Discord takes time.** Anything that sends something to Discord (posting a message, banning a member) runs off the server thread, so your server never lags waiting for Discord. Values DiSky has in memory are read directly; values it has to ask Discord for are read with `await`, which waits for the answer:

```applescript
await set {_bans::*} to bans of event-guild
```

Reading such a value without `await` is an error when the script loads, not a silent `<none>`.

**Builders.** To create something on Discord (a role, a channel, a message with several parts), you fill a builder first, then send it:

```applescript
set {_b} to a new role builder named "Helpers" in event-guild
set color of {_b} to light blue
await set {_role} to the build result of {_b}
```

**Errors.** A failed Discord action prints a readable message to the console. Put it in a `try` block to handle it yourself, as in the `ban` command above, or listen to `on discord error` to catch the ones no `try` handled.

## Coming from DiSky 4

DiSky 5 replaces DiSky 4: same plugin name, same Modrinth page, and addons that depend on DiSky keep working by name. Scripts need a few changes:

- `retrieve X and store it in {_v}` becomes `await set {_v} to X`.
- `create X and store it in {_v}` becomes a builder, then `await set {_v} to the build result of {_b}`.
- Slash command structures no longer register themselves: send them from `on guild ready` (see the example above).
- Some properties take a `discord` prefix where Skript already uses the word: `discord content`, `discord author`, `discord permissions`...
- `define bot` still works, but `a new discord bot` + `login` is the recommended way.

## Building from source

```bash
./gradlew build
```

Needs JDK 21. The plugin is `build/libs/DiSky-<version>.jar` (the `-thin` jar next to it does not contain JDA and will not load).

## License

[Apache License 2.0](LICENSE)
