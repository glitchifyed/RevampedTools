package net.glitchifyed.revampedtools.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.glitchifyed.revampedtools.RevampedTools;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    // disables equipment attributes when the item is disabled
    @ModifyExpressionValue(method = "getEquipmentChanges", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;shouldBreak()Z"))
    public boolean applyArmorToDamage(boolean original, @Local ItemStack itemStack2) {
        return original || !RevampedTools.isItemStackEnabled(itemStack2);
    }
}
