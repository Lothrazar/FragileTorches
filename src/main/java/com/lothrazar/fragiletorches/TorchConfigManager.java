package com.lothrazar.fragiletorches;

import java.util.List;
import com.google.common.collect.ImmutableList;
import com.lothrazar.library.util.StringParseUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;

public class TorchConfigManager {

  static ModConfigSpec CONFIG;
  private static ConfigValue<List<? extends String>> ENTITIESTRIGGER;
  public static DoubleValue DOUBLEVALUE;
  static {
    final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    BUILDER.comment("settings").push(ModFragileTorches.MODID);
    ENTITIESTRIGGER = BUILDER.comment("List of 'gentle' entities that will "
        + "never knock over any tagged torch.  For example, if you remove the entry for players, then players will knock over torches. " +
            "For example, add minecraft:player to the list to stop players from breaking torches ")
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

//  public TorchConfigManager() {
//    CONFIG.setConfig(setup(ModFragileTorches.MODID));
//  }

  public static boolean entityIsGentle(EntityType<?> type) {
    ResourceLocation ekey = BuiltInRegistries.ENTITY_TYPE.getKey(type);
    return StringParseUtil.isInList(ENTITIESTRIGGER.get(), ekey);
  }
}
