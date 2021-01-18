package me.linus.momentum.gui.hud.components;

import me.linus.momentum.Momentum;
import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.ThemeColor;
import me.linus.momentum.module.modules.client.HUDEditor;
import me.linus.momentum.util.render.FontUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Time extends HUDComponent {
    public Time() {
        super("Time", 2, 35);
    }

    @Override
    public void renderComponent() {
        final String time = new SimpleDateFormat("h:mm a").format(new Date());
        FontUtil.drawString(time, this.x, this.y, HUDEditor.colorSync.getValue() ? ThemeColor.BRIGHT : -1);
        width = Momentum.fontManager.getCustomFont().getStringWidth(time) + 2;
    }
}
