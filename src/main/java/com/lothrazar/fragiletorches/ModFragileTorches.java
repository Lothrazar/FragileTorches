package com.lothrazar.fragiletorches;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModFragileTorches.MODID)
public class ModFragileTorches {

  public static final String MODID = "fragiletorches";
  public static final Logger LOGGER = LogManager.getLogger();
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

  public ModFragileTorches() {
    new TorchConfigManager();
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    MinecraftForge.EVENT_BUS.register(new FragTorchEvent());
  }

  private void setup(final FMLCommonSetupEvent event) {
    LOGGER.info("Fragile torges loaded targeting block data tag '" + TAGID + "'");
  }
}
