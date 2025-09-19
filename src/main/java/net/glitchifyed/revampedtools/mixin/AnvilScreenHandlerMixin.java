package net.glitchifyed.revampedtools.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.glitchifyed.revampedtools.RevampedTools;
import net.glitchifyed.revampedtools.anvil.ForgingScreenHandlerExtender;
import net.glitchifyed.revampedtools.gamerules.RTGamerules;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    @Final @Shadow private Property levelCost;
    @Shadow private String newItemName;
    @Shadow private int repairItemUsage;

    @Unique private boolean outputUsable;

    @ModifyExpressionValue(method = "canTakeOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/Property;get()I", ordinal = 1))
    public int canTakeOutput(int original) {
        if (!RTGamerules.REWORKED_ANVILS.get() || !outputUsable) {
            return original;
        }

        return original + 1;
    }

    @Inject(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;getStack(I)Lnet/minecraft/item/ItemStack;"), cancellable = true)
    public void updateResult(CallbackInfo ci) {
        if (!RTGamerules.REWORKED_ANVILS.get()) {
            return;
        }

        ForgingScreenHandlerExtender screenHandler = (ForgingScreenHandlerExtender) this;

        var input = screenHandler.revampedtools$getInput();
        var output = screenHandler.revampedtools$getOutput();

        boolean modified = false;

        int tempLevels = 0;
        int repairingUsage = 0;
        ItemStack outputStack = ItemStack.EMPTY;

        ItemStack primaryInputStack = input.getStack(0);
        ItemStack secondaryInputStack = input.getStack(1);

        boolean equalItems = primaryInputStack.isOf(secondaryInputStack.getItem());

        int repairDamage = 0;

        if (!primaryInputStack.isEmpty()) {
            outputStack = primaryInputStack.copy();

            var itemName = primaryInputStack.getName().getString();
            var customName = primaryInputStack.getCustomName();

            if (newItemName != null && !StringHelper.isBlank(newItemName) && (customName == null && !Objects.equals(newItemName, itemName) || customName != null && !Objects.equals(newItemName, customName.getString()))) {
                outputStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(newItemName));
                //tempLevels += 1;

                modified = true;
            }

            if (!secondaryInputStack.isEmpty()) {
                if (EnchantmentHelper.canHaveEnchantments(outputStack) && EnchantmentHelper.canHaveEnchantments(secondaryInputStack) && (equalItems || !primaryInputStack.isOf(Items.ENCHANTED_BOOK) && secondaryInputStack.isOf(Items.ENCHANTED_BOOK))) {
                    ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(outputStack));

                    var enchantmentsComponent = EnchantmentHelper.getEnchantments(secondaryInputStack);
                    var primaryEnchantsComponent = EnchantmentHelper.getEnchantments(primaryInputStack);

                    boolean secondaryIsBook = secondaryInputStack.isOf(Items.ENCHANTED_BOOK);
                    boolean twoBooks = primaryInputStack.isOf(Items.ENCHANTED_BOOK) && secondaryIsBook;

                    boolean acceptable = true;

                    for (var entry : enchantmentsComponent.getEnchantments()) {
                        var enchantment = entry.value();

                        if (!enchantment.isAcceptableItem(outputStack) && !twoBooks) {
                            if (secondaryIsBook) {
                                continue;
                            }

                            acceptable = false;

                            break;
                        }

                        for (var entry2 : primaryEnchantsComponent.getEnchantments()) {
                            if (entry == entry2 || Enchantment.canBeCombined(entry, entry2)) {
                                continue;
                            }

                            acceptable = false;

                            break;
                        }

                        if (!acceptable) {
                            break;
                        }

                        var level = enchantmentsComponent.getLevel(entry);
                        var primaryLevel = primaryEnchantsComponent.getLevel(entry);

                        // takes the highest value or increases the level up to the max level if both are equal
                        var resultingLevel = level == primaryLevel ? Math.min(level + 1, enchantment.getMaxLevel()) : Math.max(level, primaryLevel);

                        builder.set(entry, resultingLevel);
                    }

                    if (acceptable) {
                        var outputComponent = builder.build();

                        for (var entry : outputComponent.getEnchantments()) {
                            var resultLevel = outputComponent.getLevel(entry);

                            var primaryLevel = primaryEnchantsComponent.getLevel(entry);
                            var secondaryLevel = enchantmentsComponent.getLevel(entry);

                            var minLevel = Math.min(primaryLevel, secondaryLevel);
                            var difference = resultLevel - minLevel;

                            if (difference == 0) {
                                continue;
                            }

                            tempLevels += 3 * difference;

                            modified = true;
                        }

                        if (!modified && equalItems && primaryInputStack.isDamageable() && (primaryInputStack.getDamage() != 0 || secondaryInputStack.getDamage() != 0)) {
                            modified = true;
                        }

                        EnchantmentHelper.set(outputStack, outputComponent);
                    }
                    else {
                        modified = false;
                    }
                }
                else if (primaryInputStack.canRepairWith(secondaryInputStack)) {
                    modified = true;
                }
                else {
                    modified = false;
                }

                if (modified) {
                    if (equalItems && primaryInputStack.isDamageable()) {
                        repairDamage = secondaryInputStack.getMaxDamage() - secondaryInputStack.getDamage();
                    }
                    else if (primaryInputStack.canRepairWith(secondaryInputStack)) {
                        int maxDamage = primaryInputStack.getMaxDamage();

                        int repairAmount = maxDamage / 4 * secondaryInputStack.getCount();
                        int maxRepairAmount = Math.min(repairAmount, primaryInputStack.getDamage());

                        if (maxRepairAmount != 0) {
                            repairingUsage = (int) Math.ceil(maxRepairAmount / (float)maxDamage * 4);
                            repairDamage = maxRepairAmount;
                        }
                        else {
                            modified = false;
                        }
                    }
                }
            }
        }

        if (modified) {
            if (repairDamage > 0) {
                int currentDamage = primaryInputStack.getDamage();
                int newDamage = Math.max(currentDamage - repairDamage, 0);

                int diff = currentDamage - newDamage;
                if (diff > 0) {
                    outputStack.setDamage(newDamage);

                    int minDamage = secondaryInputStack.isDamageable() ? Math.min(primaryInputStack.getDamage(), secondaryInputStack.getDamage()) : primaryInputStack.getDamage();
                    int minDiff = minDamage - newDamage;

                    int levels = Math.round(minDiff / (float)primaryInputStack.getMaxDamage() * 4);

                    tempLevels += levels;
                }
            }
        }
        else {
            outputStack = ItemStack.EMPTY;
        }

        outputUsable = modified;

        output.setStack(0, outputStack);
        levelCost.set(RevampedTools.scaleLevelCostToGamerule(tempLevels));
        repairItemUsage = repairingUsage;

        ci.cancel();
    }
}
