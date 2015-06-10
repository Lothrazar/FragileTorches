package com.lothrazar.samsfragiletorches;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ModFragileTorches.MODID, useMetadata=true)
public class ModFragileTorches
{
    public static final String MODID = "samsfragiletorches";

	@Instance(value = ModFragileTorches.MODID)
	public static ModFragileTorches instance;
	
   	@EventHandler
   	public void onPreInit(FMLPreInitializationEvent event)
   	{ 
   		//cfg = new ConfigRegistry(new Configuration(event.getSuggestedConfigurationFile()));
    
   		FMLCommonHandler.instance().bus().register(instance); 
   		MinecraftForge.EVENT_BUS.register(instance); 
   	}
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
    
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) 
	{  
		if(event.entityLiving.worldObj.getBlockState(event.entityLiving.getPosition()).getBlock() == Blocks.torch) 
		{ 
			float oddsWillBreak = 0.01F;//TODO: in config or something? or make this 1/100
			boolean playerCancelled = false;
			if(event.entityLiving instanceof EntityPlayer)
			{
				EntityPlayer p = (EntityPlayer)event.entityLiving;
				if(p.isSneaking())
				{
					playerCancelled = true;//torches are safe from breaking
				}
			}
			
			if(playerCancelled == false //if its a player, then the player is not sneaking
					&& event.entityLiving.worldObj.rand.nextDouble() < oddsWillBreak
					&& event.entityLiving.worldObj.isRemote == false)
			{ 
				event.entityLiving.worldObj.destroyBlock(event.entityLiving.getPosition(), true);  
			}
		}
	}

}
