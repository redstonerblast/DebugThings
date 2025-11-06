package net.tally.loot.modifiers;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.data.dev.NbtProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.function.*;
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider;
import net.minecraft.loot.provider.nbt.LootNbtProvider;
import net.minecraft.loot.provider.nbt.LootNbtProviderTypes;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.StringIdentifiable;
import net.tally.DebugThings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class ModifyNBTFunction extends ConditionalLootFunction {
    public static final Codec<ModifyNBTFunction> CODEC = RecordCodecBuilder.create((instance) -> {
        return addConditionsField(instance).and(instance.group(StringNbtReader.STRINGIFIED_CODEC.fieldOf("tag").forGetter((function) -> function.nbt),
                ModifyNBTFunction.Path.CODEC.fieldOf("target").forGetter(ModifyNBTFunction::parsedTargetPath),
                ModifyNBTFunction.Operator.CODEC.fieldOf("op").forGetter(ModifyNBTFunction::operator)
        )).apply(instance, ModifyNBTFunction::new);
    });
    private final NbtCompound nbt;
    private final Operator operator;
    private final Path parsedTargetPath;
    public static final Logger LOGGER = LoggerFactory.getLogger(DebugThings.MOD_ID);

    public static final LootFunctionType TYPE = new LootFunctionType(CODEC);

    public ModifyNBTFunction.Path parsedTargetPath() {
        return this.parsedTargetPath;
    }
    public ModifyNBTFunction.Operator operator() {
        return this.operator;
    }

    public ModifyNBTFunction(List<LootCondition> conditions, NbtCompound nbt, ModifyNBTFunction.Path parsedTargetPath, ModifyNBTFunction.Operator operator) {
        super(conditions);
        this.nbt = nbt;
        this.parsedTargetPath = parsedTargetPath;
        this.operator = operator;
    }

    public LootFunctionType getType() {
        return LootFunctionTypes.SET_NBT;
    }

    public ItemStack process(ItemStack stack, LootContext context) {
        try {
            LOGGER.info("process");
            this.operator.merge(stack.getOrCreateNbt(), this.parsedTargetPath.path(), this.nbt);
        } catch (CommandSyntaxException e) {
            LOGGER.info("fail");
            e.printStackTrace();
        }
        return stack;
    }

    public enum Operator implements StringIdentifiable {
        REPLACE("replace") {
            public void merge(NbtElement itemNbt, NbtPathArgumentType.NbtPath targetPath, NbtElement nbt) throws CommandSyntaxException {
                targetPath.put(itemNbt, nbt);
                LOGGER.info("tP - {} | iN - {} | n - {}", targetPath, itemNbt, nbt);
            }
        },
        APPEND("append") {
            public void merge(NbtElement itemNbt, NbtPathArgumentType.NbtPath targetPath, NbtElement nbt) throws CommandSyntaxException {
                List<NbtElement> list = targetPath.getOrInit(itemNbt, NbtList::new);
                list.forEach((foundNbt) -> {
                    if (foundNbt instanceof NbtList) {
                        ((NbtList) foundNbt).add(nbt);
                    }

                });
            }
        },
        MERGE("merge") {
            public void merge(NbtElement itemNbt, NbtPathArgumentType.NbtPath targetPath, NbtElement nbt) throws CommandSyntaxException {
                LOGGER.info("i- {} | p - {} | n - {}", itemNbt, targetPath, nbt);
                List<NbtElement> list = targetPath.getOrInit(itemNbt, NbtCompound::new);
                LOGGER.info("l - {}", list);
                list.forEach((foundNbt) -> {
                    LOGGER.info("fN T - {}", foundNbt);
                    if (foundNbt instanceof NbtCompound) {
                        LOGGER.info("cmpnd - a");
                        if (nbt instanceof NbtCompound) {
                            LOGGER.info("cmpnd - b");
                            ((NbtCompound) foundNbt).copyFrom((NbtCompound) nbt);
                        }
                    }

                });
            }
        };

        public static final Codec<ModifyNBTFunction.Operator> CODEC = StringIdentifiable.createCodec(ModifyNBTFunction.Operator::values);
        private final String name;

        public abstract void merge(NbtElement itemNbt, NbtPathArgumentType.NbtPath targetPath, NbtElement nbt) throws CommandSyntaxException;

        Operator(String name) {
            this.name = name;
        }

        public String asString() {
            return this.name;
        }
    }

    public record Path(String string, NbtPathArgumentType.NbtPath path) {
        public static final Codec<ModifyNBTFunction.Path> CODEC;

        public Path(String string, NbtPathArgumentType.NbtPath path) {
            this.string = string;
            this.path = path;
        }

        public static ModifyNBTFunction.Path parse(String path) throws CommandSyntaxException {
            NbtPathArgumentType.NbtPath nbtPath = (new NbtPathArgumentType()).parse(new StringReader(path));
            return new ModifyNBTFunction.Path(path, nbtPath);
        }

        public String string() {
            return this.string;
        }

        public NbtPathArgumentType.NbtPath path() {
            return this.path;
        }

        static {
            CODEC = Codec.STRING.comapFlatMap((path) -> {
                try {
                    return DataResult.success(parse(path));
                } catch (CommandSyntaxException var2) {
                    return DataResult.error(() -> {
                        return "Failed to parse path " + path + ": " + var2.getMessage();
                    });
                }
            }, ModifyNBTFunction.Path::string);
        }
    }
}
