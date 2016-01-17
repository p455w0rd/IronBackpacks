package gr8pefish.ironbackpacks.api.item.upgrades;

import gr8pefish.ironbackpacks.api.IronBackpacksAPI;
import gr8pefish.ironbackpacks.api.item.upgrades.interfaces.IPackUpgrade;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ItemPackUpgrade implements IPackUpgrade {

    private String name;
    private int upgradeCost;
    private String[] description;
    private String[] tooltip;

    public ItemPackUpgrade(String name, int upgradeCost, String[] description, String... tooltip){
        this.name = name;
        this.upgradeCost = upgradeCost;
        this.description = description;
        this.tooltip = tooltip;
    }

    public String[] getDescription(){
        return description;
    }

    public Item getItem(){
        return IronBackpacksAPI.getItem(IronBackpacksAPI.ITEM_CRAFTING_BASE);
    }

    public String getName(){
        return name;
    }

    @Override
    public String getName(ItemStack upgrade) {
        return name;
    }

    @Override
    public int getUpgradeCost(ItemStack upgrade) {
        return upgradeCost;
    }

    @Override
    public List<String> getTooltip(ItemStack upgrade) {
        return Arrays.asList(tooltip);
    }
}
