package me.linus.momentum.util.combat;

import me.linus.momentum.mixin.MixinInterface;
import me.linus.momentum.util.world.BlockUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linustouchtips
 * @since 11/24/2020
 */

public class CrystalUtil implements MixinInterface {

    public static List<BlockPos> crystalBlocks(EntityPlayer entityPlayer, double placeRange, boolean prediction, int blockCalc) {
        return BlockUtil.getNearbyBlocks(entityPlayer, placeRange, prediction).stream().filter(blockCalc == 0 ? CrystalUtil::canPlaceCrystal : CrystalUtil::canPlaceThirteenCrystal).collect(Collectors.toList());
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        if (mc.player.capabilities.isCreativeMode)
            return 0;

        double factor = (1.0 - entity.getDistance(posX, posY, posZ) / 12.0f) * entity.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());
        float calculatedDamage = (float) (int) ((factor * factor + factor) / 2.0 * 7.0 * 12.0f + 1.0);
        double damage = 1.0;

        if (entity instanceof EntityLivingBase)
            damage = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(calculatedDamage), new Explosion(mc.world, null, posX, posY, posZ, 6.0f, false, true));

        return (float) damage;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            damage *= 1.0f - MathHelper.clamp((float) EnchantmentHelper.getEnchantmentModifierDamage(entity.getArmorInventoryList(), DamageSource.causeExplosionDamage(explosion)), 0.0f, 20.0f) / 25.0f;

            if (entity.isPotionActive(Potion.getPotionById(11)))
                damage -= damage / 4.0f;

            return damage;
        }

        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private static float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getDifficultyId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }

    public static boolean canBlockBeSeen(BlockPos blockPos) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), false, true, false) == null;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static void attackCrystal(EntityEnderCrystal crystal, boolean packet) {
        if (packet)
            mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
        else
            mc.playerController.attackEntity(mc.player, crystal);
    }

    public static boolean attackCheck(Entity crystal, int mode, double breakRange, List<BlockPos> placedCrystals) {
        if (!(crystal instanceof EntityEnderCrystal))
            return false;

        switch (mode) {
            case 0:
                return true;
            case 1:
                for (BlockPos placePos : new ArrayList<>(placedCrystals)) {
                    if (placePos != null && placePos.getDistance((int) crystal.posX, (int) crystal.posY, (int) crystal.posZ) <= breakRange)
                        return true;
                }
        }

        return false;
    }

    public static void swingArm(int mode) {
        switch (mode) {
            case 0:
                mc.player.swingArm(EnumHand.MAIN_HAND);
            case 1:
                if (!mc.player.getHeldItemOffhand().isEmpty())
                    mc.player.swingArm(EnumHand.OFF_HAND);
            case 2:
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.OFF_HAND);
            case 3:
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    public static void placeCrystal(BlockPos placePos, EnumFacing enumFacing, boolean packet) {
        if (packet)
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placePos, enumFacing, mc.player.getHeldItemOffhand().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        else
            mc.playerController.processRightClickBlock(mc.player, mc.world, placePos, enumFacing, new Vec3d(0, 0, 0), mc.player.getHeldItemOffhand().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
    }

    public static EnumFacing getEnumFacing(boolean rayTrace, BlockPos finalPos) {
        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(finalPos.getX() + 0.5, finalPos.getY() - 0.5, finalPos.getZ() + 0.5));
        EnumFacing enumFacing = null;

        if (rayTrace) {
            if (result == null || result.sideHit == null)
                return null;

            else
                enumFacing = result.sideHit;
        }

        return enumFacing;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos) {
        return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.AIR && mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() == Blocks.AIR && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos.add(0, 1, 0))).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos.add(0, 2, 0))).isEmpty();
    }

    public static boolean canPlaceThirteenCrystal(BlockPos blockPos) {
        return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos.add(0, 1, 0))).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos.add(0, 2, 0))).isEmpty();
    }
}
