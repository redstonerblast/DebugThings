package net.tally.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tally.DebugThings;
import net.tally.effects.DebugEffects;
import net.tally.helpers.FunctionTags;
import net.tally.helpers.IEntityDataSaver;
import net.tally.helpers.SoundSequenceHelper;
import net.tally.inflictions.InflictionHandler;
import net.tally.inflictions.Inflictions;

import java.util.Collection;

public class PlayerEvents {
    private static final Identifier RESPAWN_TAG_ID = new Identifier("debugthings", "respawn");
    private static final Identifier JOIN_TAG_ID = new Identifier("debugthings", "join");
    private static final Identifier COPY_PLAYER_ID = new Identifier("debugthings", "copy_player");
    private static final Identifier PLAYER_DIMENSION_SWITCH_ID = new Identifier("debugthings", "player_change_world");

    public static void initialize() {
        ServerPlayConnectionEvents.JOIN.register(PlayerEvents::join);
        PlayerBlockBreakEvents.BEFORE.register(PlayerEvents::blockBreakBefore);
        UseEntityCallback.EVENT.register(PlayerEvents::useEntity);
        UseBlockCallback.EVENT.register(PlayerEvents::useBlock);
        UseItemCallback.EVENT.register(PlayerEvents::useItem);
        ServerPlayerEvents.COPY_FROM.register(PlayerEvents::copyFrom);
        ServerPlayerEvents.AFTER_RESPAWN.register(PlayerEvents::respawnPlayer);
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(PlayerEvents::afterChangeWorld);
    }

    private static void afterChangeWorld(ServerPlayerEntity serverPlayerEntity, ServerWorld world, ServerWorld world1) {
        Collection<CommandFunction<ServerCommandSource>> collection = serverPlayerEntity.getServer().getCommandFunctionManager().getTag(PLAYER_DIMENSION_SWITCH_ID);
        FunctionTags.executeAllEntity(serverPlayerEntity.getServer(), collection, PLAYER_DIMENSION_SWITCH_ID, serverPlayerEntity);
    }

    public static void copyFrom(PlayerEntity oldPlayer, PlayerEntity newPlayer, Boolean alive) {
        NbtCompound nbt = ((IEntityDataSaver)newPlayer).debugged_things_1_20_4$getPersistentData();
        if (nbt.getString("name").isEmpty()) {
            nbt.putString("name", newPlayer.getName().getLiteralString());
        }
        if (newPlayer instanceof ServerPlayerEntity serverPlayerEntity) {
            Collection<CommandFunction<ServerCommandSource>> collection = serverPlayerEntity.getServer().getCommandFunctionManager().getTag(COPY_PLAYER_ID);
            FunctionTags.executeAllEntity(serverPlayerEntity.getServer(), collection, COPY_PLAYER_ID, serverPlayerEntity);
        }
    }

    public static void respawnPlayer(PlayerEntity oldPlayer, PlayerEntity newPlayer, Boolean alive) {
        NbtCompound nbt = ((IEntityDataSaver)newPlayer).debugged_things_1_20_4$getPersistentData();
        if (nbt.getString("name").isEmpty()) {
            nbt.putString("name", newPlayer.getName().getLiteralString());
        }
        if (newPlayer instanceof ServerPlayerEntity serverPlayerEntity) {
            Collection<CommandFunction<ServerCommandSource>> collection = serverPlayerEntity.getServer().getCommandFunctionManager().getTag(RESPAWN_TAG_ID);
            FunctionTags.executeAllEntity(serverPlayerEntity.getServer(), collection, RESPAWN_TAG_ID, serverPlayerEntity);
            InflictionHandler.sendClientPacket(serverPlayerEntity, new NbtCompound());
        }
    }

    public static TypedActionResult<ItemStack> useItem(PlayerEntity player, World world, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        if(player.hasStatusEffect(DebugEffects.TEMPORAL_FREEZE)) {
            return TypedActionResult.fail(itemStack);
        }

        return TypedActionResult.pass(itemStack);
    }

    public static ActionResult useEntity(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if(player.hasStatusEffect(DebugEffects.TEMPORAL_FREEZE)) {
            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    public static ActionResult useBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if(player.hasStatusEffect(DebugEffects.TEMPORAL_FREEZE) || InflictionHandler.hasInfliction(Inflictions.MAGENTA, player)) {
            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    public static boolean blockBreakBefore(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity be) {
        if (player.hasStatusEffect(DebugEffects.TEMPORAL_FREEZE) || InflictionHandler.hasInfliction(Inflictions.MAGENTA, player)) {
            return false;
        }
        if (!world.getWorldChunk(pos).hasAttached(DebugThings.PERSISTENT_TEMP_BLOCK_LOC)) {
            return true;
        };
        return !world.getWorldChunk(pos).getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_LOC).contains(pos);
    }

    public static void join(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {
        if (serverPlayNetworkHandler.player.hasAttached(DebugThings.SOUND_SEQUENCE)) {
            SoundSequenceHelper.play(serverPlayNetworkHandler.player, serverPlayNetworkHandler.player.getAttached(DebugThings.SOUND_SEQUENCE).get(0));
        }

        NbtCompound nbt = ((IEntityDataSaver)serverPlayNetworkHandler.player).debugged_things_1_20_4$getPersistentData();
        if (nbt.getString("name").isEmpty()) {
            nbt.putString("name", serverPlayNetworkHandler.player.getName().getLiteralString());
        }

        Collection<CommandFunction<ServerCommandSource>> collection = minecraftServer.getCommandFunctionManager().getTag(JOIN_TAG_ID);
        FunctionTags.executeAllEntity(minecraftServer, collection, JOIN_TAG_ID, serverPlayNetworkHandler.player);
        InflictionHandler.forceUpdateInflicts(serverPlayNetworkHandler.player, serverPlayNetworkHandler.player.getServerWorld());
    }
}
