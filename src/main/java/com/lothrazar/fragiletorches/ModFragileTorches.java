package com.lothrazar.fragiletorches;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

@Mod(modid = ModFragileTorches.MODID, useMetadata = true)
public class ModFragileTorches {

  public static final String MODID = "fragiletorches";
  public static float oddsWillBreak = 0.01F;
  @Instance(value = ModFragileTorches.MODID)
  public static ModFragileTorches instance;

  @EventHandler
  public void onPreInit(FMLPreInitializationEvent event) {
    //    cfg = Configuration(event.getSuggestedConfigurationFile());
    FMLCommonHandler.instance().bus().register(instance);
    MinecraftForge.EVENT_BUS.register(instance);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {}

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    World world = event.entityLiving.worldObj;
    int posX = (int) event.entityLiving.posX;
    int posY = (int) event.entityLiving.posY;
    int posZ = (int) event.entityLiving.posZ;
    if (world.getBlock(posX, posY, posZ) == Blocks.torch) {
      boolean playerCancelled = false;
      if (event.entityLiving instanceof EntityPlayer) {
        EntityPlayer p = (EntityPlayer) event.entityLiving;
        if (p.isSneaking()) {
          playerCancelled = true;// torches are safe from breaking
        }
      }
      if (playerCancelled == false // if its a player, then the player is not sneaking
          && world.rand.nextDouble() < oddsWillBreak && world.isRemote == false) {
        // func_147480_a//destroyBlock
        world.breakBlock(posX, posY, posZ, true);
      }
    }
  }
}
