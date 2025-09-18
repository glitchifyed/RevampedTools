package net.glitchifyed.revampedtools.mixin;

import net.glitchifyed.revampedtools.gamerules.RTGamerules;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderDragonEntity.class)
public class EnderDragonEntityMixin {
    @Redirect(method = "updatePostDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonFight;hasPreviouslyKilled()Z"))
    public boolean hasPreviouslyKilled(EnderDragonFight instance) {
        if (RTGamerules.CONSISTENT_DRAGON_EXP.get()) {
            return false;
        }

        return instance.hasPreviouslyKilled();
    }
}