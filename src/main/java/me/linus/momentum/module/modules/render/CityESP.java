package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.color.ColorPicker;
import me.linus.momentum.setting.mode.Mode;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.util.combat.EnemyUtil;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.render.builder.RenderBuilder.RenderMode;
import me.linus.momentum.util.world.WorldUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class CityESP extends Module {
    public CityESP() {
        super("CityESP", Category.RENDER, "Highlights blocks where nearby players can be citied");
    }

    public static Mode mode = new Mode("Mode", "Fill", "Outline", "Both", "Claw");
    public static Slider range = new Slider("Range", 0.0D, 12.0D, 20.0D, 0);

    public static Checkbox color = new Checkbox("Color", true);
    public static ColorPicker colorPicker = new ColorPicker(color, "Color Picker", new Color(255, 0, 0, 55));

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(range);
        addSetting(color);
    }

    List<BlockPos> cityBlocks = new ArrayList<>();

    @Override
    public void onUpdate() {
        cityBlocks.clear();

        WorldUtil.getNearbyPlayers(20).forEach(entityPlayer -> {
            EnemyUtil.getCityBlocks(entityPlayer, false).stream().filter(blockPos -> mc.player.getDistanceSq(blockPos) <= range.getValue()).forEach(blockPos -> {
                cityBlocks.add(blockPos);
            });
        });
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        if (nullCheck())
            return;

        for (BlockPos cityPos : cityBlocks) {
            switch (mode.getValue()) {
                case 0:
                    RenderUtil.drawBoxBlockPos(cityPos, 0, colorPicker.getColor(), RenderMode.Fill);
                    break;
                case 1:
                    RenderUtil.drawBoxBlockPos(cityPos, 0, colorPicker.getColor(), RenderMode.Outline);
                    break;
                case 2:
                    RenderUtil.drawBoxBlockPos(cityPos, 0, colorPicker.getColor(), RenderMode.Both);
                    break;
                case 3:
                    RenderUtil.drawBoxBlockPos(cityPos, 0, colorPicker.getColor(), RenderMode.Claw);
                    break;
            }
        }
    }
}