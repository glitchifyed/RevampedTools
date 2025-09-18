package net.glitchifyed.revampedtools.mixin;

import net.glitchifyed.revampedtools.RevampedTools;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Final @Shadow protected ServerPlayerEntity player;

    @Inject(method = "processBlockBreakingAction", at = @At("HEAD"), cancellable = true)
    public void processBlockBreakingAction(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence, CallbackInfo ci) {
        ItemStack stack = player.getMainHandStack();
        if (!RevampedTools.isItemStackEnabled(stack)) {
            ci.cancel();
        }
    }
}
