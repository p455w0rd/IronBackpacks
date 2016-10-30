//package gr8pefish.ironbackpacks.playerDataSaving.saving;
//
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.entity.player.InventoryPlayer;
//import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import net.minecraftforge.fml.common.gameevent.PlayerEvent;
//import net.minecraftforge.fml.common.gameevent.TickEvent;
//
//public class PlayerDataSavingEventHandler {
//
//    @SubscribeEvent
//    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
//        EntityPlayer player = event.player;
//        if (player instanceof EntityPlayerMP) {
//            LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageSyncLore((EntityPlayerMP) player), (EntityPlayerMP) player);
//            LoreUtil.checkDefaults(player);
//        }
//
//        if (LoreConfiguration.spawnWithJournal && !player.getEntityData().hasKey("loreexpansion-spawn")) {
//            GeneralUtil.giveStackToPlayer(player, new ItemStack(LoreExpansion.LORE_JOURNAL));
//            player.getEntityData().setBoolean("loreexpansion-spawn", true);
//        }
//    }
//
//    @SubscribeEvent
//    public void playerTick(TickEvent.PlayerTickEvent event) {
//        if (event.phase != TickEvent.Phase.START)
//            return;
//
//        if (event.player.getEntityWorld().isRemote || event.player.capabilities.isCreativeMode)
//            return;
//        InventoryPlayer inventoryPlayer = event.player.inventory;
//
//        for (int i = 0; i < inventoryPlayer.getSizeInventory(); i++) {
//            if (inventoryPlayer.getStackInSlot(i) != null && inventoryPlayer.getStackInSlot(i).getItem() == LoreExpansion.LORE_PAGE) {
//                Lore lore = LoreUtil.readLore(inventoryPlayer.getStackInSlot(i));
//                if (lore.shouldAutoAdd() && LoreUtil.provideLore(event.player, lore))
//                    inventoryPlayer.setInventorySlotContents(i, null);
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public void onItemPickup(EntityItemPickupEvent event) {
//        if (event.getEntityPlayer().getEntityWorld().isRemote || event.getEntityPlayer().capabilities.isCreativeMode)
//            return;
//        ItemStack stack = event.getItem().getEntityItem();
//
//        if (stack.getItem() == LoreExpansion.LORE_PAGE && stack.hasTagCompound()) {
//            Lore lore = LoreUtil.readLore(stack);
//            if (lore.shouldAutoAdd() && LoreUtil.provideLore(event.getEntityPlayer(), lore)) {
//                event.getItem().setDead();
//                event.setCanceled(true);
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
//        if (LoreConfiguration.syncLoresFromServer)
//            LoreExpansion.NETWORK_WRAPPER.sendTo(new MessageSyncLoreRegistry(Serializers.getStdGson().toJson(LoreLoader.LOADED_LORE)), (EntityPlayerMP) event.player);
//    }
//
//    @SubscribeEvent
//    @SideOnly(Side.CLIENT)
//    public void onLeaveServer(GuiScreenEvent.InitGuiEvent event) {
//        if (MessageSyncLoreRegistry.LORE_BACKUP == null)
//            return;
//
//        if (Minecraft.getMinecraft().theWorld != null)
//            return;
//
//        LoreLoader.registerLore(MessageSyncLoreRegistry.LORE_BACKUP, false);
//        MessageSyncLoreRegistry.LORE_BACKUP = null;
//    }
//}
