package main.ironbackpacks.container.backpack;

import codechicken.enderstorage.storage.item.ItemEnderPouch;
import invtweaks.api.container.ChestContainer;
import main.ironbackpacks.IronBackpacks;
import main.ironbackpacks.container.slot.AdvancedNestingBackpackSlot;
import main.ironbackpacks.container.slot.BackpackSlot;
import main.ironbackpacks.container.slot.NestingBackpackSlot;
import main.ironbackpacks.integration.InterModSupport;
import main.ironbackpacks.items.backpacks.BackpackTypes;
import main.ironbackpacks.items.backpacks.IBackpack;
import main.ironbackpacks.items.upgrades.UpgradeMethods;
import main.ironbackpacks.util.IronBackpacksHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The container of the backpack when it is opened normally.
 */
@ChestContainer //Inventory tweaks
public class ContainerBackpack extends Container {

    private EntityPlayer player; //the player
    private InventoryBackpack inventory; //the inventory
    private BackpackTypes type; //the type of backpack
    private int xSize = 0; //the x size
    private int ySize = 0; //the y size

    public ContainerBackpack(EntityPlayer entityPlayer, InventoryBackpack backpackInventory, BackpackTypes type, int xSize, int ySize){
        this.player = entityPlayer;
        this.inventory = backpackInventory;
        this.type = type;
        this.xSize = xSize;
        this.ySize = ySize;
        layoutContainer(entityPlayer.inventory, backpackInventory, xSize, ySize, type);
    }

    //overloaded constructor for when size is irrelevant
    public ContainerBackpack(EntityPlayer entityPlayer, InventoryBackpack backpackInventory, BackpackTypes type){
        this.player = entityPlayer;
        this.inventory = backpackInventory;
        this.type = type;
        layoutContainer(entityPlayer.inventory, backpackInventory, xSize, ySize, type);
    }

    public EntityPlayer getPlayer() {
        return player;
    }
    public InventoryBackpack getInventoryBackpack() {
        return inventory;
    }
    public BackpackTypes getType() {
        return type;
    }

    /**
     * Adds the slots to the continer.
     * @param playerInventory - the player's inventory
     * @param backpackInventory - the backpack inventory
     * @param xSize - the x location
     * @param ySize - the y location
     * @param type - the backpack's type
     */
    //credit to cpw here for basic layout of adding backpack's slots
    protected void layoutContainer(IInventory playerInventory, IInventory backpackInventory, int xSize, int ySize, BackpackTypes type){

        //adds chest's slots
        ItemStack baseBackpack = IronBackpacksHelper.getBackpack(player);
        int[] upgrades = IronBackpacksHelper.getUpgradesAppliedFromNBT(baseBackpack);

        for (int backpackRow = 0; backpackRow < type.getRowCount(); backpackRow++) {
            for (int backpackCol = 0; backpackCol < type.getRowLength(); backpackCol++) {
                if (UpgradeMethods.hasNestingUpgrade(upgrades)){
                    addSlotToContainer(new NestingBackpackSlot(backpackInventory, backpackCol + backpackRow * type.getRowLength(), 20 + backpackCol * 18, 18 + backpackRow * 18, this.type));
                }else if (UpgradeMethods.hasNestingAdvancedUpgrade(upgrades)) {
                    addSlotToContainer(new AdvancedNestingBackpackSlot(backpackInventory, backpackCol + backpackRow * type.getRowLength(), 20 + backpackCol * 18, 18 + backpackRow * 18, this.type));
                }else{
                    addSlotToContainer(new BackpackSlot(backpackInventory, backpackCol + backpackRow * type.getRowLength(), 20 + backpackCol * 18, 18 + backpackRow * 18));
                }
            }
        }

        //adds player's inventory
        int leftCol = (xSize - 162) / 2 + 1;
        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++){
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++){
                addSlotToContainer(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, ySize - (4 - playerInvRow) * 18 - 10));
            }

        }

        //adds player's hotbar
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++){
            addSlotToContainer(new Slot(playerInventory, hotbarSlot, leftCol + hotbarSlot * 18, ySize - 24));
        }
    }

    @Override //copied from IronChests
    public ItemStack transferStackInSlot(EntityPlayer p, int i){
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(i);
        if (slot != null && slot.getHasStack()){
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i < type.getSize()){ //if clicking from backpack to player
                if (!mergeItemStack(itemstack1, type.getSize(), inventorySlots.size(), true))
                    return null;
            } else if (!((BackpackSlot) inventorySlots.get(1)).acceptsStack(itemstack1))
                return null;
            else if (!mergeItemStack(itemstack1, 0, type.getSize(), false))
                return null;
            if (itemstack1.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();
        }
        return itemstack;
    }

    /**
     * Checks if the item can be put into the backpack, for use with the filter upgrade
     * @param itemToPutInBackpack - the itemstack to put in
     * @return - the remaining itemstack (null if it has been put it, the remaining otherwise)
     */
    public ItemStack transferStackInSlot(ItemStack itemToPutInBackpack){ //TODO: have to check not the currPack, so you can't put curr pack in itself
        if (!mergeItemStack(itemToPutInBackpack, 0, type.getSize(), false)) //stack, startIndex, endIndex, flag
            return null;
        else if (!((BackpackSlot) inventorySlots.get(1)).acceptsStack(itemToPutInBackpack)) //slot 1 is always a backpackSlot
            return null;
        return itemToPutInBackpack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!player.worldObj.isRemote) //server side
            this.inventory.onGuiSaved(player);
    }

    @Override
    public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player) { //TODO: inter-mod backpack (IInv) support with right click
        // this will prevent the player from interacting with the item that opened the inventory:
        ItemStack currPack = IronBackpacks.proxy.getCurrBackpack(player);
        if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getHasStack() && ItemStack.areItemStacksEqual(getSlot(slot).getStack(), currPack) && button == 0) {
            return null;
        }else if (button == 1 && slot >= 0 && getSlot(slot) != null && getSlot(slot).getHasStack()){ //right click on non-empty slot
            if(getSlot(slot).getStack().getItem() instanceof IBackpack) { //has to be a backpack

                ItemStack stack = getSlot(slot).getStack();

                if (!ItemStack.areItemStackTagsEqual(stack, IronBackpacksHelper.getBackpack(player))) //can't right click the same backpack you have open, causes it to not update correctly and dupe items
                    stack.useItemRightClick(player.worldObj, player);
                return null;

            }else if(InterModSupport.isEnderStorageLoaded && getSlot(slot).getStack().getItem() instanceof ItemEnderPouch) {
                ItemStack stack = getSlot(slot).getStack();
                stack.useItemRightClick(player.worldObj, player);
                return null;
            }
        }
        return super.slotClick(slot, button, flag, player);
    }

    //======================================================================HELPER METHODS=========================================================================

    /**
     * Saves via updating the inventory.
     * @param player - the player
     */
    public void save(EntityPlayer player) {
        if (!player.worldObj.isRemote) { //server side
            this.inventory.onGuiSaved(player);
        }
    }

    /**
     * Moves items from the backpack to the player's inventory
     */
    public void backpackToInventory(){
        for (int i = 0; i <= type.getSize()-1; i++) {
            transferStackInSlot(player, i);
        }
    }

    /**
     * Moves items from the player's inventory to the backpack
     */
    public void inventoryToBackpack(){
        int start = type.getSize();
        int end = start + player.inventory.getSizeInventory() - player.inventory.getHotbarSize() - 4; //Not sure why it is 4 too large...
        for (int i = start; i < end; i++){
            transferStackInSlot(player, i);
        }
    }

    /**
     * Moves items from the player's hotbar to the backpack
     */
    public void hotbarToBackpack(){
        int start = type.getSize() + player.inventory.getSizeInventory() - player.inventory.getHotbarSize() - 4;
        int end = start + this.player.inventory.getHotbarSize();

        ItemStack equippedPack = IronBackpacksHelper.getBackpack(player); //access once here instead of in the loop

        for (int slot = start; slot < end; slot++){ //for each slot in hotbar
            if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getHasStack()) { //non-empty slot
                if (!(getSlot(slot).getStack().getItem() instanceof IBackpack)) { //not a backpack
                    transferStackInSlot(player, slot); //transfer it
                } else {
                    ItemStack stack = getSlot(slot).getStack();
                    System.out.println(equippedPack.getDisplayName());
                    if (!ItemStack.areItemStackTagsEqual(stack, equippedPack)) { //can't move the same backpack you have open
                        transferStackInSlot(player, slot);
                    }
                }

            }
        }
    }

    @ChestContainer.RowSizeCallback //Inventory tweaks compatibility
    public int getNumColumns(){
        return type.getRowLength();
    }

    @ChestContainer.IsLargeCallback //Inventory tweaks compatibility
    public boolean getVerticalButtons(){
        return false;
    }

    //===================================================================Sorting Algorithm==================================================================

    //TODO: comment more

    /**
     * Sorts the backpack by merging the stacks that can be, swapping out empty spaced to condense the pack, and then reorders via alphabetization of the stacks' display names.
     */
    public void sort(){
        if (!inventorySlots.isEmpty() && !inventoryItemStacks.isEmpty()){
            mergeStacks(); //merge all the items together into the least possible number of stacks
            swapStacks();  //then swap out the null stacks/blank spaces so that the backpack's stacks with items are condensed into the smallest area possible
            reorderStacks(); //finally reorder the stacks (I am using alphabetization by the stack's display names).
        }
    }

    private void mergeStacks(){
        for (int i = 0; i < type.getSize(); i++){
            Slot tempSlot = (Slot) inventorySlots.get(i);
            if (tempSlot!= null && tempSlot.getHasStack()){
                ItemStack tempStack = tempSlot.getStack();
                if (tempStack!= null && tempStack.stackSize < tempStack.getMaxStackSize()){
                    fillSlot(tempSlot, i+1);
                }
            }
        }
    }

    private void fillSlot(Slot slotToFill, int startIndex){
        ItemStack stackToFill = slotToFill.getStack();
        int fillAmt = stackToFill.getMaxStackSize() - stackToFill.stackSize;
        if (fillAmt > 0){
            for (int i = startIndex; i < type.getSize(); i++){
                stackToFill = slotToFill.getStack();
                fillAmt = stackToFill.getMaxStackSize() - stackToFill.stackSize;
                Slot tempSlot = (Slot) inventorySlots.get(i);
                if (tempSlot != null && tempSlot.getHasStack()){
                    ItemStack tempStack = tempSlot.getStack();
                    if (tempStack.stackSize > 0 && tempStack.isItemEqual(stackToFill) && ItemStack.areItemStackTagsEqual(tempStack, stackToFill)){
                        if (tempStack.stackSize > fillAmt){
                            tempSlot.decrStackSize(fillAmt);
                            slotToFill.putStack(new ItemStack(stackToFill.getItem(), stackToFill.getMaxStackSize(), stackToFill.getItemDamage()));
                            slotToFill.onSlotChanged();
                            break;
                        }else{
                            tempSlot.putStack(null);
                            slotToFill.putStack(new ItemStack(stackToFill.getItem(), stackToFill.stackSize + tempStack.stackSize, stackToFill.getItemDamage()));
                            slotToFill.onSlotChanged();
                        }
                    }
                }
            }
        }
    }

    private void swapStacks(){
        ArrayList<Integer> indicesOfSlotsWithItems = new ArrayList<Integer>();
        for (int i = 0; i < type.getSize(); i++){
            Slot tempSlot = (Slot) inventorySlots.get(i);
            if (tempSlot!= null && tempSlot.getHasStack()){
                ItemStack tempStack = tempSlot.getStack();
                if (tempStack != null && tempStack.stackSize > 0){
                    indicesOfSlotsWithItems.add(i);
                }
            }
        }
        if (!indicesOfSlotsWithItems.isEmpty()) {
            if (indicesOfSlotsWithItems.get(indicesOfSlotsWithItems.size() - 1) != (indicesOfSlotsWithItems.size() - 1)) { //if not already swapped so no null slots,
                for (int i = 0; i < indicesOfSlotsWithItems.size(); i++) {
                    Slot tempSlot = (Slot) inventorySlots.get(i);
                    if (tempSlot != null) {
                        if (!tempSlot.getHasStack()) {
                            swapNull(tempSlot, (Slot) inventorySlots.get(indicesOfSlotsWithItems.get(i)));
                        }
                    }
                }
            }
        }
    }

    private void swapNull(Slot nullSlot, Slot stackSlot){
        if (stackSlot != null && stackSlot.getHasStack()) {
            nullSlot.putStack(stackSlot.getStack());
            stackSlot.putStack(null);
            stackSlot.onSlotChanged();
        }
    }

    private void reorderStacks(){
        ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>(countLengthOfStacks());
        for (int i = 0; i < type.getSize(); i++){
            Slot tempSlot = (Slot) inventorySlots.get(i);
            if (tempSlot != null && tempSlot.getHasStack()) {
                itemStacks.add(tempSlot.getStack());
            } else {
                break;
            }
        }
        if (!itemStacks.isEmpty()) {
//            Collections.sort(itemStacks, new ItemStackNameComparator()); //TODO ,add with options
            Collections.sort(itemStacks, new ItemStackIDComparator());
            for (int i = 0; i < itemStacks.size(); i++) {
                Slot tempSlot = (Slot) inventorySlots.get(i);
                tempSlot.putStack(itemStacks.get(i));
            }
        }
    }

    private class ItemStackNameComparator implements Comparator<ItemStack> {
        @Override
        public int compare(ItemStack stack1, ItemStack stack2) {
            return stack1.getDisplayName().compareToIgnoreCase(stack2.getDisplayName());
        }
    }

    private class ItemStackIDComparator implements Comparator<ItemStack> {
        @Override
        public int compare(ItemStack stack1, ItemStack stack2) {
            int item1ID;
            int item2ID;
            if (stack1.getItem() instanceof IBackpack)
                item1ID = -100;
            else
                item1ID = Item.getIdFromItem(stack1.getItem());
            if (stack2.getItem() instanceof IBackpack)
                item2ID = -100;
            else
                item2ID = Item.getIdFromItem(stack2.getItem());
            if (item1ID == item2ID) return 0;
            return (item1ID > item2ID) ? 1 : -1;
        }
    }

    private int countLengthOfStacks(){
        int total = 0;
        for (int i = 0; i < type.getSize(); i++){
            Slot tempSlot = (Slot) inventorySlots.get(i);
            if (tempSlot != null && tempSlot.getHasStack()) {
                total++;
            } else {
                return total;
            }
        }
        return total;
    }

}
