package net.glitchifyed.revampedtools.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

import static net.glitchifyed.revampedtools.gamerules.RTGamerules.getGamerules;

public class BooleanGamerule {
    private final GameRules.Key<GameRules.BooleanRule> gamerule;

    public BooleanGamerule(String name, GameRules.Category category, boolean defaultValue) {
        gamerule = GameRuleRegistry.register(name, category, GameRuleFactory.createBooleanRule(defaultValue));
    }

    public BooleanGamerule(String name, CustomGameRuleCategory category, boolean defaultValue) {
        gamerule = GameRuleRegistry.register(name, category, GameRuleFactory.createBooleanRule(defaultValue));
    }

    public boolean get() {
        return getGamerules().getBoolean(gamerule);
    }
}
