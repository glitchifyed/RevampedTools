package net.glitchifyed.revampedtools.mixin;

import net.glitchifyed.revampedtools.anvil.ForgingScreenHandlerExtender;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ForgingScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ForgingScreenHandler.class)
public class ForgingScreenHandlerMixin implements ForgingScreenHandlerExtender {
    @Shadow @Final protected Inventory input;
    @Shadow @Final protected CraftingResultInventory output;

    @Unique @Override
    public Inventory revampedtools$getInput() {
        return input;
    }

    @Unique @Override
    public CraftingResultInventory revampedtools$getOutput() {
        return output;
    }
}
