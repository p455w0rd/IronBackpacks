//package gr8pefish.ironbackpacks.playerDataSaving.saving;
//
//import com.google.common.collect.ImmutableMap;
//import com.google.common.collect.Sets;
//import gr8pefish.ironbackpacks.api.Constants;
//import gr8pefish.ironbackpacks.playerDataSaving.data.BackpackDataKey;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.nbt.NBTTagList;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.WorldSavedData;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.UUID;
//
//public class BackpackSaveData extends WorldSavedData {
//
//    public static final ResourceLocation BACKPACK_DATA_ID = new ResourceLocation(Constants.MODID, "playerData");
//
//    private Map<UUID, Set<BackpackDataKey>> playerData = new HashMap<UUID, Set<BackpackDataKey>>();
//
//    public BackpackSaveData(String id) {
//        super(id);
//    }
//
//    public BackpackSaveData() {
//        this(BACKPACK_DATA_ID.toString());
//    }
//
//    @Override
//    public void readFromNBT(NBTTagCompound tag) {
//        NBTTagList entries = tag.getTagList("playerData", 10);
//        for (int i = 0; i < entries.tagCount(); i++) {
//            NBTTagCompound loreTag = entries.getCompoundTagAt(i);
//            UUID uuid = UUID.fromString(loreTag.getString("uuid"));
//            Set<BackpackDataKey> mapValue = Sets.newHashSet();
//            NBTTagList loreEntries = loreTag.getTagList("loreEntries", 10);
//            for (int k = 0; k < loreEntries.tagCount(); k++)
//                mapValue.add(BackpackDataKey.deserialize(loreEntries.getCompoundTagAt(k)));
//
//            playerData.put(uuid, mapValue);
//        }
//    }
//
//    @Override
//    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
//        NBTTagList entries = new NBTTagList();
//        for (Map.Entry<UUID, Set<BackpackDataKey>> entry : playerData.entrySet()) {
//            NBTTagCompound loreTag = new NBTTagCompound();
//            loreTag.setString("uuid", entry.getKey().toString());
//            NBTTagList loreEntries = new NBTTagList();
//            for (BackpackDataKey loreKey : entry.getValue())
//                loreEntries.appendTag(loreKey.serialize());
//            loreTag.setTag("loreEntries", loreEntries);
//
//            entries.appendTag(loreTag);
//        }
//
//        tag.setTag("playerData", entries);
//
//        return tag;
//    }
//
//    public Set<BackpackDataKey> getDataForPlayer(EntityPlayer player) {
//        UUID uuid = player.getGameProfile().getId();
//        if (!playerData.containsKey(uuid))
//            initPlayer(player);
//        return playerData.get(uuid);
//    }
//
//    public boolean addData(EntityPlayer player, BackpackDataKey loreKey) {
//        Set<BackpackDataKey> playerData = getDataForPlayer(player);
//        if (playerData.contains(loreKey))
//            return false;
//
//        playerData.add(loreKey);
//        markDirty();
//        return true;
//    }
//
//    public void clearPlayer(EntityPlayer player) {
//        getDataForPlayer(player).clear();
//        markDirty();
//    }
//
//    public void initPlayer(EntityPlayer player) {
//        UUID playerId = player.getGameProfile().getId();
//        if (!playerData.containsKey(playerId))
//            playerData.put(playerId, Sets.<BackpackDataKey>newHashSet());
//    }
//
//    public Map<UUID, Set<BackpackDataKey>> getPlayerData() {
//        return ImmutableMap.copyOf(playerData);
//    }
//}
