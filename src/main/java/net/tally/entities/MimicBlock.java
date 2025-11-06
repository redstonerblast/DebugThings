package net.tally.entities;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.tally.DebugThings;
import net.tally.helpers.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MimicBlock extends Entity implements Ownable {
    private BlockState block = Blocks.SAND.getDefaultState();
    private Boolean moveOthers = true;
    private Boolean physics = false;
    private Boolean canPlace = false;
    private Boolean landDestroy = false;
    private Boolean mimicCollide = false;
    private Boolean rumble = false;
    private Float damage = 0.0f;
    @Nullable
    private UUID ownerUuid;
    @Nullable
    private Entity owner;

    private static final String DATA_KEY = "data";
    private NbtCompound data = new NbtCompound();

    @Nullable
    public NbtCompound blockEntityData;
    protected static final TrackedData<BlockPos> BLOCK_POS = DataTracker.registerData(MimicBlock.class, TrackedDataHandlerRegistry.BLOCK_POS);

    public MimicBlock(EntityType<? extends MimicBlock> type, World world) {
        super(type, world);
    }

    public boolean collidesWith(Entity other) {
        return other.isCollidable() || (other.getType() == DebugThings.MIMIC_BLOCK_ENTITY_TYPE && this.mimicCollide);
    }


    public boolean isCollidable() {
        return true;
    }

    public boolean getRumble() {
        return this.rumble;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float dmg) {
        this.damage = dmg;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.getWorld().isClient || this.isRemoved()) {
            return false;
        }
        if (source.getAttacker() == null || source.getAttacker() instanceof PlayerEntity) {
            return false;
        }
        this.emitGameEvent(GameEvent.ENTITY_DAMAGE, source.getAttacker());
        return true;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(BLOCK_POS, BlockPos.ORIGIN);
    }

    public void tick() {
        super.tick();
        if (this.moveOthers && this.getVelocity().length() != 0) {
            List<Entity> entities = this.getWorld().getOtherEntities(this, this.calculateBoundingBox().offset(0,0.4,0));
            entities.addAll(getEntityCollision(this.getPos(), this.getPos().add(this.getVelocity())));
            for (Entity element : entities) {
                NbtCompound nbt = ((IEntityDataSaver)element).debugged_things_1_20_4$getPersistentData();
                if (this.rumble && element instanceof MimicBlock otherMimic && otherMimic.getRumble() && this.getVelocity().length() > 0.1 && this.getVelocity().length() > otherMimic.getVelocity().length()) {
                    otherMimic.addVelocity(this.getVelocity().multiply(0.75));
                    this.setVelocity(this.getVelocity().multiply(0.15));
                }
                if (element instanceof LivingEntity) {
                    if (!this.isOwner(element) && this.damage > 0 && this.getVelocity().length() > 0.1) {
                        if (this.getOwner() != null) {
                            element.damage(new DamageSource(this.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypes.FALLING_BLOCK), this, this.getOwner()), (Float) this.damage);
                        } else {
                            element.damage(new DamageSource(this.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypes.FALLING_BLOCK), this), (Float) this.damage);
                        }
                        if (this.rumble) {
                            this.remove(RemovalReason.KILLED);
                        }
                    }
                }
                if (element instanceof LivingEntity && !nbt.getBoolean("mimicPushed")) {
                    nbt.putBoolean("mimicPushed", true);
                    element.move(MovementType.SHULKER, this.getVelocity());
                    element.velocityModified = true;
                }
            }
        }
        this.move(MovementType.SELF, this.getVelocity());
        if (!this.hasNoGravity()) {
            this.setVelocity(this.getVelocity().add(0.0D, -0.04D, 0.0D));
        }


        BlockPos blockPos = this.getBlockPos();
        BlockState blockState = this.getWorld().getBlockState(blockPos);
        if (!this.getWorld().isClient && !blockState.isOf(Blocks.MOVING_PISTON) && (this.canPlace || this.landDestroy) && Math.abs(this.getVelocity().length()) <= 0.08) {
            boolean bl = this.getBlockState().getBlock() instanceof ConcretePowderBlock;
            boolean bl2 = bl && this.getWorld().getFluidState(blockPos).isIn(FluidTags.WATER);
            boolean bl3 = blockState.canReplace(new AutomaticItemPlacementContext(this.getWorld(), blockPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
            boolean bl4 = FallingBlock.canFallThrough(this.getWorld().getBlockState(blockPos.down())) && (!bl || !bl2);
            boolean bl5 = this.getBlockState().canPlaceAt(this.getWorld(), blockPos) && !bl4;
            Block block = this.getBlockState().getBlock();
            if (bl3 && bl5 || this.isOnGround()) {
                if (this.getBlockState().contains(Properties.WATERLOGGED) && this.getWorld().getFluidState(blockPos).getFluid() == Fluids.WATER) {
                    this.block = this.getBlockState().with(Properties.WATERLOGGED, true);
                }

                if (!this.landDestroy && bl3 && bl5 && this.getWorld().setBlockState(blockPos, this.getBlockState(), 3)) {
                    ((ServerWorld)this.getWorld()).getChunkManager().threadedAnvilChunkStorage.sendToOtherNearbyPlayers(this, new BlockUpdateS2CPacket(blockPos, this.getWorld().getBlockState(blockPos)));
                    this.discard();

                    if (this.blockEntityData != null && this.getBlockState().hasBlockEntity()) {
                        BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);
                        if (blockEntity != null) {
                            NbtCompound nbtCompound = blockEntity.createNbt();

                            for (String string : this.blockEntityData.getKeys()) {
                                nbtCompound.put(string, this.blockEntityData.get(string).copy());
                            }

                            try {
                                blockEntity.readNbt(nbtCompound);
                            } catch (Exception var15) {
                                DebugThings.LOGGER.error("Failed to load block entity from falling block", var15);
                            }

                            blockEntity.markDirty();
                        }
                    }
                } else if (this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS) && !(Boolean) this.landDestroy) {
                    this.discard();
                    this.dropItem(block);
                } else {
                    this.discard();
                }
            }
        }

        Vec3d vel = this.getVelocity();
        if (this.physics) {
            vel = vel.multiply(0.95);
        }
        this.setVelocity(Math.abs(vel.x) >= 0.01 ? vel.x: 0, Math.abs(vel.y) >= 0.01 ? vel.y: 0, Math.abs(vel.z) >= 0.01 ? vel.z: 0);
    }

    protected List<Entity> getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return getEntityCollision(this.getWorld(), this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), 0.3f);
    }

    public static List<Entity> getEntityCollision(World world, Entity entity, Vec3d min, Vec3d max, Box box, float margin) {
        double d = 1.7976931348623157E308D;
        List<Entity> entities = new ArrayList<>();

        for (Entity entity3 : world.getOtherEntities(entity, box)) {
            Box box2 = entity3.getBoundingBox().expand((double) margin);
            Optional<Vec3d> optional = box2.raycast(min, max);
            //DebugThings.LOGGER.info("MAIN");
            if (optional.isPresent()) {
                //DebugThings.LOGGER.info("PRESENT");
                double e = min.squaredDistanceTo((Vec3d) optional.get());
                if (e < d) {
                    //DebugThings.LOGGER.info("ADD");
                    entities.add(entity3);
                    d = e;
                }
            }
        }

        return entities;
    }

    @Override
    public boolean doesNotCollide(double offsetX, double offsetY, double offsetZ) {
        return true;
    }

    @Override
    public void pushAwayFrom(Entity entity) {
    }

    public BlockPos getMimicBlockPos() {
        return this.dataTracker.get(BLOCK_POS);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.noClip = true;
        if (nbt.contains("Physics", 99)) {
            this.physics = nbt.getBoolean("Physics");
            this.noClip = !this.physics;
            this.setNoGravity(!this.physics);
        }
        this.block = NbtHelper.toBlockState(this.getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("BlockState"));
        if (this.block.isAir()) {
            this.block = Blocks.SAND.getDefaultState();
        }
        if (nbt.contains("MoveOthers", 99)) {
            this.moveOthers = nbt.getBoolean("MoveOthers");
        }
        if (nbt.contains("CanPlace", 99)) {
            this.canPlace = nbt.getBoolean("CanPlace");
        }
        if (nbt.contains("Damage", 99)) {
            this.damage = nbt.getFloat("Damage");
        }
        if (nbt.contains("LandDestroy", 99)) {
            this.landDestroy = nbt.getBoolean("LandDestroy");
        }
        if (nbt.contains("MimicCollide", 99)) {
            this.mimicCollide = nbt.getBoolean("MimicCollide");
        }
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
            this.owner = null;
        }
        if(nbt.contains("BlockEntity")) {
            this.blockEntityData = nbt.getCompound("BlockEntity");
        }
        if (nbt.contains("Rumble", 99)) {
            this.rumble = nbt.getBoolean("Rumble");
        }

        this.data = nbt.getCompound("data");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put("BlockState", NbtHelper.fromBlockState(this.block));
        nbt.putBoolean("MoveOthers", this.moveOthers);
        nbt.putBoolean("Physics", this.physics);
        nbt.putBoolean("CanPlace", this.canPlace);
        nbt.putFloat("Damage", this.damage);
        nbt.putBoolean("LandDestroy", this.landDestroy);
        nbt.putBoolean("MimicCollide", this.mimicCollide);
        nbt.putBoolean("Rumble", this.rumble);
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
        if (this.blockEntityData != null) {
            nbt.put("Owner", this.blockEntityData);
        }

        nbt.put("data", this.data.copy());
    }

    protected boolean isOwner(Entity entity) {
        return entity.getUuid().equals(this.ownerUuid);
    }

    public BlockState getBlockState() {
        return this.block;
    }

    protected Text getDefaultName() {
        return Text.translatable("entity.debugthings.mimic_block_type", (this.block).getBlock().getName());
    }

    public boolean doesRenderOnFire() {
        return false;
    }

    public void populateCrashReport(CrashReportSection section) {
        super.populateCrashReport(section);
        section.add("Immitating BlockState", (this.block).toString());
    }

    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, Block.getRawIdFromState(this.getBlockState()));
    }

    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.block = Block.getStateFromRawId(packet.getEntityData());
    }

    @Override
    @Nullable
    public Entity getOwner() {
        World world;
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        }
        if (this.ownerUuid != null && (world = this.getWorld()) instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld)world;
            this.owner = serverWorld.getEntity(this.ownerUuid);
            return this.owner;
        }
        return null;
    }

    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUuid = entity.getUuid();
            this.owner = entity;
        }
    }
}
