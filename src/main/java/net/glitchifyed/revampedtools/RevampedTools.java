package net.glitchifyed.revampedtools;

import net.fabricmc.api.ModInitializer;
import net.glitchifyed.revampedtools.gamerules.RTGamerules;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RevampedTools implements ModInitializer {

    public static final String MODID = "revampedtools";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static MinecraftServer SERVER;

    @Override
    public void onInitialize() {
        RTGamerules.init();

        LOGGER.info("Initialized gamerules!");
    }

    public static boolean isItemStackEnabled(ItemStack stack) {
        return !RTGamerules.PERSISTENT_EQUIPMENT.get() || !stack.isDamageable() || stack.getDamage() < stack.getMaxDamage();
    }

    public static int scaleLevelCostToGamerule(int levels) {
        return levels;
    }

    public static void log(Object... toLog) {
        StringBuilder resultingLine = new StringBuilder();

        for (var obj : toLog) {
            if (!resultingLine.isEmpty()) {
                resultingLine.append(", ");
            }

            resultingLine.append(obj);
        }

        LOGGER.info(resultingLine.toString());
    }
}
