package net.tally.events;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.tally.DebugThingsClient;
import net.tally.components.DebugThingsComponents;
import org.joml.Matrix4f;

public class RenderEvents {
    public static float totalTickDelta;

    public static void initialize() {
        HudRenderCallback.EVENT.register(RenderEvents::hudRender);
        /*WorldRenderEvents.AFTER_ENTITIES.register(RenderEvents::worldAfterEntities);*/
    }

    private static void worldAfterEntities(WorldRenderContext worldRenderContext) {
        worldRenderContext.world().getEntities().forEach(p -> processEntity(p, worldRenderContext));
    }

    private static void processEntity(Entity entity, WorldRenderContext worldRenderContext) {
        if (!(entity instanceof LivingEntity)) { return; }

        MinecraftClient client = MinecraftClient.getInstance();
        NbtCompound nbtCompound = DebugThingsComponents.getInflictRender((LivingEntity) entity);
        VertexConsumerProvider vertexConsumerProvider = client.getBufferBuilders().getEntityVertexConsumers();
        DrawContext drawContext = new DrawContext(client, client.getBufferBuilders().getEntityVertexConsumers());
        MatrixStack matrix = drawContext.getMatrices();
        Camera camera = worldRenderContext.camera();
        double d = worldRenderContext.camera().getPos().distanceTo(entity.getPos());

        if (!(d > (double)4096.0F)) {
            boolean bl = !entity.isSneaky();
            float lHeight = entity.getNameLabelHeight() - 0.3f;
            float opacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            TextRenderer textRenderer = client.textRenderer;


            if (!nbtCompound.isEmpty() && entity.getUuid() != client.getGameProfile().getId()) {
                int maxLength = nbtCompound.getKeys().size() * 11;

                int status = 1;
                NbtCompound inflictions = DebugThingsClient.getInfliction();
                if (inflictions == null) {
                    return;
                }
                for (String key : inflictions.getKeys()) {
                    int stacks = inflictions.getInt(key);
                    Identifier id = Identifier.tryParse(key);
                    Identifier texture = Identifier.of(id.getNamespace(), "textures/inflictions/" + id.getPath() + ".png");

                    String text = String.valueOf(stacks);

                    float scale = 5f / ((float) textRenderer.getWidth(text));

                    matrix.push();
                    matrix.translate(0.0F, lHeight, 0.0F);
                    matrix.multiply(camera.getRotation());
                    matrix.scale(-0.025F, -0.025F, 0.025F);
                    Matrix4f matrix4f = matrix.peek().getPositionMatrix();
                    int j = (int)(opacity * 255.0F) << 24;
                    float h = (float)(-maxLength / 2);


                    matrix.scale(scale, scale, 1F);
                    float v = ((h + (11f * status)) + 8f) / scale;
                    textRenderer.draw(text, h, (float)0, 553648127, false,  matrix4f.translate(v, (4f / scale) + 4f, 0f), vertexConsumerProvider, bl ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, j, 15);
                    if (bl) {
                        textRenderer.draw(text, h, (float)0, -1, false, matrix4f, vertexConsumerProvider, TextRenderer.TextLayerType.NORMAL, 0, 15);
                    }

                    drawContext.drawTexture(texture, (int) (h + (11 * status)), 0, 11, 11, 0, 0,11, 11, 11, 11);

                    matrix.pop();
                    status += 1;
                }
            }
        }
    }

    public static void hudRender(DrawContext context, float tickDelta) {
        MatrixStack matrix = context.getMatrices();
        MinecraftClient client = MinecraftClient.getInstance();
        int scalex = context.getScaledWindowWidth();
        int scaley = context.getScaledWindowHeight();
        int status = 1;
        int hei = 1;
        NbtCompound inflictions = DebugThingsClient.getInfliction();
        if (inflictions == null) {
            return;
        }
        for (String key : inflictions.getKeys()) {
            int stacks = inflictions.getInt(key);
            Identifier id = Identifier.tryParse(key);
            Identifier texture = Identifier.of(id.getNamespace(), "textures/inflictions/" + id.getPath() + ".png");

            float scale = 5f / ((float) client.textRenderer.getWidth(String.valueOf(stacks)));

            matrix.push();
            matrix.scale(scale, scale, 1F);
            matrix.translate(0f, 0f, 1f);

            context.drawCenteredTextWithShadow(client.textRenderer, String.valueOf(stacks), (int) (((((float) scalex) - (13 * status)) + 10f) / scale), (int) ((((float) scaley - ((11f * hei) - 7f)) / scale) - 4f), 0xFFFFFFFF);

            matrix.pop();

            context.drawTexture(texture, (scalex - ((13 * status) - 2)), (scaley - ((11 * hei) + 3)), 11, 11, 0, 0,11, 11, 11, 11);
            status += 1;
            if (status > 12) {
                status = 1;
                hei += 1;
            }
            if (hei > 3) {
                break;
            }
        }
    }
}
