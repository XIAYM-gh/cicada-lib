package nl.enjarai.cicada.api.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;

public class DrawUtils {
    public static void drawEntityFollowingMouse(MatrixStack matrices, int x, int y, int size, double rotation, double mouseX, double mouseY, LivingEntity entity) {
        float yaw = (float) (Math.atan((-mouseX + x) / 40.0F) * Math.sin((rotation / 180.0 + 0.5) * Math.PI));
        float pitch = (float) Math.atan((-mouseY + y - size * 1.6f) / 40.0F);

        Quaternionf entityRotation = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf pitchRotation = new Quaternionf().rotateX(pitch * 20.0F * 0.017453292F);
        entityRotation.mul(pitchRotation);

        float oldBodyYaw = entity.bodyYaw;
        float oldYaw = entity.getYaw();
        float oldPitch = entity.getPitch();
        /*? if >=1.21.5 {*/
        float oldPrevHeadYaw = entity.lastHeadYaw;
        /*?} else {*/
        /*float oldPrevHeadYaw = entity.prevHeadYaw;
        *//*?}*/
        float oldHeadYaw = entity.headYaw;
        entity.bodyYaw = 180.0F + yaw * 20.0F;
        entity.setYaw(180.0F + yaw * 40.0F);
        entity.setPitch(-pitch * 20.0F);
        entity.headYaw = entity.getYaw();
        /*? if >=1.21.5 {*/
        entity.lastHeadYaw = entity.getYaw();
        /*?} else {*/
        /*entity.prevHeadYaw = entity.getYaw();
         *//*?}*/

        /*? if >=1.20.5 {*/
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.translate(0.0f, 0.0f, 1000.0f);
        /*?} else {*/
        /*MatrixStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.push();
        modelViewStack.translate(0.0, 0.0, 1000.0);
        *//*?}*/

        //? if <=1.21.1
        /*RenderSystem.applyModelViewMatrix();*/

        matrices.push();
        matrices.translate(x, y, -950.0);
        matrices.multiplyPositionMatrix(new Matrix4f().scaling(size, size, -size));
        matrices.translate(0, -1, 0);
        matrices.multiply(entityRotation);
        matrices.translate(0, -1, 0);
        /*? if >=1.21.5 {*/
        DiffuseLighting.enableGuiShaderLighting();
        /*?} else {*/
        /*DiffuseLighting.method_34742();
        *//*?}*/

        EntityRenderDispatcher dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        if (pitchRotation != null) {
            pitchRotation.conjugate();
            dispatcher.setRotation(pitchRotation);
        }
        dispatcher.setRenderShadows(false);

        VertexConsumerProvider.Immediate vertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        //? if >1.21.1 {
        dispatcher.render(entity, 0.0, 0.0, 0.0, 1.0f, matrices, vertexConsumers, 0xF000F0);
        //?} else {
        /*dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, matrices, vertexConsumers, 0xF000F0);
         *///?}
        vertexConsumers.draw();

        dispatcher.setRenderShadows(true);

        matrices.pop();
        DiffuseLighting.enableGuiDepthLighting();

        /*? if >=1.20.5 {*/
        modelViewStack.popMatrix();
        /*?} else {*/
        /*modelViewStack.pop();
         *//*?}*/

        //? if <=1.21.1
        /*RenderSystem.applyModelViewMatrix();*/

        entity.bodyYaw = oldBodyYaw;
        entity.setYaw(oldYaw);
        entity.setPitch(oldPitch);
        /*? if >=1.21.5 {*/
        entity.lastHeadYaw = oldPrevHeadYaw;
        /*?} else {*/
        /*entity.prevHeadYaw = oldPrevHeadYaw;
         *//*?}*/
        entity.headYaw = oldHeadYaw;
    }
}
