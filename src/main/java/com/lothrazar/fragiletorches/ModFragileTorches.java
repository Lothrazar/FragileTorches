package com.lothrazar.fragiletorches;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.fragiletorches.ConfigManager;
import com.lothrazar.fragiletorches.setup.ClientProxy;
import com.lothrazar.fragiletorches.setup.IProxy;
import com.lothrazar.fragiletorches.setup.ServerProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(ModFragileTorches.MODID)
public class ModFragileTorches {

  public static final String MODID = "fragiletorches";
  public static ConfigManager config;
  public static final IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
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
    config = new ConfigManager(FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    MinecraftForge.EVENT_BUS.register(new FragTorchEvent());
  }

  private void setup(final FMLCommonSetupEvent event) {
    LOGGER.info("Fragile torges loaded targeting block data tag '" + TAGID + "'");
  }
}
