package gr8pefish.ironbackpacks.api.item.backpacks.abstractClasses;

import gr8pefish.ironbackpacks.api.crafting.IIncreaseBackpackTierRecipe;
import gr8pefish.ironbackpacks.api.crafting.IRemoveUpgradeRecipe;
import gr8pefish.ironbackpacks.api.item.backpacks.interfaces.ITieredBackpack;
import gr8pefish.ironbackpacks.api.item.backpacks.interfaces.IUpgradableBackpack;
import gr8pefish.ironbackpacks.api.crafting.IAddUpgradeRecipe;

/**
 * To use for backpacks that are both tiered and upgradable.
 */
public abstract class AbstractUpgradableTieredBackpack implements IUpgradableBackpack, ITieredBackpack, IAddUpgradeRecipe, IRemoveUpgradeRecipe, IIncreaseBackpackTierRecipe {

}
