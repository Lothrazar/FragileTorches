package com.lothrazar.fragiletorches;

import com.lothrazar.fragiletorches.config.ConfigManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.BlockTags.Wrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FragTorchEvent {

  private static final ResourceLocation TAGRL = new ResourceLocation(ModFragileTorches.TAGID);
  private static final Wrapper TAGSTATE = new BlockTags.Wrapper(TAGRL);

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    Entity ent = event.getEntity();
    if (ent == null) {
      return;
    }
    World world = ent.world;
    if (ent.world.rand.nextDouble() > 0.01) {
      return;
    }
    //    private static final BlockTags.Wrapper SLEAVES = new BlockTags.Wrapper(ScytheType.LEAVES.type());
    BlockState bs = world.getBlockState(ent.getPosition());
    //    if (bs.getBlock() == Blocks.TORCH) {
    if (bs.isIn(TAGSTATE) && !ConfigManager.entityIsGentle(ent.getType())) {
      //player? 
      if (ent instanceof PlayerEntity) {
        //i am a player, i can avoid this
        PlayerEntity p = (PlayerEntity) ent;
        if (p.isSneaking()) {
          return;// ok // torches are safe from breaking as secret edge case for happiness
        }
      }
      //ok break the torch
      this.tryBreak(ent);
    }
  }

  private void tryBreak(Entity living) {
    living.world.destroyBlock(living.getPosition(), true);
  }
}
