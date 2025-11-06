package net.tally.holders;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.command.*;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.argument.NbtElementArgumentType;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.DataCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DataExHolder {
    private static final SimpleCommandExceptionType MERGE_FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.data.merge.failed"));
    private static final DynamicCommandExceptionType GET_INVALID_EXCEPTION = new DynamicCommandExceptionType(
            path -> Text.stringifiedTranslatable("commands.data.get.invalid", path)
    );
    private static final DynamicCommandExceptionType GET_UNKNOWN_EXCEPTION = new DynamicCommandExceptionType(
            path -> Text.stringifiedTranslatable("commands.data.get.unknown", path)
    );
    private static final SimpleCommandExceptionType GET_MULTIPLE_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.data.get.multiple"));
    private static final DynamicCommandExceptionType MODIFY_EXPECTED_OBJECT_EXCEPTION = new DynamicCommandExceptionType(
            nbt -> Text.stringifiedTranslatable("commands.data.modify.expected_object", nbt)
    );
    private static final DynamicCommandExceptionType MODIFY_EXPECTED_VALUE_EXCEPTION = new DynamicCommandExceptionType(
            nbt -> Text.stringifiedTranslatable("commands.data.modify.expected_value", nbt)
    );
    private static final Dynamic2CommandExceptionType MODIFY_INVALID_SUBSTRING_EXCEPTION = new Dynamic2CommandExceptionType(
            (startIndex, endIndex) -> Text.stringifiedTranslatable("commands.data.modify.invalid_substring", startIndex, endIndex));

    public static final List<Function<String, DataCommand.ObjectType>> OBJECT_TYPE_FACTORIES = ImmutableList.of(EntityDataObject.TYPE_FACTORY, BlockDataObject.TYPE_FACTORY, StorageDataObject.TYPE_FACTORY);
    public static final List<DataCommand.ObjectType> TARGET_OBJECT_TYPES = OBJECT_TYPE_FACTORIES.stream().map(factory -> (DataCommand.ObjectType) factory.apply("target")).collect(ImmutableList.toImmutableList());

    public static final List<DataCommand.ObjectType> SOURCE_OBJECT_TYPES = (List<DataCommand.ObjectType>)OBJECT_TYPE_FACTORIES.stream()
            .map(factory -> (DataCommand.ObjectType)factory.apply("source"))
            .collect(ImmutableList.toImmutableList());

    private static final SuggestionProvider<ServerCommandSource> LIST_SUGGESTION_PROVIDER = new ListPointSuggestion();

    @Nullable
    private static NbtPathArgumentType.NbtPath parsePath(String rawPath) {
        try {
            return new NbtPathArgumentType().parse(new StringReader(rawPath));
        } catch (CommandSyntaxException var2) {
            return null;
        }
    }

    @Nullable
    private static NbtCompound parsePathNbt(String rawPath) {
        try {
            return NbtCompoundArgumentType.nbtCompound().parse(new StringReader(rawPath));
        } catch (CommandSyntaxException var2) {
            return null;
        }
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("dataex").requires(source -> source.hasPermissionLevel(2));

        for (DataCommand.ObjectType objectType : TARGET_OBJECT_TYPES) {
            literalArgumentBuilder.then(
                    objectType.addArgumentsToBuilder(
                            CommandManager.literal("merge"),
                            builder -> builder.then(
                                    CommandManager.argument("nbt", NbtCompoundArgumentType.nbtCompound())
                                            .executes(context -> executeMerge(context.getSource(), objectType.getObject(context), NbtCompoundArgumentType.getNbtCompound(context, "nbt")))
                                            .then(CommandManager.literal("localized")
                                                    .executes(context -> {
                                                        NbtCompound path = NbtCompoundArgumentType.getNbtCompound(context, "nbt");
                                                        String replaced = refactorPath(context, path.toString());
                                                        NbtCompound newPath = parsePathNbt(replaced);
                                                        return executeMerge(
                                                                context.getSource(),
                                                                objectType.getObject(context),
                                                                newPath
                                                        );
                                                    })
                                            )
                            )
                    )
            ).then(
                    objectType.addArgumentsToBuilder(
                            CommandManager.literal("get"),
                            builder -> builder.executes(context -> executeGet((ServerCommandSource) context.getSource(), objectType.getObject(context)))
                                    .then(
                                            CommandManager.argument("path", NbtPathArgumentType.nbtPath())
                                                    .executes(context -> executeGet(context.getSource(), objectType.getObject(context), NbtPathArgumentType.getNbtPath(context, "path")))
                                                    .then(CommandManager.literal("localized")
                                                            .executes(context -> {
                                                                NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path");
                                                                String replaced = refactorPath(context, path);
                                                                NbtPathArgumentType.NbtPath newPath = parsePath(replaced);
                                                                return executeGet(
                                                                        context.getSource(),
                                                                        objectType.getObject(context),
                                                                        newPath
                                                                );
                                                            })
                                                    )
                                                    .then(
                                                            CommandManager.argument("scale", DoubleArgumentType.doubleArg())
                                                                    .executes(
                                                                            context -> executeGet(
                                                                                    context.getSource(),
                                                                                    objectType.getObject(context),
                                                                                    NbtPathArgumentType.getNbtPath(context, "path"),
                                                                                    DoubleArgumentType.getDouble(context, "scale")
                                                                            )
                                                                    )
                                                                    .then(CommandManager.literal("localized")
                                                                            .executes(context -> {
                                                                                NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path");
                                                                                String replaced = refactorPath(context, path);
                                                                                NbtPathArgumentType.NbtPath newPath = parsePath(replaced);
                                                                                return executeGet(
                                                                                        context.getSource(),
                                                                                        objectType.getObject(context),
                                                                                        newPath,
                                                                                        DoubleArgumentType.getDouble(context, "scale")
                                                                                );
                                                                            })
                                                                    )
                                                    )
                                                    .then(
                                                            CommandManager.literal("list")
                                                                    .then(CommandManager.argument("point", StringArgumentType.word())
                                                                            .suggests(LIST_SUGGESTION_PROVIDER)

                                                                            .executes(context -> executeGet(context.getSource(), objectType.getObject(context), NbtPathArgumentType.getNbtPath(context, "path"), StringArgumentType.getString(context, "point")))
                                                                            .then(CommandManager.literal("localized")
                                                                                    .executes(context -> {
                                                                                        NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path");
                                                                                        String replaced = refactorPath(context, path);
                                                                                        NbtPathArgumentType.NbtPath newPath = parsePath(replaced);
                                                                                        return executeGet(
                                                                                                context.getSource(),
                                                                                                objectType.getObject(context),
                                                                                                newPath,
                                                                                                StringArgumentType.getString(context, "point")
                                                                                        );
                                                                                    })
                                                                            )
                                                                            .then(
                                                                                    CommandManager.argument("scale", DoubleArgumentType.doubleArg())
                                                                                            .executes(
                                                                                                    context -> executeGet(
                                                                                                            context.getSource(),
                                                                                                            objectType.getObject(context),
                                                                                                            NbtPathArgumentType.getNbtPath(context, "path"),
                                                                                                            StringArgumentType.getString(context, "point"),
                                                                                                            DoubleArgumentType.getDouble(context, "scale")
                                                                                                    )
                                                                                            )
                                                                                            .then(CommandManager.literal("localized")
                                                                                                    .executes(context -> {
                                                                                                        NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path");
                                                                                                        String replaced = refactorPath(context, path);
                                                                                                        NbtPathArgumentType.NbtPath newPath = parsePath(replaced);
                                                                                                        return executeGet(
                                                                                                                context.getSource(),
                                                                                                                objectType.getObject(context),
                                                                                                                newPath,
                                                                                                                StringArgumentType.getString(context, "point"),
                                                                                                                DoubleArgumentType.getDouble(context, "scale")
                                                                                                        );
                                                                                                    })
                                                                                            )
                                                                            )
                                                                    )
                                                    )
                                    )
                    )
            ).then(
                    objectType.addArgumentsToBuilder(
                            CommandManager.literal("remove"),
                            builder -> builder.then(
                                    CommandManager.argument("path", NbtPathArgumentType.nbtPath())
                                            .executes(context -> executeRemove(context.getSource(), objectType.getObject(context), NbtPathArgumentType.getNbtPath(context, "path")))
                                            .then(CommandManager.literal("localized")
                                                    .executes(context -> {
                                                        NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path");
                                                        String replaced = refactorPath(context, path);
                                                        NbtPathArgumentType.NbtPath newPath = parsePath(replaced);
                                                        return executeRemove(
                                                                context.getSource(),
                                                                objectType.getObject(context),
                                                                newPath
                                                        );
                                                    })
                                            )
                                            .then(
                                                    CommandManager.literal("list")
                                                            .then(CommandManager.argument("point", StringArgumentType.word())
                                                                    .suggests(LIST_SUGGESTION_PROVIDER)
                                                                    .executes(context -> executeRemove(context.getSource(), objectType.getObject(context), NbtPathArgumentType.getNbtPath(context, "path"), StringArgumentType.getString(context, "point")))
                                                                    .then(CommandManager.literal("localized")
                                                                            .executes(context -> {
                                                                                NbtPathArgumentType.NbtPath path = NbtPathArgumentType.getNbtPath(context, "path");
                                                                                String replaced = refactorPath(context, path);
                                                                                NbtPathArgumentType.NbtPath newPath = parsePath(replaced);
                                                                                return executeRemove(
                                                                                        context.getSource(),
                                                                                        objectType.getObject(context),
                                                                                        newPath,
                                                                                        StringArgumentType.getString(context, "point")
                                                                                );
                                                                            })
                                                                    )
                                                            )
                                            )
                            )
                    )
            )
                    .then(
                    addModifyArgument(
                            (builder, modifier) -> builder.then(
                                            CommandManager.literal("insert")
                                                    .then(
                                                            CommandManager.argument("index", IntegerArgumentType.integer())
                                                                    .then(modifier.create((context, sourceNbt, path, elements) -> path.insert(IntegerArgumentType.getInteger(context, "index"), sourceNbt, elements)))
                                                    )
                                    )
                                    .then(CommandManager.literal("prepend").then(modifier.create((context, sourceNbt, path, elements) -> path.insert(0, sourceNbt, elements))))
                                    .then(CommandManager.literal("append").then(modifier.create((context, sourceNbt, path, elements) -> path.insert(-1, sourceNbt, elements))))
                                    .then(CommandManager.literal("set").then(modifier.create((context, sourceNbt, path, elements) -> path.put(sourceNbt, Iterables.getLast(elements)))))
                                    .then(CommandManager.literal("merge").then(modifier.create((context, element, path, elements) -> {
                                        NbtCompound nbtCompound = new NbtCompound();

                                        for (NbtElement nbtElement : elements) {
                                            if (NbtPathArgumentType.NbtPath.isTooDeep(nbtElement, 0)) {
                                                throw NbtPathArgumentType.TOO_DEEP_EXCEPTION.create();
                                            }

                                            if (!(nbtElement instanceof NbtCompound nbtCompound2)) {
                                                throw MODIFY_EXPECTED_OBJECT_EXCEPTION.create(nbtElement);
                                            }

                                            nbtCompound.copyFrom(nbtCompound2);
                                        }

                                        Collection<NbtElement> collection = path.getOrInit(element, NbtCompound::new);
                                        int i = 0;

                                        for (NbtElement nbtElement2 : collection) {
                                            if (!(nbtElement2 instanceof NbtCompound nbtCompound3)) {
                                                throw MODIFY_EXPECTED_OBJECT_EXCEPTION.create(nbtElement2);
                                            }

                                            NbtCompound nbtCompound4 = nbtCompound3.copy();
                                            nbtCompound3.copyFrom(nbtCompound);
                                            i += nbtCompound4.equals(nbtCompound3) ? 0 : 1;
                                        }

                                        return i;
                                    })))
                    )
            );
        }
        dispatcher.register(literalArgumentBuilder);
    }

    private static int executeModify(
            CommandContext<ServerCommandSource> context, DataCommand.ObjectType objectType, DataExHolder.ModifyOperation modifier, List<NbtElement> elements
    ) throws CommandSyntaxException {
        DataCommandObject dataCommandObject = objectType.getObject(context);
        NbtPathArgumentType.NbtPath nbtPath = NbtPathArgumentType.getNbtPath(context, "targetPath");
        NbtCompound nbtCompound = dataCommandObject.getNbt();
        int i = modifier.modify(context, nbtCompound, nbtPath, elements);
        if (i == 0) {
            throw MERGE_FAILED_EXCEPTION.create();
        } else {
            dataCommandObject.setNbt(nbtCompound);
            context.getSource().sendFeedback(dataCommandObject::feedbackModify, true);
            return i;
        }
    }

    private static int executeModifyLocalized(
            CommandContext<ServerCommandSource> context, DataCommand.ObjectType objectType, DataExHolder.ModifyOperation modifier, List<NbtElement> elements
    ) throws CommandSyntaxException {
        DataCommandObject dataCommandObject = objectType.getObject(context);
        NbtPathArgumentType.NbtPath nbtPathO = NbtPathArgumentType.getNbtPath(context, "targetPath");

        String replaced = refactorPath(context, nbtPathO);

        NbtPathArgumentType.NbtPath nbtPath = parsePath(replaced);

        NbtCompound nbtCompound = dataCommandObject.getNbt();
        int i = modifier.modify(context, nbtCompound, nbtPath, elements);
        if (i == 0) {
            throw MERGE_FAILED_EXCEPTION.create();
        } else {
            dataCommandObject.setNbt(nbtCompound);
            context.getSource().sendFeedback(dataCommandObject::feedbackModify, true);
            return i;
        }
    }

    @FunctionalInterface
    interface ModifyArgumentCreator {
        ArgumentBuilder<ServerCommandSource, ?> create(DataExHolder.ModifyOperation modifier);
    }

    @FunctionalInterface
    interface ModifyOperation {
        int modify(CommandContext<ServerCommandSource> context, NbtCompound sourceNbt, NbtPathArgumentType.NbtPath path, List<NbtElement> elements) throws CommandSyntaxException;
    }

    private static ArgumentBuilder<ServerCommandSource, ?> addModifyArgument(
            BiConsumer<ArgumentBuilder<ServerCommandSource, ?>, DataExHolder.ModifyArgumentCreator> subArgumentAdder
    ) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("modify");

        for (DataCommand.ObjectType objectType : TARGET_OBJECT_TYPES) {
            objectType.addArgumentsToBuilder(
                    literalArgumentBuilder,
                    builder -> {
                        ArgumentBuilder<ServerCommandSource, ?> argumentBuilder = CommandManager.argument("targetPath", NbtPathArgumentType.nbtPath());

                        for (DataCommand.ObjectType objectType2 : SOURCE_OBJECT_TYPES) {
                            subArgumentAdder.accept(
                                    argumentBuilder,
                                    (DataExHolder.ModifyArgumentCreator)operation -> objectType2.addArgumentsToBuilder(
                                            CommandManager.literal("from"),
                                            builderx -> builderx.executes(context -> executeModify(context, objectType, operation, getValues(context, objectType2)))
                                                    .then(
                                                            CommandManager.argument("sourcePath", NbtPathArgumentType.nbtPath())
                                                                    .then(
                                                                            CommandManager.literal("list")
                                                                                    .then(CommandManager.argument("point", StringArgumentType.word())
                                                                                            .suggests(LIST_SUGGESTION_PROVIDER)
                                                                                            .executes(context -> executeModifyLocalized(context, objectType, operation, getValuesByPath(context, objectType2, StringArgumentType.getString(context, "point"))))
                                                                                            .then(CommandManager.literal("localized")
                                                                                                    .executes(context -> executeModifyLocalized(context, objectType, operation, getValuesByPathLocalized(context, objectType2, StringArgumentType.getString(context, "point"))))
                                                                                            )
                                                                                    )
                                                                    )
                                                                    .then(CommandManager.literal("localized")
                                                                            .executes(context -> executeModifyLocalized(context, objectType, operation, getValuesByPathLocalized(context, objectType2)))
                                                                    )
                                                                    .executes(context -> executeModify(context, objectType, operation, getValuesByPath(context, objectType2)))
                                                    )
                                    )
                            );
                            subArgumentAdder.accept(
                                    argumentBuilder,
                                    (DataExHolder.ModifyArgumentCreator)operation -> objectType2.addArgumentsToBuilder(
                                            CommandManager.literal("string"),
                                            builderx -> builderx.executes(context -> executeModify(context, objectType, operation, mapValues(getValues(context, objectType2), value -> value)))
                                                    .then(
                                                            CommandManager.argument("sourcePath", NbtPathArgumentType.nbtPath())
                                                                    .executes(context -> executeModify(context, objectType, operation, mapValues(getValuesByPath(context, objectType2), value -> value)))
                                                                    .then(
                                                                            CommandManager.argument("start", IntegerArgumentType.integer())
                                                                                    .then(CommandManager.literal("localized")
                                                                                            .executes(
                                                                                                    context -> executeModifyLocalized(
                                                                                                            context,
                                                                                                            objectType,
                                                                                                            operation,
                                                                                                            mapValues(getValuesByPathLocalized(context, objectType2), value -> substring(value, IntegerArgumentType.getInteger(context, "start")))
                                                                                                    )
                                                                                            )
                                                                                    )
                                                                                    .executes(
                                                                                            context -> executeModify(
                                                                                                    context,
                                                                                                    objectType,
                                                                                                    operation,
                                                                                                    mapValues(getValuesByPath(context, objectType2), value -> substring(value, IntegerArgumentType.getInteger(context, "start")))
                                                                                            )
                                                                                    )
                                                                                    .then(
                                                                                            CommandManager.argument("end", IntegerArgumentType.integer())
                                                                                                    .then(CommandManager.literal("localized")
                                                                                                            .executes(
                                                                                                                    context -> executeModifyLocalized(
                                                                                                                            context,
                                                                                                                            objectType,
                                                                                                                            operation,
                                                                                                                            mapValues(
                                                                                                                                    getValuesByPathLocalized(context, objectType2),
                                                                                                                                    value -> substring(value, IntegerArgumentType.getInteger(context, "start"), IntegerArgumentType.getInteger(context, "end"))
                                                                                                                            )
                                                                                                                    )
                                                                                                            )
                                                                                                    )
                                                                                                    .executes(
                                                                                                            context -> executeModify(
                                                                                                                    context,
                                                                                                                    objectType,
                                                                                                                    operation,
                                                                                                                    mapValues(
                                                                                                                            getValuesByPath(context, objectType2),
                                                                                                                            value -> substring(value, IntegerArgumentType.getInteger(context, "start"), IntegerArgumentType.getInteger(context, "end"))
                                                                                                                    )
                                                                                                            )
                                                                                                    )
                                                                                    )
                                                                    )
                                                    )
                                    )
                            );
                        }

                        subArgumentAdder.accept(
                                argumentBuilder,
                                (DataExHolder.ModifyArgumentCreator)modifier -> CommandManager.literal("value")
                                        .then(CommandManager.argument("value", NbtElementArgumentType.nbtElement())
                                                .then(CommandManager.literal("localized")
                                                        .executes(context -> {
                                                            List<NbtElement> list = Collections.singletonList(NbtElementArgumentType.getNbtElement(context, "value"));
                                                            return executeModifyLocalized(context, objectType, modifier, list);
                                                        })
                                                )
                                                .executes(context -> {
                                            List<NbtElement> list = Collections.singletonList(NbtElementArgumentType.getNbtElement(context, "value"));
                                            return executeModify(context, objectType, modifier, list);
                                        }))
                        );
                        return builder.then(argumentBuilder);
                    }
            );
        }

        return literalArgumentBuilder;
    }

    private static String refactorPath(CommandContext<ServerCommandSource> context, NbtPathArgumentType.NbtPath path) {
        String replaced = path.getString();

        if (context.getSource().getPlayer() != null) {
            replaced = replaced.replace("$LOCALIZEDNAME", context.getSource().getPlayer().getName().getLiteralString());
        }

        if (context.getSource().getEntity() != null) {
            replaced = replaced.replace("$LOCALIZEDUUID", context.getSource().getEntity().getUuidAsString());
        }
        return replaced;
    }

    private static String refactorPath(CommandContext<ServerCommandSource> context, String replaced) {
        if (context.getSource().getPlayer() != null) {
            replaced = replaced.replace("$LOCALIZEDNAME", context.getSource().getPlayer().getName().getLiteralString());
        }

        if (context.getSource().getEntity() != null) {
            replaced = replaced.replace("$LOCALIZEDUUID", context.getSource().getEntity().getUuidAsString());
        }
        return replaced;
    }

    public static class ListPointSuggestion implements SuggestionProvider<ServerCommandSource> {
        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            builder.suggest("end").suggest("start");
            return builder.buildFuture();
        }
    }

    private static String asString(NbtElement nbt) throws CommandSyntaxException {
        if (nbt.getNbtType().isImmutable()) {
            return nbt.asString();
        } else {
            throw MODIFY_EXPECTED_VALUE_EXCEPTION.create(nbt);
        }
    }

    private static List<NbtElement> mapValues(List<NbtElement> list, DataExHolder.Processor processor) throws CommandSyntaxException {
        List<NbtElement> list2 = new ArrayList(list.size());

        for (NbtElement nbtElement : list) {
            String string = asString(nbtElement);
            list2.add(NbtString.of(processor.process(string)));
        }

        return list2;
    }

    private static String substringInternal(String string, int startIndex, int endIndex) throws CommandSyntaxException {
        if (startIndex >= 0 && endIndex <= string.length() && startIndex <= endIndex) {
            return string.substring(startIndex, endIndex);
        } else {
            throw MODIFY_INVALID_SUBSTRING_EXCEPTION.create(startIndex, endIndex);
        }
    }

    private static String substring(String string, int startIndex, int endIndex) throws CommandSyntaxException {
        int i = string.length();
        int j = getSubstringIndex(startIndex, i);
        int k = getSubstringIndex(endIndex, i);
        return substringInternal(string, j, k);
    }

    private static String substring(String string, int startIndex) throws CommandSyntaxException {
        int i = string.length();
        return substringInternal(string, getSubstringIndex(startIndex, i), i);
    }

    private static int getSubstringIndex(int index, int length) {
        return index >= 0 ? index : length + index;
    }

    private static List<NbtElement> getValues(CommandContext<ServerCommandSource> context, DataCommand.ObjectType objectType) throws CommandSyntaxException {
        DataCommandObject dataCommandObject = objectType.getObject(context);
        return Collections.singletonList(dataCommandObject.getNbt());
    }

    private static List<NbtElement> getValuesByPath(CommandContext<ServerCommandSource> context, DataCommand.ObjectType objectType) throws CommandSyntaxException {
        DataCommandObject dataCommandObject = objectType.getObject(context);
        NbtPathArgumentType.NbtPath nbtPath = NbtPathArgumentType.getNbtPath(context, "sourcePath");
        return nbtPath.get(dataCommandObject.getNbt());
    }

    private static List<NbtElement> getValuesByPathLocalized(CommandContext<ServerCommandSource> context, DataCommand.ObjectType objectType) throws CommandSyntaxException {
        DataCommandObject dataCommandObject = objectType.getObject(context);

        NbtPathArgumentType.NbtPath nbtPathO = NbtPathArgumentType.getNbtPath(context, "sourcePath");

        String replaced = refactorPath(context, nbtPathO);

        NbtPathArgumentType.NbtPath nbtPath = parsePath(replaced);
        return nbtPath.get(dataCommandObject.getNbt());
    }

    private static List<NbtElement> getValuesByPath(CommandContext<ServerCommandSource> context, DataCommand.ObjectType objectType, String point) throws CommandSyntaxException {
        DataCommandObject dataCommandObject = objectType.getObject(context);
        NbtPathArgumentType.NbtPath nbtPath = NbtPathArgumentType.getNbtPath(context, "sourcePath");

        List<NbtElement> nbtElement = nbtPath.get(dataCommandObject.getNbt());

        if (nbtElement == null || nbtElement.get(0).getNbtType() != NbtList.TYPE) {
            throw GET_INVALID_EXCEPTION.create(nbtPath.toString());
        }

        NbtElement nbtOld = nbtElement.get(0);

        NbtElement newnbtElement = Objects.equals(point, "end") ? ((NbtList) nbtOld).get(((NbtList) nbtOld).size() - 1) : ((NbtList) nbtOld).get(0);

        return Collections.singletonList(newnbtElement);
    }

    private static List<NbtElement> getValuesByPathLocalized(CommandContext<ServerCommandSource> context, DataCommand.ObjectType objectType, String point) throws CommandSyntaxException {
        DataCommandObject dataCommandObject = objectType.getObject(context);
        NbtPathArgumentType.NbtPath nbtPathO = NbtPathArgumentType.getNbtPath(context, "sourcePath");

        String replaced = refactorPath(context, nbtPathO);

        NbtPathArgumentType.NbtPath nbtPath = parsePath(replaced);

        List<NbtElement> nbtElement = nbtPath.get(dataCommandObject.getNbt());

        if (nbtElement == null || nbtElement.get(0).getNbtType() != NbtList.TYPE) {
            throw GET_INVALID_EXCEPTION.create(nbtPath.toString());
        }

        NbtElement nbtOld = nbtElement.get(0);

        NbtElement newnbtElement = Objects.equals(point, "end") ? ((NbtList) nbtOld).get(((NbtList) nbtOld).size() - 1) : ((NbtList) nbtOld).get(0);

        return Collections.singletonList(newnbtElement);
    }

    private static int executeMerge(ServerCommandSource source, DataCommandObject object, NbtCompound nbt) throws CommandSyntaxException {
        NbtCompound nbtCompound = object.getNbt();
        if (NbtPathArgumentType.NbtPath.isTooDeep(nbt, 0)) {
            throw NbtPathArgumentType.TOO_DEEP_EXCEPTION.create();
        } else {
            NbtCompound nbtCompound2 = nbtCompound.copy().copyFrom(nbt);
            if (nbtCompound.equals(nbtCompound2)) {
                throw MERGE_FAILED_EXCEPTION.create();
            } else {
                object.setNbt(nbtCompound2);
                source.sendFeedback(() -> object.feedbackModify(), true);
                return 1;
            }
        }
    }

    private static int executeRemove(ServerCommandSource source, DataCommandObject object, NbtPathArgumentType.NbtPath path) throws CommandSyntaxException {
        NbtCompound nbtCompound = object.getNbt();
        int i = path.remove(nbtCompound);
        if (i == 0) {
            throw MERGE_FAILED_EXCEPTION.create();
        } else {
            object.setNbt(nbtCompound);
            source.sendFeedback(() -> object.feedbackModify(), true);
            return i;
        }
    }

    private static int executeRemove(ServerCommandSource source, DataCommandObject object, NbtPathArgumentType.NbtPath path, String point) throws CommandSyntaxException {
        NbtElement nbtElement = getNbt(path, object);

        if (nbtElement == null || nbtElement.getNbtType() != NbtList.TYPE) {
            throw GET_INVALID_EXCEPTION.create(path.toString());
        }

        ((NbtList)nbtElement).remove(Objects.equals(point, "end") ? (((NbtList) nbtElement).size() - 1) : 0);

        return 1;
    }

    public static NbtElement getNbt(NbtPathArgumentType.NbtPath path, DataCommandObject object) throws CommandSyntaxException {
        Collection<NbtElement> collection = path.get(object.getNbt());
        Iterator<NbtElement> iterator = collection.iterator();
        NbtElement nbtElement = (NbtElement) iterator.next();
        if (iterator.hasNext()) {
            throw GET_MULTIPLE_EXCEPTION.create();
        } else {
            return nbtElement;
        }
    }

    private static int executeGet(ServerCommandSource source, DataCommandObject object, NbtPathArgumentType.NbtPath path) throws CommandSyntaxException {
        NbtElement nbtElement = getNbt(path, object);
        int i;
        if (nbtElement instanceof AbstractNbtNumber) {
            i = MathHelper.floor(((AbstractNbtNumber) nbtElement).doubleValue());
        } else if (nbtElement instanceof AbstractNbtList) {
            i = ((AbstractNbtList) nbtElement).size();
        } else if (nbtElement instanceof NbtCompound) {
            i = ((NbtCompound) nbtElement).getSize();
        } else {
            if (!(nbtElement instanceof NbtString)) {
                throw GET_UNKNOWN_EXCEPTION.create(path.toString());
            }

            i = nbtElement.asString().length();
        }

        source.sendFeedback(() -> object.feedbackQuery(nbtElement), false);
        return i;
    }

    private static int executeGet(ServerCommandSource source, DataCommandObject object, NbtPathArgumentType.NbtPath path, String point) throws CommandSyntaxException {
        NbtElement nbtElement = getNbt(path, object);

        if (nbtElement == null || nbtElement.getNbtType() != NbtList.TYPE) {
            throw GET_INVALID_EXCEPTION.create(path.toString());
        }

        nbtElement = Objects.equals(point, "end") ? ((NbtList) nbtElement).get(((NbtList) nbtElement).size() - 1) : ((NbtList) nbtElement).get(0);

        int i;
        if (nbtElement instanceof AbstractNbtNumber) {
            i = MathHelper.floor(((AbstractNbtNumber) nbtElement).doubleValue());
        } else if (nbtElement instanceof AbstractNbtList) {
            i = ((AbstractNbtList) nbtElement).size();
        } else if (nbtElement instanceof NbtCompound) {
            i = ((NbtCompound) nbtElement).getSize();
        } else {
            if (!(nbtElement instanceof NbtString)) {
                throw GET_UNKNOWN_EXCEPTION.create(path.toString());
            }

            i = nbtElement.asString().length();
        }

        NbtElement finalNbtElement = nbtElement;
        source.sendFeedback(() -> object.feedbackQuery(finalNbtElement), false);
        return i;
    }

    private static int executeGet(ServerCommandSource source, DataCommandObject object, NbtPathArgumentType.NbtPath path, String point, double scale) throws CommandSyntaxException {
        NbtElement nbtElement = getNbt(path, object);

        if (nbtElement == null || nbtElement.getNbtType() != NbtList.TYPE) {
            throw GET_INVALID_EXCEPTION.create(path.toString());
        }

        nbtElement = Objects.equals(point, "end") ? ((NbtList) nbtElement).get(((NbtList) nbtElement).size() - 1) : ((NbtList) nbtElement).get(0);

        if (!(nbtElement instanceof AbstractNbtNumber)) {
            throw GET_INVALID_EXCEPTION.create(path.toString());
        } else {
            int i = MathHelper.floor(((AbstractNbtNumber) nbtElement).doubleValue() * scale);
            source.sendFeedback(() -> object.feedbackGet(path, scale, i), false);
            return i;
        }
    }

    private static int executeGet(ServerCommandSource source, DataCommandObject object, NbtPathArgumentType.NbtPath path, double scale) throws CommandSyntaxException {
        NbtElement nbtElement = getNbt(path, object);
        if (!(nbtElement instanceof AbstractNbtNumber)) {
            throw GET_INVALID_EXCEPTION.create(path.toString());
        } else {
            int i = MathHelper.floor(((AbstractNbtNumber) nbtElement).doubleValue() * scale);
            source.sendFeedback(() -> object.feedbackGet(path, scale, i), false);
            return i;
        }
    }

    private static int executeGet(ServerCommandSource source, DataCommandObject object) throws CommandSyntaxException {
        NbtCompound nbtCompound = object.getNbt();
        source.sendFeedback(() -> object.feedbackQuery(nbtCompound), false);
        return 1;
    }

    @FunctionalInterface
    interface Processor {
        String process(String string) throws CommandSyntaxException;
    }
}
