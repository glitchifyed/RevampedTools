package net.glitchifyed.revampedtools.gamerules;

import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

import static net.glitchifyed.revampedtools.gamerules.RTGamerules.getGamerules;

public class IntegerGamerule {
    private final GameRules.Key<GameRules.IntRule> gamerule;

    public IntegerGamerule(String name, GameRules.Category category, Integer defaultValue) {
        gamerule = GameRuleRegistry.register(name, category, GameRuleFactory.createIntRule(defaultValue));
    }

    public IntegerGamerule(String name, CustomGameRuleCategory category, Integer defaultValue) {
        gamerule = GameRuleRegistry.register(name, category, GameRuleFactory.createIntRule(defaultValue));
    }

    public Integer get() {
        return getGamerules().getInt(gamerule);
    }
}
