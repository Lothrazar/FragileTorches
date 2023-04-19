package com.lothrazar.fragiletorches;

import java.util.List;
import com.google.common.collect.ImmutableList;
import com.lothrazar.library.config.ConfigTemplate;
import com.lothrazar.library.util.StringParseUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.registries.ForgeRegistries;

public class TorchConfigManager extends ConfigTemplate {

  private static ForgeConfigSpec CONFIG;
  private static ConfigValue<List<? extends String>> ENTITIESTRIGGER;
  public static DoubleValue DOUBLEVALUE;
  static {
    final ForgeConfigSpec.Builder BUILDER = builder();
    BUILDER.comment("settings").push(ModFragileTorches.MODID);
    ENTITIESTRIGGER = BUILDER.comment("List of 'gentle' entities that will "
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
    DOUBLEVALUE = BUILDER.comment(
        "Chance that a torch will break during a collision (checks on a tick while entity is inside torch)")
        .defineInRange("chancetobreak",
            0.02000000000000F,
            0F, 1F);
    BUILDER.pop();
    CONFIG = BUILDER.build();
  }

  public TorchConfigManager() {
    CONFIG.setConfig(setup(ModFragileTorches.MODID));
  }

  public static boolean entityIsGentle(EntityType<?> type) {
    ResourceLocation ekey = ForgeRegistries.ENTITY_TYPES.getKey(type);
    return StringParseUtil.isInList(ENTITIESTRIGGER.get(), ekey);
  }
}
