package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.module.modules.hud.SystemModule;
import me.linus.momentum.module.modules.hud.TimeModule;
import me.linus.momentum.util.render.FontUtil;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class System extends HUDComponent<SystemModule> {
    public System() {
        super("System", 2, 500, SystemModule.INSTANCE);
        this.toggle();
    }

    @Override
    public void render() {
        final String time = new SimpleDateFormat("h:mm a").format(new Date());
        FontUtil.drawStringWithShadow(time, Momentum.componentManager.getComponentByName("System").getX(), Momentum.componentManager.getComponentByName("System").getY(), new Color(255, 255, 255).getRGB());
        width = (int) FontUtil.getStringWidth(time) + 2;
    }

    @Override
    public void mouseHovered(int mouseX, int mouseY) {
        if (isMouseOnComponent(mouseX, mouseY)) colors = new Color(82, 81, 77, 125).getRGB();
        else colors = new Color(117, 116, 110, 125).getRGB();
    }

    public boolean isMouseOnComponent(int x, int y) {
        if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height) {
            return true;
        }
        return false;
    }
}