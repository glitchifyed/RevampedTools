package net.glitchifyed.revampedtools.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.glitchifyed.revampedtools.gamerules.RTGamerules;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

import static net.glitchifyed.revampedtools.RevampedTools.isItemStackEnabled;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique
    private static void disableItemBreaking(CallbackInfoReturnable<Boolean> cir) {
        if (!RTGamerules.PERSISTENT_EQUIPMENT.get()) {
            return;
        }

        cir.setReturnValue(false);
    }

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    public void getMaxDamage(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        var enchantments = stack.getEnchantments();
        int unbreakingLevel = 0;

        for (var entry : enchantments.getEnchantments()) {
            // check if it has item_damage component types rather than just checking if its unbreaking
            if (!entry.value().effects().contains(EnchantmentEffectComponentTypes.ITEM_DAMAGE)) {
                continue;
            }

            int level = enchantments.getLevel(entry);
            if (level < unbreakingLevel) {
                continue;
            }

            unbreakingLevel = level;

            break;
        }

        int realDamage = stack.getOrDefault(DataComponentTypes.MAX_DAMAGE, 0);

        cir.setReturnValue((int)(realDamage * (1 + RTGamerules.MAX_UNBREAKING_FACTOR.get() / 30f * unbreakingLevel)));
    }

    // i did wanna remove the ITEM_DAMAGE tag from unbreaking but like mending i couldnt figure it out, so instead i just removed the EnchantmentHelper.getItemDamage call
    // i also couldve removed ITEM_DAMAGEs changes to getItemDamage but i cannot for the life of me find where its mentioned
    @Inject(method = "calculateDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getItemDamage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;I)I"), cancellable = true)
    public void calculateDamage(int baseDamage, ServerWorld world, ServerPlayerEntity player, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(baseDamage);
    }

    @Inject(method = "isItemEnabled", at = @At("RETURN"), cancellable = true)
    public void isItemEnabled(FeatureSet enabledFeatures, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack)(Object) this;

        if (!isItemStackEnabled(stack)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "shouldBreak", at = @At("RETURN"), cancellable = true)
    public void shouldBreak(CallbackInfoReturnable<Boolean> cir) {
        disableItemBreaking(cir);
    }

    @Inject(method = "willBreakNextUse", at = @At("RETURN"), cancellable = true)
    public void willBreakNextUse(CallbackInfoReturnable<Boolean> cir) {
        disableItemBreaking(cir);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = (ItemStack)(Object) this;

        if (!isItemStackEnabled(stack)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = (ItemStack)(Object) this;

        if (!isItemStackEnabled(stack)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(method = "useOnEntity", at = @At("HEAD"), cancellable = true)
    public void useOnEntity(PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = (ItemStack)(Object) this;

        if (!isItemStackEnabled(stack)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(method = "canBreak", at = @At("RETURN"), cancellable = true)
    public void canBreak(CachedBlockPosition pos, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack)(Object) this;

        if (!isItemStackEnabled(stack)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canPlaceOn", at = @At("RETURN"), cancellable = true)
    public void canPlaceOn(CachedBlockPosition pos, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack)(Object) this;

        if (!isItemStackEnabled(stack)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canMine", at = @At("RETURN"), cancellable = true)
    public void canMine(BlockState state, World world, BlockPos pos, PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack)(Object) this;

        if (!isItemStackEnabled(stack)) {
            cir.setReturnValue(false);
        }
    }

    @ModifyExpressionValue(method = "appendTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isDamaged()Z"))
    public boolean removeAdvancedDurabilityTooltip(boolean original) {
        return original && !RTGamerules.DISPLAY_DURABILITY.get();
    }

    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/tooltip/TooltipType;isAdvanced()Z"))
    public void appendDurabilityToTooltip(Item.TooltipContext context, TooltipDisplayComponent displayComponent, PlayerEntity player, TooltipType type, Consumer<Text> textConsumer, CallbackInfo ci) {
        ItemStack stack = (ItemStack)(Object) this;
        if (!stack.isDamageable()) {
            return;
        }

        boolean display = RTGamerules.DISPLAY_DURABILITY.get();
        boolean persistent = RTGamerules.PERSISTENT_EQUIPMENT.get();

        if (!display && !persistent) {
            return;
        }

        textConsumer.accept(Text.literal(""));

        int max = stack.getMaxDamage();
        int current = stack.getDamage();

        if (persistent && current == max) {
            textConsumer.accept(Text.translatable("item.revampedtools.persistentBroken").formatted(Formatting.RED));
            textConsumer.accept(Text.translatable("item.revampedtools.persistentExplainer").formatted(Formatting.ITALIC).formatted(Formatting.DARK_RED));

            return;
        }

        int durability = max - current;
        float relative = durability / (float)max;

        int percent = (int)(relative * 100);

        var colour = relative > 0.75f ? Formatting.GREEN : relative > 0.5f ? Formatting.YELLOW : relative > 0.25f ? Formatting.GOLD : Formatting.RED;

        textConsumer.accept(Text.translatable("item.durability", durability, max).formatted(colour).append(Text.literal(" (" + percent + "%)")));
    }
}