package net.tally.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.util.Identifier;

import java.util.Collection;

public class FunctionTags {
    public static void executeAll(MinecraftServer server, Collection<CommandFunction<ServerCommandSource>> functions, Identifier label) {
        server.getProfiler().push(label::toString);

        for (CommandFunction<ServerCommandSource> commandFunction : functions) {
            server.getCommandFunctionManager().execute(commandFunction, server.getCommandFunctionManager().getScheduledCommandSource());
        }

        server.getProfiler().pop();
    }

    public static void executeAllEntity(MinecraftServer server, Collection<CommandFunction<ServerCommandSource>> functions, Identifier label, Entity entity) {
        server.getProfiler().push(label::toString);

        for (CommandFunction<ServerCommandSource> commandFunction : functions) {
            server.getCommandFunctionManager().execute(commandFunction, server.getCommandFunctionManager().getScheduledCommandSource().withEntity(entity));
        }

        server.getProfiler().pop();
    }
}
