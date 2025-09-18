package net.glitchifyed.revampedtools.mixin;

import net.glitchifyed.revampedtools.RevampedTools;
import net.glitchifyed.revampedtools.gamerules.RTGamerules;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Shadow public int experienceLevel;

    @Redirect(method = "addExperienceLevels", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;experienceLevel:I", opcode = Opcodes.GETFIELD, ordinal = 2))
    public int experienceLevel2(PlayerEntity instance) {
        if (RTGamerules.REBALANCED_EXP_LEVELS.get()) {
            return instance.experienceLevel * 5;
        }

        return instance.experienceLevel;
    }

    // make it so each level up plays a noise instead of every 5 levels
    @Redirect(method = "addExperienceLevels", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;age:I", opcode = Opcodes.GETFIELD, ordinal = 0))
    public int age1(PlayerEntity instance) {
        if (RTGamerules.REBALANCED_EXP_LEVELS.get()) {
            return Integer.MAX_VALUE;
        }

        return instance.age;
    }

    @Inject(method = "getNextLevelExperience", at = @At("RETURN"), cancellable = true)
    public void getNextLevelExperience(CallbackInfoReturnable<Integer> cir) {
        if (!RTGamerules.REBALANCED_EXP_LEVELS.get()) {
            return;
        }

        int currentLevel = this.experienceLevel;
        int exp = 100 + currentLevel * 20;

        cir.setReturnValue(exp);
    }

    @Inject(method = "isBlockBreakingRestricted", at = @At("HEAD"), cancellable = true)
    public void isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity)(Object) this;
        ItemStack itemStack = player.getMainHandStack();

        if (!RevampedTools.isItemStackEnabled(itemStack)) {
            cir.setReturnValue(true);
        }
    }
}
