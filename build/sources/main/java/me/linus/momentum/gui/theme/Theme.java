package me.linus.momentum.gui.theme;

import java.util.ArrayList;
import java.util.List;

import me.linus.momentum.gui.hud.HUDComponent;
import me.linus.momentum.gui.theme.themes.*;
import me.linus.momentum.module.Module;

/**
 * @author bon
 * @since 11/22/20
 */

public abstract class Theme {
	
	private String name;
	private int width;
	private int height;
	
	public Theme(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}
	
	public static final List<Theme> themes = new ArrayList<>();
	
	public abstract void updateColors();

	public abstract void drawTitles(String name, int left, int top);

	public abstract void drawModules(List<Module> modules, int left, int top);

	public abstract void drawHUDModules(List<HUDComponent> modules, int left, int top);
	
	/**
	 * Make sure to add any new themes you create in this arraylist.
	 */
	public static void initThemes() {
		themes.add(new DefaultTheme());
		themes.add(new DarkTheme());
	}
	
	public void addTheme(Theme theme) {
		themes.add(theme);
	}
	
	public List<Theme> getThemes(){
		return themes;
	}
	
	public String getThemeName() {
		return this.name;
	}
	
	public int getThemeWidth() {
		return this.width;
	}
	
	public int getThemeHeight() {
		return this.height;
	}
	
	public static Theme getTheme(int themeIndex) {
		return themes.get(themeIndex);
	}

}