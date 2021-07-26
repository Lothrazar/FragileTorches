package com.lothrazar.fragiletorches;

import java.nio.file.Path;
import java.util.List;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.ImmutableList;
import com.lothrazar.fragiletorches.ModFragileTorches;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class ConfigManager {

  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
  private static ForgeConfigSpec COMMON_CONFIG;
  public static DoubleValue DOUBLEVALUE;
  static { 
    initConfig();
  }

  private static void initConfig() {
    COMMON_BUILDER.comment("settings").push(ModFragileTorches.MODID);
    entitiesTrigger = COMMON_BUILDER.comment("List of 'gentle' entities that will "
        + "never knock over any tagged torch.  For example, if you remove the entry for players, then players will knock over torches. ")
        .defineList("entities.gentle.list",
            ImmutableList.of(
                "minecraft:bat",
                "minecraft:boat",
                "minecraft:cat",
                "minecraft:ender_pearl",
                "minecraft:egg",
                "minecraft:horse",
                "minecraft:item",
                "minecraft:donkey",
                "minecraft:experience_orb",
                "minecraft:minecart",
                "minecraft:player",
                "minecraft:villager",
                "minecraft:wolf"),
            obj -> obj instanceof String);
    DOUBLEVALUE = COMMON_BUILDER.comment(
        "Chance that a torch will break during a collision (checks on a tick while entity is inside torch)")
        .defineInRange("chancetobreak",
            0.02000000000000F,
            0F, 1F);
    COMMON_BUILDER.pop();
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  private static ConfigValue<List<? extends String>> entitiesTrigger;

  public ConfigManager(Path path) {
    final CommentedFileConfig configData = CommentedFileConfig.builder(path)
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build();
    configData.load();
    COMMON_CONFIG.setConfig(configData);
  }

  public static boolean entityIsGentle(EntityType<?> type) {
    String thisType = type.getRegistryName().toString();
    for (String s : entitiesTrigger.get()) {
      if (s != null && s.equals(thisType)) {
        return true;
      }
    }
    return false;
  }
}
