package me.linus.momentum.util.world;

import com.mojang.authlib.GameProfile;
import me.linus.momentum.managers.social.friend.FriendManager;
import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.module.Module;
import me.linus.momentum.util.combat.EnemyUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 12/26/2020
 */

public class WorldUtil implements MixinInterface {

    public static void createFakePlayer(@Nullable String name, boolean copyInventory, boolean copyAngles, boolean health, boolean player, BlockPos position) {
        EntityOtherPlayerMP entity = player ? new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile()) : new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("70ee432d-0a96-4137-a2c0-37cc9df67f03"), name));
        entity.copyLocationAndAnglesFrom(mc.player);

        if (copyInventory)
            entity.inventory.copyInventory(mc.player.inventory);

        if (copyAngles) {
            entity.rotationYaw = mc.player.rotationYaw;
            entity.rotationYawHead = mc.player.rotationYawHead;
        }

        if (health)
            entity.setHealth(mc.player.getHealth() + mc.player.getAbsorptionAmount());

        mc.world.addEntityToWorld(69420, entity);
    }

    public static EntityPlayer getClosestPlayer(double range) {
        if (mc.world.getLoadedEntityList().size() == 0)
            return null;

        EntityPlayer closestPlayer = mc.world.playerEntities.stream().filter(entityPlayer -> mc.player != entityPlayer).filter(entityPlayer -> mc.player.getDistance(entityPlayer) <= range).filter(entityPlayer -> !entityPlayer.isDead).findFirst().orElse(null);

        if (FriendManager.isFriend(closestPlayer.getName()) && FriendManager.isFriendModuleEnabled())
            return null;

        return closestPlayer;
    }

    public static EntityPlayer getTarget(double range, int mode) {
        if (mc.world.getLoadedEntityList().size() == 0)
            return null;

        EntityPlayer crystalPlayer = null;

        switch (mode) {
            case 0:
                crystalPlayer = mc.world.playerEntities.stream().filter(entityPlayer -> mc.player != entityPlayer).filter(entityPlayer -> mc.player.getDistance(entityPlayer) <= range).filter(entityPlayer -> !entityPlayer.isDead).findFirst().orElse(null);
                break;
            case 1:
                crystalPlayer = mc.world.playerEntities.stream().filter(entityPlayer -> mc.player != entityPlayer).filter(entityPlayer -> mc.player.getDistance(entityPlayer) <= range).filter(entityPlayer -> !entityPlayer.isDead).min(Comparator.comparing(entityPlayer -> EnemyUtil.getHealth(entityPlayer))).orElse(null);
                break;
            case 2:
                crystalPlayer = mc.world.playerEntities.stream().filter(entityPlayer -> mc.player != entityPlayer).filter(entityPlayer -> mc.player.getDistance(entityPlayer) <= range).filter(entityPlayer -> !entityPlayer.isDead).min(Comparator.comparing(entityPlayer -> EnemyUtil.getArmor(entityPlayer))).orElse(null);
                break;
        }

        if (FriendManager.isFriend(crystalPlayer.getName()) && FriendManager.isFriendModuleEnabled())
            return null;

        return crystalPlayer;
    }

    public static List<EntityPlayer> getNearbyPlayers(double range) {
        if (mc.world.getLoadedEntityList().size() == 0)
            return null;

        List<EntityPlayer> nearbyPlayers = mc.world.playerEntities.stream().filter(entityPlayer -> mc.player != entityPlayer).filter(entityPlayer -> mc.player.getDistance(entityPlayer) <= range).filter(entityPlayer -> !entityPlayer.isDead).collect(Collectors.toList());

        for (EntityPlayer closestPlayer : nearbyPlayers)
            if (FriendManager.isFriend(closestPlayer.getName()) && FriendManager.isFriendModuleEnabled())
                nearbyPlayers.remove(closestPlayer);

        return nearbyPlayers;
    }

    public static List<EntityPlayer> getNearbyTargets(double range, int mode) {
        if (mc.world.getLoadedEntityList().size() == 0)
            return null;

        List<EntityPlayer> nearbyTargets = null;

        switch (mode) {
            case 0:
                nearbyTargets = getNearbyPlayers(range);
                break;
            case 1:
                nearbyTargets = getNearbyPlayers(range).stream().sorted(Comparator.comparing(entityPlayer -> EnemyUtil.getHealth(entityPlayer))).collect(Collectors.toList());
                break;
            case 2:
                nearbyTargets = getNearbyPlayers(range).stream().sorted(Comparator.comparing(entityPlayer -> EnemyUtil.getArmor(entityPlayer))).collect(Collectors.toList());
                break;
        }

        for (EntityPlayer closestPlayer : nearbyTargets)
            if (FriendManager.isFriend(closestPlayer.getName()) && FriendManager.isFriendModuleEnabled())
                nearbyTargets.remove(closestPlayer);

        return nearbyTargets;
    }

    public static List<EntityPlayer> getNearbyFriends(double range) {
        if (mc.world.getLoadedEntityList().size() == 0)
            return null;

        List<EntityPlayer> nearbyFriends = mc.world.playerEntities.stream().filter(entityPlayer -> mc.player != entityPlayer).filter(entityPlayer -> mc.player.getDistance(entityPlayer) <= range).filter(entityPlayer -> !entityPlayer.isDead).filter(entityPlayer -> FriendManager.isFriend(entityPlayer.getName())).collect(Collectors.toList());
        return nearbyFriends;
    }

    public static void disconnectFromWorld(Module module) {
        module.disable();
        mc.world.sendQuittingDisconnectingPacket();
        mc.loadWorld(null);
        mc.displayGuiScreen(new GuiMainMenu());
    }
}
