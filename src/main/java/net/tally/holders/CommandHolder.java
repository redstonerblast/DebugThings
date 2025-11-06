package net.tally.holders;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.command.BlockDataObject;
import net.minecraft.command.DataCommandObject;
import net.minecraft.command.EntityDataObject;
import net.minecraft.command.StorageDataObject;
import net.minecraft.command.argument.*;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.DataCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.tally.DebugThings;
import net.tally.components.DebugThingsComponents;
import net.tally.helpers.CustomRegistryHandler;
import net.tally.helpers.IEntityDataSaver;
import net.tally.helpers.SoundSequenceHelper;
import net.tally.helpers.TempBlockAttachment;
import net.tally.networking.ClientMessages;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandHolder {
    public static final Logger LOGGER = LoggerFactory.getLogger(DebugThings.MOD_ID);

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("sound_sequence")
                .requires(source -> source.hasPermissionLevel(2))
                .then(literal("add")
                        .then(argument("sound", IdentifierArgumentType.identifier()).suggests(SuggestionProviders.AVAILABLE_SOUNDS)
                                .then(argument("volume", FloatArgumentType.floatArg(0.0F))
                                        .then(argument("pitch", FloatArgumentType.floatArg(0.0F,2.0F))
                                                .then(argument("players", EntityArgumentType.players())
                                                        .executes(context ->
                                                                soundSequenceAdd(IdentifierArgumentType.getIdentifier(context, "sound"), FloatArgumentType.getFloat(context, "volume"), FloatArgumentType.getFloat(context, "pitch"), EntityArgumentType.getPlayers(context, "players"), context)
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .then(literal("set")
                        .then(argument("players", EntityArgumentType.players())
                                .executes(context ->
                                        soundSequenceReset(EntityArgumentType.getPlayers(context, "players"), context)
                                )
                        )
                )
        ));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("motion")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("type", StringArgumentType.string())
                        .then(argument("entity", EntityArgumentType.entity())
                                .then(argument("xmot", DoubleArgumentType.doubleArg())
                                        .then(argument("ymot", DoubleArgumentType.doubleArg())
                                                .then(argument("zmot", DoubleArgumentType.doubleArg())
                                                        .executes(context ->
                                                                createMotion(StringArgumentType.getString(context, "type"), EntityArgumentType.getEntity(context, "entity"), new Vec3d(DoubleArgumentType.getDouble(context, "xmot"), DoubleArgumentType.getDouble(context, "ymot"), DoubleArgumentType.getDouble(context, "zmot")), context)
                                                        )
                                                )
                                        )
                                )
                                .then(literal("toward")
                                        .then(argument("location", Vec3ArgumentType.vec3())
                                                .then(argument("power", DoubleArgumentType.doubleArg())
                                                        .then(literal("relative")
                                                                .executes(context ->
                                                                        createMotionLocRel(StringArgumentType.getString(context, "type"), EntityArgumentType.getEntity(context, "entity"), Vec3ArgumentType.getVec3(context, "location"), DoubleArgumentType.getDouble(context, "power"), context)
                                                                )
                                                        )
                                                        .executes(context ->
                                                                createMotionLoc(StringArgumentType.getString(context, "type"), EntityArgumentType.getEntity(context, "entity"), Vec3ArgumentType.getVec3(context, "location"), DoubleArgumentType.getDouble(context, "power"), context)
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("distance")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("location-1", Vec3ArgumentType.vec3())
                        .then(argument("location-2", Vec3ArgumentType.vec3())
                                .executes(context ->
                                        getdist(Vec3ArgumentType.getVec3(context, "location-1"), Vec3ArgumentType.getVec3(context, "location-2"), context)
                                )
                        )
                )
                .then(argument("entity", EntityArgumentType.entity())
                        .then(argument("location-2", Vec3ArgumentType.vec3())
                                .executes(context ->
                                        getdist(EntityArgumentType.getEntity(context, "entity"), Vec3ArgumentType.getVec3(context, "location-2"), context)
                                )
                        )
                        .then(argument("entity-2", EntityArgumentType.entity())
                                .executes(context ->
                                        getdist(EntityArgumentType.getEntity(context, "entity"), EntityArgumentType.getEntity(context, "entity-2"), context)
                                )
                        )
                )
        ));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("target")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("actor[s]", EntityArgumentType.entities())
                        .then(argument("target", EntityArgumentType.entity())
                                .executes(context ->
                                        settarg(EntityArgumentType.getEntities(context, "actor[s]"), EntityArgumentType.getEntity(context, "target"), context)
                                )
                        )
                        .then(literal("clear")
                                .executes(context ->
                                        cleartarg(EntityArgumentType.getEntities(context, "actor[s]"), context)
                                )
                        )
                )
        ));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("tempblock")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("location", Vec3ArgumentType.vec3())
                        .then(argument("time", IntegerArgumentType.integer())
                                .then(argument("block", BlockStateArgumentType.blockState(registryAccess))
                                        .executes(context ->
                                                createTempBlock(Vec3ArgumentType.getVec3(context, "location"), IntegerArgumentType.getInteger(context, "time"), BlockStateArgumentType.getBlockState(context, "block").getBlockState(), context)
                                        )
                                )
                        )
                )
        ));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("squad")
                .requires(source -> source.hasPermissionLevel(2))
                .then(literal("clear")
                        .then(
                                argument("entity", EntityArgumentType.entity())
                                        .executes(context -> clearSquad(EntityArgumentType.getEntity(context, "entity"), context))
                        )
                )
                .then(literal("add")
                        .then(
                                argument("entity", EntityArgumentType.entity())
                                        .then(argument("name", StringArgumentType.word())
                                                .executes(context -> setSquad(StringArgumentType.getString(context, "name"), EntityArgumentType.getEntity(context, "entity"), context))
                                        )
                        )
                )
        ));

        InflictionCommand.register();

        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> DataExHolder.register(commandDispatcher)));
        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> FunctionExHolder.register(commandDispatcher)));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("debugthings")
                        .then(literal("mimicblock")
                                .then(argument("block", BlockPosArgumentType.blockPos())
                                        .then(argument("summon", Vec3ArgumentType.vec3())
                                                .then(argument("nbt", NbtCompoundArgumentType.nbtCompound())
                                                        .executes(commandContext -> newMimic(BlockPosArgumentType.getBlockPos(commandContext, "block"), Vec3ArgumentType.getVec3(commandContext, "summon"), NbtCompoundArgumentType.getNbtCompound(commandContext, "nbt"), commandContext))
                                                )
                                        )
                                )
                        )
                        .then(literal("remblock")
                                .then(argument("block", BlockPosArgumentType.blockPos())
                                        .executes(commandContext -> {
                                            BlockPos blockPos = BlockPosArgumentType.getBlockPos(commandContext, "block");
                                            commandContext.getSource().getWorld().removeBlockEntity(blockPos);
                                            return commandContext.getSource().getWorld().removeBlock(blockPos, false) ? 1 : 0;
                                        })
                                )
                        )
                        .then(literal("radian")
                                .then(argument("x", IntegerArgumentType.integer())
                                        .then(argument("y", IntegerArgumentType.integer())
                                            .executes(commandContext -> radian(IntegerArgumentType.getInteger(commandContext, "x"), IntegerArgumentType.getInteger(commandContext, "y"), commandContext))
                                        )
                                )
                        )
                        .then(literal("raycast")
                                .then(argument("location", Vec3ArgumentType.vec3())
                                        .then(argument("rotation", RotationArgumentType.rotation())
                                                .then(argument("step", FloatArgumentType.floatArg())
                                                        .then(argument("distance", FloatArgumentType.floatArg())
                                                            .then(argument("command", StringArgumentType.greedyString())
                                                                    .executes(commandContext ->
                                                                            raycast(
                                                                                    Vec3ArgumentType.getPosArgument(commandContext, "location"),
                                                                                    RotationArgumentType.getRotation(commandContext, "rotation"),
                                                                                    FloatArgumentType.getFloat(commandContext, "step"),
                                                                                    FloatArgumentType.getFloat(commandContext, "distance"),
                                                                                    StringArgumentType.getString(commandContext, "command"),
                                                                                    commandContext
                                                                                    ))
                                                            )
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(literal("tick")
                                .then(argument("location", BlockPosArgumentType.blockPos())
                                        .then(argument("ticks", IntegerArgumentType.integer())
                                                .executes(commandContext -> {
                                                    BlockPos bPos = BlockPosArgumentType.getBlockPos(commandContext, "location");
                                                    int speed = IntegerArgumentType.getInteger(commandContext, "ticks");
                                                    ServerWorld world = commandContext.getSource().getWorld();
                                                    BlockState bState = world.getBlockState(bPos);
                                                    @Nullable BlockEntity blockEntity = world.getBlockEntity(bPos);
                                                    tickBlockEntity(world, bPos, bState, blockEntity, speed, commandContext.getSource().getWorld().random);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(literal("health")
                                .then(argument("entity", EntityArgumentType.entity())
                                        .then(argument("num", FloatArgumentType.floatArg())
                                                .executes(commandContext -> {
                                                    Entity entity = EntityArgumentType.getEntity(commandContext, "entity");
                                                    float health = FloatArgumentType.getFloat(commandContext, "num");
                                                    if (entity instanceof LivingEntity livingEntity) {
                                                        livingEntity.setHealth(health);
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(literal("immunity")
                                .then(literal("has")
                                        .then(argument("entity", EntityArgumentType.entity())
                                                .then(argument("immunity", StringArgumentType.word())
                                                        .executes(commandContext -> {
                                                            Entity entity = EntityArgumentType.getEntity(commandContext, "entity");
                                                            String string = StringArgumentType.getString(commandContext, "immunity");
                                                            if (!(entity instanceof LivingEntity livingEntity)) {
                                                                return 0;
                                                            }
                                                            return DebugThingsComponents.hasImmunity(livingEntity, string) ? 1 : 0;
                                                        })
                                                )
                                        )
                                )
                                .then(literal("add")
                                        .then(argument("entities", EntityArgumentType.entities())
                                                .then(argument("immunity", StringArgumentType.word())
                                                        .executes(commandContext -> {
                                                            Collection<? extends Entity> entities = EntityArgumentType.getEntities(commandContext, "entities");
                                                            String string = StringArgumentType.getString(commandContext, "immunity");
                                                            for (Entity entity : entities) {
                                                                if (!(entity instanceof LivingEntity livingEntity)) {
                                                                    continue;
                                                                }
                                                                DebugThingsComponents.addImmunity(livingEntity, string);
                                                            }
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                                .then(literal("remove")
                                        .then(argument("entities", EntityArgumentType.entities())
                                                .then(argument("immunity", StringArgumentType.word())
                                                        .executes(commandContext -> {
                                                            Collection<? extends Entity> entities = EntityArgumentType.getEntities(commandContext, "entities");
                                                            String string = StringArgumentType.getString(commandContext, "immunity");
                                                            for (Entity entity : entities) {
                                                                if (!(entity instanceof LivingEntity livingEntity)) {
                                                                    continue;
                                                                }
                                                                DebugThingsComponents.removeImmunity(livingEntity, string);
                                                            }
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                ));


    }

    private static  <T extends BlockEntity> void tickBlockEntity(ServerWorld world, BlockPos bPos, BlockState bState, T blockEntity, int speed, Random random) {
        @Nullable BlockEntityTicker<T> ticker = null;
        if (blockEntity != null) {
            ticker = bState.getBlockEntityTicker(world, (BlockEntityType<T>)blockEntity.getType());
        }
        if (!world.isClient()) {
            for (int i = 0; i < speed; i++) {
                if (blockEntity != null && ticker != null) {
                    ticker.tick(world, bPos, bState, blockEntity);
                }
                bState.randomTick(world, bPos, random);
            }
        }
    }

    private static int newMimic(BlockPos block, Vec3d summon, NbtCompound nbt, CommandContext<ServerCommandSource> context) {
        ServerWorld world = context.getSource().getWorld();
        NbtCompound nbtCompound = nbt.copy();
        nbtCompound.put("BlockState", NbtHelper.fromBlockState(world.getBlockState(block)));
        BlockEntity blockEntity = world.getBlockEntity(block);
        if (blockEntity != null) {
            nbtCompound.put("BlockEntity", blockEntity.createNbt());
        }

        BlockPos blockPos = BlockPos.ofFloored(summon);
        if (World.isValid(blockPos)) {
            nbtCompound.putString("id", DebugThings.MIMIC_BLOCK_ENTITY_TYPE.getRegistryEntry().registryKey().getValue().toString());
            Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, entityx -> {
                entityx.refreshPositionAndAngles(summon.x, summon.y, summon.z, entityx.getYaw(), entityx.getPitch());
                return entityx;
            });
            if (entity != null) {

                world.spawnNewEntityAndPassengers(entity);
            }
        }

        return 1;
    }

    private static int setSquad(String type, Entity entity, CommandContext<ServerCommandSource> context) {
        NbtCompound nbt = ((IEntityDataSaver)entity).debugged_things_1_20_4$getPersistentData();
        nbt.putString("squad", type);
        return 1;
    }

    private static int clearSquad(Entity entity, CommandContext<ServerCommandSource> context) {
        NbtCompound nbt = ((IEntityDataSaver)entity).debugged_things_1_20_4$getPersistentData();
        nbt.remove("squad");
        return 1;
    }
    
    private static int soundSequenceAdd(Identifier sound, Float volume, Float pitch, Collection<ServerPlayerEntity> players, CommandContext<ServerCommandSource> context) {
        long l = context.getSource().getWorld().getRandom().nextLong();
        for (ServerPlayerEntity player : players) {
            if (!player.hasAttached(DebugThings.SOUND_SEQUENCE)) {
                player.setAttached(DebugThings.SOUND_SEQUENCE, List.of("temp_value"));
            } else {
                SoundSequenceHelper.addIndexAttachment(player, DebugThings.SOUND_SEQUENCE, "temp_value");
            }
            Integer index = player.getAttached(DebugThings.SOUND_SEQUENCE).size() - 1;
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeIdentifier(sound).writeFloat(volume).writeFloat(pitch).writeBoolean(index==0).writeInt(index);
            ServerPlayNetworking.send(player, ClientMessages.SOUND_SEQ_C, buf);
        }
        return 1;
    }

    private static void executeGetPName(ServerCommandSource source, DataCommandObject object, ServerPlayerEntity player) throws CommandSyntaxException {
        try {
            source.sendFeedback(() -> object.feedbackQuery(NbtString.of(player.getName().getString())), false);
        }
        catch (Exception e) {
            LOGGER.info("Error - {}", e.getMessage());
        }
    }

    private static int soundSequenceReset(Collection<ServerPlayerEntity> players, CommandContext<ServerCommandSource> context) {
        for (ServerPlayerEntity player : players) {
            player.removeAttached(DebugThings.SOUND_SEQUENCE);
        }
        return 1;
    }

    private static int createMotion(String type, Entity entity, Vec3d vec3d, CommandContext<ServerCommandSource> context) {
        if(entity != null) {
            if(Objects.equals(type, "set") || Objects.equals(type, "add")) {
                if(Objects.equals(type, "set")) {
                    entity.setVelocity(vec3d);
                    entity.velocityModified = Boolean.TRUE;
                    return 1;
                } else {
                    entity.addVelocity(vec3d);
                    entity.velocityModified = Boolean.TRUE;
                    return 1;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    private static int createTempBlock(Vec3d vec3d, Integer time, BlockState block, CommandContext<ServerCommandSource> context) {
        ServerWorld world = context.getSource().getWorld();
        BlockPos bPos = BlockPos.ofFloored(vec3d);
        BlockState saveblock = world.getBlockState(bPos);
        BlockEntity saventity = saveblock.hasBlockEntity() ? world.getBlockEntity(bPos) : null;
        TempBlockAttachment.createTemp(world, bPos, saveblock, saventity, block, time);
        return 1;
    }

    private static int createMotionLocRel(String type, Entity entity, Vec3d vec3d, Double power, CommandContext<ServerCommandSource> context) {
        if(entity != null) {
            if(Objects.equals(type, "set") || Objects.equals(type, "add")) {
                Vec3d vel = vec3d.subtract(entity.getPos()).multiply(power);
                if(Objects.equals(type, "set")) {
                    entity.setVelocity(vel);
                    entity.velocityModified = Boolean.TRUE;
                    return 1;
                } else {
                    entity.addVelocity(vel);
                    entity.velocityModified = Boolean.TRUE;
                    return 1;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    private static int createMotionLoc(String type, Entity entity, Vec3d vec3d, Double power, CommandContext<ServerCommandSource> context) {
        if(entity != null) {
            if(Objects.equals(type, "set") || Objects.equals(type, "add")) {
                Vec3d vel = vec3d.subtract(entity.getPos()).normalize().multiply(power);
                if(Objects.equals(type, "set")) {
                    entity.setVelocity(vel);
                    entity.velocityModified = Boolean.TRUE;
                    return 1;
                } else {
                    entity.addVelocity(vel);
                    entity.velocityModified = Boolean.TRUE;
                    return 1;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    private static int getdist(Vec3d loca, Vec3d locb, CommandContext<ServerCommandSource> context) {
        return (int) Math.round(loca.distanceTo(locb) * 100);
    }

    private static int getdist(Entity entitya, Vec3d locb, CommandContext<ServerCommandSource> context) {
        return (int) Math.round(entitya.getPos().distanceTo(locb) * 100);
    }

    private static int getdist(Entity entitya, Entity entityb, CommandContext<ServerCommandSource> context) {
        return (int) Math.round(entitya.getPos().distanceTo(entityb.getPos()) * 100);
    }

    private static int radian(int x, int y, CommandContext<ServerCommandSource> context) {
        if (x == 0 && y == 0) {
            return -1;
        }
        var angle = Math.atan2(y, x);
        var degrees = 180*angle/Math.PI;
        return (int) ((360+Math.round(degrees))%360);
    }

    private static int raycast(PosArgument location, PosArgument rotation, float step, float distance, String function, CommandContext<ServerCommandSource> context) {
        ServerCommandSource orig = context.getSource();
        Vec3d loc = location.toAbsolutePos(orig);
        Vec3d add = Vec3d.fromPolar(rotation.toAbsoluteRotation(orig)).multiply(step);
        ServerCommandSource stepSource;
        for (float i = 0; i <= distance; i += step) {
            stepSource = orig.withPosition(loc).withSilent();
            orig.getWorld().getServer().getCommandManager().executeWithPrefix(stepSource, function);
            loc = loc.add(add);
        }
        return 1;
    }

    private  static int settarg(java.util.Collection<? extends net.minecraft.entity.Entity> entities, Entity entity, CommandContext<ServerCommandSource> context) {
        if (entity instanceof LivingEntity) {
            for (Entity actor : entities) {
                if (actor instanceof MobEntity) {
                    ((MobEntity) actor).setTarget((LivingEntity) entity);
                }
            }
            return 1;
        } else {
            context.getSource().sendError(Text.of("Target Entity must be a Living Entity"));
            return 0;
        }
    }

    private  static int cleartarg(java.util.Collection<? extends net.minecraft.entity.Entity> entities, CommandContext<ServerCommandSource> context) {
        for (Entity actor : entities) {
            if (actor instanceof MobEntity) {
                ((MobEntity) actor).setTarget(null);
            }
        }
        return 1;
    }
}
