package com.lothrazar.fragiletorches;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.IEventBus;

@Mod(ModFragileTorches.MODID)
public class ModFragileTorches {

  public static final String MODID = "fragiletorches";
  public static final Logger LOGGER = LogUtils.getLogger();
  /**
   * Data file path
   *
   * src/main/resources/data/fragiletorches/tags/blocks/fragile.json
   *
   * example contents
   *
   * <pre>
   {
      "replace": false,
      "values": [
      "minecraft:torch",
      "ceilingtorch:torch",
      "tconstruct:stone_torch",
      "minecraft:sugar_cane"
      ]
    }
   * </pre>
   */
  public static final String TAGID = ModFragileTorches.MODID + ":fragile";

  public ModFragileTorches(IEventBus modEventBus, ModContainer modContainer) {
    modContainer.registerConfig(ModConfig.Type.COMMON, TorchConfigManager.CONFIG);
    modEventBus.addListener(this::setup);
    NeoForge.EVENT_BUS.register(new FragTorchEvent());
  }

  private void setup(final FMLCommonSetupEvent event) {
    LOGGER.info("Fragile torges loaded targeting block data tag '" + TAGID + "'");
  }
}
