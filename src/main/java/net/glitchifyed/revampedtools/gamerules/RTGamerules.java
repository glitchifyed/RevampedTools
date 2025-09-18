package net.glitchifyed.revampedtools.config;

import static net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry.register;
import net.glitchifyed.revampedtools.RevampedTools;
import net.minecraft.world.GameRules;

import static net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory.createBooleanRule;
import static net.minecraft.world.GameRules.*;

public class RTGamerules {
    public static Key<BooleanRule> CONSISTENT_DRAGON_EXP;
    public static Key<BooleanRule> REBALANCED_EXP_LEVELS;

    public static void init() {
        CONSISTENT_DRAGON_EXP = register("consistentDragonExp", Category.DROPS, createBooleanRule(true));
        REBALANCED_EXP_LEVELS = register("rebalancedExpLevels", Category.PLAYER, createBooleanRule(true));
    }

    private static GameRules getGameRules() {
        return RevampedTools.SERVER.getGameRules();
    }

    public static boolean getBoolean(GameRules.Key<GameRules.BooleanRule> gamerule) {
        return getGameRules().getBoolean(gamerule);
    }

    public static Integer getInteger(GameRules.Key<GameRules.IntRule> gamerule) {
        return getGameRules().getInt(gamerule);
    }
}