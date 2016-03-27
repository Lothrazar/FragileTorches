package com.lothrazar.samsfragiletorches;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ModFragileTorches.MODID, useMetadata = true, updateJSON = "https://github.com/LothrazarMinecraftMods/FragileTorches/blob/master/update.json")
public class ModFragileTorches{

	public static final String MODID = "samsfragiletorches";

	@Instance(value = ModFragileTorches.MODID)
	public static ModFragileTorches instance;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event){

		MinecraftForge.EVENT_BUS.register(instance);
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event){
		Entity ent = event.getEntity();
		if(ent instanceof EntityLiving == false){
			return;
		}
		EntityLivingBase living = (EntityLivingBase) event.getEntity();
		if(living == null){
			return;
		}
		if(living.worldObj.getBlockState(living.getPosition()).getBlock() == Blocks.torch){
			float oddsWillBreak = 0.01F;// TODO: in config or something? or make this 1/100
			boolean playerCancelled = false;
			if(living instanceof EntityPlayer){
				EntityPlayer p = (EntityPlayer) living;
				if(p.isSneaking()){
					playerCancelled = true;// torches are safe from breaking
				}
			}

			if(playerCancelled == false // if its a player, then the player is not sneaking
					&& living.worldObj.rand.nextDouble() < oddsWillBreak && living.worldObj.isRemote == false){

				living.worldObj.destroyBlock(living.getPosition(), true);
			}
		}
	}
}
