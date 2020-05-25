package com.lothrazar.fragiletorches;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModFragileTorches.MODID, certificateFingerprint = "@FINGERPRINT@")
public class ModFragileTorches {

  public static final String MODID = "fragiletorches";
  public static final Logger LOGGER = LogManager.getLogger();

  @EventHandler
  public void onPreInit(FMLPreInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(new FragTorchEvent());
  }
}
