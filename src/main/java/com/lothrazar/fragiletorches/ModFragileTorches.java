package com.lothrazar.fragiletorches;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.fragiletorches.config.ConfigManager;
import com.lothrazar.fragiletorches.setup.ClientProxy;
import com.lothrazar.fragiletorches.setup.IProxy;
import com.lothrazar.fragiletorches.setup.ServerProxy;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.BlockTags.Wrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(ModFragileTorches.MODID)
public class ModFragileTorches {

  public static final String MODID = "fragiletorches";
  private String certificateFingerprint = "@FINGERPRINT@";
  public static ConfigManager config;
  public static final IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
  private static final Logger LOGGER = LogManager.getLogger();
  //the json file name in data/modid/tags/blocks/ that lists tag default
  private static final String TAGID = MODID + ":fragile";
  private static final ResourceLocation TAGRL = new ResourceLocation(TAGID);
  private static final Wrapper TAGSTATE = new BlockTags.Wrapper(TAGRL);

  public ModFragileTorches() {
    config = new ConfigManager(FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    MinecraftForge.EVENT_BUS.register(this);
  }

  private void setup(final FMLCommonSetupEvent event) {
    LOGGER.info("Fragile torges loaded targeting data tag '" + TAGID + "'");
    //    Blocks.TORCH.getTags() 
    boolean conts = Blocks.TORCH.getTags().contains(TAGRL);
    LOGGER.info("Vanilla torch tags: '", Blocks.TORCH.getTags());
    for (ResourceLocation t : Blocks.TORCH.getTags()) {
      //
      if (t.equals(TAGRL)) {
        conts = true;
        break;
      }
    }
    LOGGER.info("Vanilla torch has fragile tag: '" + conts + "'");
  }

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
    if (bs.isIn(TAGSTATE)) {
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
