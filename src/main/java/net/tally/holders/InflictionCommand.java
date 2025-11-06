package net.tally.holders;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.DataCommandObject;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.DataCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.tally.helpers.CustomRegistryHandler;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionHandler;
import net.tally.inflictions.InflictionInstance;
import net.tally.inflictions.Inflictions;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class InflictionCommand {
    private static final SimpleCommandExceptionType GIVE_FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.infliction.give.failed"));

    public static void register() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("infliction")
                .requires(source -> source.hasPermissionLevel(2))
                .then(literal("give")
                        .then(
                                argument("targets", EntityArgumentType.entities())
                                        .then(
                                                argument("infliction", RegistryEntryArgumentType.registryEntry(registryAccess, CustomRegistryHandler.INFLICTION))
                                                        .then(
                                                                argument("ticks", IntegerArgumentType.integer())
                                                                        .then(
                                                                                argument("stacks", IntegerArgumentType.integer())
                                                                                        .then(
                                                                                                argument("name", StringArgumentType.word())
                                                                                                        .then(
                                                                                                                argument("variant", IntegerArgumentType.integer(0, 255))
                                                                                                                        .then(
                                                                                                                                argument("application duration", StringArgumentType.word())
                                                                                                                                        .suggests(new ApplicationSuggestion())
                                                                                                                                        .then(
                                                                                                                                                argument("application stacks", StringArgumentType.word())
                                                                                                                                                        .suggests(new ApplicationSuggestion())
                                                                                                                                                        .then(argument("source", EntityArgumentType.entity())
                                                                                                                                                                .executes(context -> inflictionGiveApp(
                                                                                                                                                                        context.getSource(),
                                                                                                                                                                        EntityArgumentType.getEntities(context, "targets"),
                                                                                                                                                                        RegistryEntryArgumentType.getRegistryEntry(context, "infliction", CustomRegistryHandler.INFLICTION),
                                                                                                                                                                        IntegerArgumentType.getInteger(context, "ticks"),
                                                                                                                                                                        IntegerArgumentType.getInteger(context, "stacks"),
                                                                                                                                                                        StringArgumentType.getString(context, "name"),
                                                                                                                                                                        IntegerArgumentType.getInteger(context, "variant"),
                                                                                                                                                                        StringArgumentType.getString(context, "application duration"),
                                                                                                                                                                        StringArgumentType.getString(context, "application stacks"),
                                                                                                                                                                        EntityArgumentType.getEntity(context, "source")
                                                                                                                                                                ))
                                                                                                                                                        )
                                                                                                                                                        .executes(context -> inflictionGiveApp(
                                                                                                                                                                context.getSource(),
                                                                                                                                                                EntityArgumentType.getEntities(context, "targets"),
                                                                                                                                                                RegistryEntryArgumentType.getRegistryEntry(context, "infliction", CustomRegistryHandler.INFLICTION),
                                                                                                                                                                IntegerArgumentType.getInteger(context, "ticks"),
                                                                                                                                                                IntegerArgumentType.getInteger(context, "stacks"),
                                                                                                                                                                StringArgumentType.getString(context, "name"),
                                                                                                                                                                IntegerArgumentType.getInteger(context, "variant"),
                                                                                                                                                                StringArgumentType.getString(context, "application duration"),
                                                                                                                                                                StringArgumentType.getString(context, "application stacks"),
                                                                                                                                                                null
                                                                                                                                                        ))
                                                                                                                                        )
                                                                                                                        )
                                                                                                                        .then(literal("add")
                                                                                                                                .then(argument("source", EntityArgumentType.entity())
                                                                                                                                        .executes(context -> inflictionGive(
                                                                                                                                                context.getSource(),
                                                                                                                                                EntityArgumentType.getEntities(context, "targets"),
                                                                                                                                                RegistryEntryArgumentType.getRegistryEntry(context, "infliction", CustomRegistryHandler.INFLICTION),
                                                                                                                                                IntegerArgumentType.getInteger(context, "ticks"),
                                                                                                                                                IntegerArgumentType.getInteger(context, "stacks"),
                                                                                                                                                StringArgumentType.getString(context, "name"),
                                                                                                                                                IntegerArgumentType.getInteger(context, "variant"),
                                                                                                                                                EntityArgumentType.getEntity(context, "source")
                                                                                                                                        ))
                                                                                                                                )
                                                                                                                                .executes(context -> inflictionGive(
                                                                                                                                        context.getSource(),
                                                                                                                                        EntityArgumentType.getEntities(context, "targets"),
                                                                                                                                        RegistryEntryArgumentType.getRegistryEntry(context, "infliction", CustomRegistryHandler.INFLICTION),
                                                                                                                                        IntegerArgumentType.getInteger(context, "ticks"),
                                                                                                                                        IntegerArgumentType.getInteger(context, "stacks"),
                                                                                                                                        StringArgumentType.getString(context, "name"),
                                                                                                                                        IntegerArgumentType.getInteger(context, "variant"),
                                                                                                                                        null
                                                                                                                                ))
                                                                                                                        )
                                                                                                        )
                                                                                        )
                                                                        )
                                                        )
                                        )
                        )
                )
                .then(literal("get")
                        .then(argument("target", EntityArgumentType.entity())
                                .executes(context -> inflictionGet(
                                        context.getSource(),
                                        EntityArgumentType.getEntity(context, "target")
                                ))
                                .then(argument("infliction", RegistryEntryArgumentType.registryEntry(registryAccess, CustomRegistryHandler.INFLICTION))
                                        .executes(context -> {
                                            Entity target = EntityArgumentType.getEntity(context, "target");
                                            Infliction infliction = RegistryEntryArgumentType.getRegistryEntry(context, "infliction", CustomRegistryHandler.INFLICTION).value();
                                            if (!(target instanceof LivingEntity livingEntity)) {
                                                return 0;
                                            }
                                            return InflictionHandler.getInflictions(infliction, livingEntity);
                                        })
                                )
                        )
                )
                .then(literal("has")
                        .then(argument("target", EntityArgumentType.entity())
                                .then(argument("infliction", RegistryEntryArgumentType.registryEntry(registryAccess, CustomRegistryHandler.INFLICTION))
                                        .executes( context -> {
                                            Entity target = EntityArgumentType.getEntity(context, "target");
                                            Infliction infliction = RegistryEntryArgumentType.getRegistryEntry(context, "infliction", CustomRegistryHandler.INFLICTION).value();
                                            if (target instanceof LivingEntity livingEntity) {
                                                return InflictionHandler.hasInfliction(infliction, livingEntity) ? 1 : 0;
                                            } else {
                                                return 0;
                                            }
                                        })
                                )
                        )
                )
                .then(literal("clear")
                        .then(argument("targets", EntityArgumentType.entities())
                                .executes( context -> {
                                    Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
                                    for (Entity entity : targets) {
                                        if (entity instanceof LivingEntity livingEntity) {
                                            InflictionHandler.clearInflictions(livingEntity);
                                        }
                                    }
                                    return targets.size();
                                })
                                .then(argument("stacks", IntegerArgumentType.integer())
                                        .executes( context -> {
                                            Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
                                            int stacks = IntegerArgumentType.getInteger(context, "stacks");
                                            for (Entity entity : targets) {
                                                if (entity instanceof LivingEntity livingEntity) {
                                                    InflictionHandler.clearInflictions(livingEntity, stacks);
                                                }
                                            }
                                            return targets.size();
                                        })
                                )
                                .then(argument("infliction", RegistryEntryArgumentType.registryEntry(registryAccess, CustomRegistryHandler.INFLICTION))
                                        .executes( context -> {
                                            Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
                                            Infliction infliction = RegistryEntryArgumentType.getRegistryEntry(context, "infliction", CustomRegistryHandler.INFLICTION).value();
                                            for (Entity entity : targets) {
                                                if (entity instanceof LivingEntity livingEntity) {
                                                    InflictionHandler.clearInfliction(livingEntity, infliction);
                                                }
                                            }
                                            return targets.size();
                                        })
                                        .then(argument("stacks", IntegerArgumentType.integer())
                                                .executes( context -> {
                                                    Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
                                                    Infliction infliction = RegistryEntryArgumentType.getRegistryEntry(context, "infliction", CustomRegistryHandler.INFLICTION).value();
                                                    int stacks = IntegerArgumentType.getInteger(context, "stacks");
                                                    for (Entity entity : targets) {
                                                        if (entity instanceof LivingEntity livingEntity) {
                                                            InflictionHandler.clearInflictions(livingEntity, infliction, stacks);
                                                        }
                                                    }
                                                    return targets.size();
                                                })
                                        )
                                )
                        )
                )
        ));
    }

    public static class ApplicationSuggestion implements SuggestionProvider<ServerCommandSource> {
        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            builder.suggest("replace").suggest("highest").suggest("combine").suggest("lowest");
            return builder.buildFuture();
        }
    }

    private static int inflictionGet(ServerCommandSource source, Entity entity) {

        if (!(entity instanceof LivingEntity) && source.getWorld().isClient()) {
            return 0;
        }

        NbtCompound compound = InflictionHandler.returnInflicts((LivingEntity) entity);

        source.sendFeedback(() -> Text.translatable("commands.infliction.get", entity.getName(), compound), false);
        return 1;
    }

    private static int inflictionGive(ServerCommandSource source, Collection<? extends Entity> targets, RegistryEntry.Reference<Infliction> infliction, int ticks, int stacks, String name, int variant, @Nullable Entity sourceE) throws CommandSyntaxException {
        Infliction infliction2 = infliction.value();
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity livingEntity) {
                InflictionInstance inflictionInstance = new InflictionInstance(infliction2, ticks, stacks, name, variant, sourceE == null ? null : sourceE.getId());
                if (InflictionHandler.addInfliction(livingEntity, inflictionInstance, source.getWorld())) {
                    i++;
                }
            }
        }

        if (i == 0) {
            throw GIVE_FAILED_EXCEPTION.create();
        } else {
            if (targets.size() == 1) {
                source.sendFeedback(
                        () -> Text.translatable("commands.infliction.give.success.single", stacks, infliction2.getName(), ((Entity) targets.iterator().next()).getDisplayName(), ticks / 20),
                        true
                );
            } else {
                source.sendFeedback(() -> Text.translatable("commands.infliction.give.success.multiple", infliction2.getName(), targets.size(), ticks / 20), true);
            }

            return i;
        }
    }

    private static int inflictionGiveApp(ServerCommandSource source, Collection<? extends Entity> targets, RegistryEntry.Reference<Infliction> infliction, int ticks, int stacks, String name, int variant, String applicationdur, String applicationstack, @Nullable Entity sourceE) throws CommandSyntaxException {
        Infliction infliction2 = infliction.value();
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity livingEntity) {
                InflictionInstance inflictionInstance = new InflictionInstance(infliction2, ticks, stacks, name, variant, sourceE == null ? null : sourceE.getId());
                if (InflictionHandler.addInfliction(livingEntity, inflictionInstance, applicationdur, applicationstack, source.getWorld())) {
                    i++;
                }
            }
        }

        if (i == 0) {
            throw GIVE_FAILED_EXCEPTION.create();
        } else {
            if (targets.size() == 1) {
                source.sendFeedback(
                        () -> Text.translatable("commands.infliction.give.success.single", stacks, infliction2.getName(), ((Entity) targets.iterator().next()).getDisplayName(), ticks / 20),
                        true
                );
            } else {
                source.sendFeedback(() -> Text.translatable("commands.infliction.give.success.multiple", infliction2.getName(), targets.size(), ticks / 20), true);
            }

            return i;
        }
    }
}
