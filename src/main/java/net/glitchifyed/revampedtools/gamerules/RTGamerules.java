package net.glitchifyed.revampedtools.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.glitchifyed.revampedtools.RevampedTools;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

import static net.minecraft.world.GameRules.*;

public class RTGamerules {
    public static CustomGameRuleCategory MOD_CATEGORY;

    public static BooleanGamerule CONSISTENT_DRAGON_EXP;
    public static BooleanGamerule REBALANCED_EXP_LEVELS;
    public static BooleanGamerule REWORKED_ANVILS;
    public static BooleanGamerule PERSISTENT_EQUIPMENT;
    public static BooleanGamerule DISPLAY_DURABILITY;

    public static IntegerGamerule MAX_UNBREAKING_FACTOR;

    public static void init() {
        MOD_CATEGORY = new CustomGameRuleCategory(Identifier.of("revampedtools:gamerules"), Text.translatable("gamerule.category.revampedtools").formatted(Formatting.BOLD).formatted(Formatting.YELLOW));

        CONSISTENT_DRAGON_EXP = new BooleanGamerule("rtConsistentDragonExp", MOD_CATEGORY, true);
        REBALANCED_EXP_LEVELS = new BooleanGamerule("rtExpLevels", MOD_CATEGORY, true);
        REWORKED_ANVILS = new BooleanGamerule("rtAnvils", MOD_CATEGORY, true);
        PERSISTENT_EQUIPMENT = new BooleanGamerule("rtPersistentEquipment", MOD_CATEGORY, true);
        DISPLAY_DURABILITY = new BooleanGamerule("rtDisplayItemDurability", MOD_CATEGORY, true);

        MAX_UNBREAKING_FACTOR = new IntegerGamerule("rtMaxUnbreakingFactor", MOD_CATEGORY, 20);
    }

    public static GameRules getGamerules() {
        return RevampedTools.SERVER.getGameRules();
    }
}