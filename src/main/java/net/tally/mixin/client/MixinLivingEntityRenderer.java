package net.tally.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.tally.DebugThings;
import net.tally.DebugThingsClient;
import net.tally.components.DebugThingsComponents;
import net.tally.helpers.IEntityDataSaver;
import net.tally.helpers.InflictionAccessor;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

    protected MixinLivingEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "HEAD"))
    public void debugthings$livingEntityRender(T livingEntity, float f, float g, MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        NbtCompound inflictions = DebugThingsComponents.getInflictRender(livingEntity);
        TextRenderer textRenderer = this.getTextRenderer();

        double d = this.dispatcher.getSquaredDistanceToCamera(livingEntity);

        if (!(d > (double)4096.0F)) {
            boolean bl = !livingEntity.isSneaky();
            float lHeight = livingEntity.getNameLabelHeight() + 0.2f;
            float opacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);

            if (!(inflictions == null) && !inflictions.isEmpty() && livingEntity.getUuid() != client.getGameProfile().getId()) {
                int maxLength = inflictions.getKeys().size() * 11;

                int status = 0;
                for (String key : inflictions.getKeys()) {
                    int stacks = inflictions.getInt(key);
                    Identifier id = Identifier.tryParse(key);

                    String text = String.valueOf(stacks);

                    float scale = 5f / ((float) textRenderer.getWidth(text));

                    matrix.push();
                    matrix.translate(0.0F, lHeight, 0.0F);
                    matrix.multiply(this.dispatcher.getRotation());
                    matrix.scale(-0.025F, -0.025F, 0.025F);
                    Matrix4f matrix4f = matrix.peek().getPositionMatrix();
                    Matrix4f matrix4fb = matrix.peek().getPositionMatrix();
                    int j = (int)(opacity * 255.0F) << 24;
                    float h = (float)(-maxLength / 2);
                    if (bl) {
                        textRenderer.draw(Text.literal("\uE000").setStyle(Style.EMPTY.withFont(id)), h, (float)0, Colors.WHITE, false, matrix4f.translate((11 * status), 0f, 0f), vertexConsumerProvider, TextRenderer.TextLayerType.NORMAL, 0, light);
                        textRenderer.draw(text, (h + 1) / scale, -6 / scale, Colors.WHITE, false, matrix4fb.translate(0, 0, -0.1f).scale(scale), vertexConsumerProvider, TextRenderer.TextLayerType.NORMAL, 0, light);
                    }

                    matrix.pop();
                    status += 1;
                }
            }
        }
    }

    @Override
    public Identifier getTexture(T entity) {
        return null;
    }
}
