package net.glitchifyed.revampedtools.mixin;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToolMaterial.class)
public class ToolMaterialMixin {
    @Mutable @Shadow @Final public static ToolMaterial GOLD;
    @Mutable @Shadow @Final public static ToolMaterial DIAMOND;
    @Mutable @Shadow @Final public static ToolMaterial NETHERITE;
    @Mutable @Shadow @Final public static ToolMaterial IRON;
    @Mutable @Shadow @Final public static ToolMaterial STONE;
    @Mutable @Shadow @Final public static ToolMaterial WOOD;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyTools(CallbackInfo ci) {
        WOOD = new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 100, 2, 0, 1, ItemTags.WOODEN_TOOL_MATERIALS);
        STONE = new ToolMaterial(BlockTags.INCORRECT_FOR_STONE_TOOL, 700, 5, 1, 5, ItemTags.STONE_TOOL_MATERIALS);
        IRON = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 500, 10, 3, 10, ItemTags.IRON_TOOL_MATERIALS);
        GOLD = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 200, 20, 5, 50, ItemTags.GOLD_TOOL_MATERIALS);
        DIAMOND = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 3000, 8, 2, 20, ItemTags.DIAMOND_TOOL_MATERIALS);
        NETHERITE = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 8000, 10, 3, 50, ItemTags.NETHERITE_TOOL_MATERIALS);
    }
}