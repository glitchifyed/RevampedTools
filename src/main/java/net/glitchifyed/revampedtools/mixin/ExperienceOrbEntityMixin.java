package net.glitchifyed.revampedtools.mixin;

import net.glitchifyed.revampedtools.RevampedTools;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {
    // i tried removing REPAIR_WITH_XP from mending but im not very good with mixins yet, so i just decided to bypass the repair method entirely
    @Inject(method = "repairPlayerGears", at = @At("HEAD"), cancellable = true)
    private void repairPlayerGears(ServerPlayerEntity player, int amount, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(amount);
    }
}