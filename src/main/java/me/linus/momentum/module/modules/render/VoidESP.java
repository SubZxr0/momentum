package me.linus.momentum.module.modules.render;

import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import me.linus.momentum.util.render.RenderUtil;
import me.linus.momentum.util.world.BlockUtil;
import me.linus.momentum.util.world.HoleUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class VoidESP extends Module {
    public VoidESP() {
        super("VoidESP", Category.RENDER, "Highlights void holes");
    }

    public static Slider range = new Slider("Range", 0.0D, 12.0D, 20.0D, 0);
    public static Checkbox outline = new Checkbox("Outline", false);

    public static Checkbox color = new Checkbox("Color", true);
    public static SubSlider r = new SubSlider(color, "Red", 0.0D, 255.0D, 255.0D, 0);
    public static SubSlider g = new SubSlider(color, "Green", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider b = new SubSlider(color, "Blue", 0.0D, 0.0D, 255.0D, 0);
    public static SubSlider a = new SubSlider(color, "Alpha", 0.0D, 55.0D, 255.0D, 0);


    @Override
    public void setup() {
        addSetting(range);
        addSetting(outline);
        addSetting(color);
    }

    List<BlockPos> voidBlocks = new ArrayList<>();

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        voidBlocks.clear();

        BlockUtil.getNearbyBlocks(mc.player, range.getValue(), false).stream().filter(blockPos -> HoleUtil.isVoidHole(blockPos)).forEach(blockPos -> {
            voidBlocks.add(blockPos);
        });
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent eventRender) {
        for (BlockPos voidPos : voidBlocks) {
            RenderUtil.drawBoxBlockPos(voidPos, 0, new Color((int) r.getValue(), (int) g.getValue(), (int) b.getValue(), (int) a.getValue()));

            if (outline.getValue())
                RenderUtil.drawBoundingBoxBlockPos(voidPos, 0, new Color((int) r.getValue(), (int) g.getValue(), (int) b.getValue(), 144));
        }
    }
}
