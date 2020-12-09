package me.linus.momentum.command.commands;

import baritone.api.BaritoneAPI;
import me.linus.momentum.command.Command;

public class Cancel extends Command {
    public Cancel() {
        super("Cancel", new String[]{"cancel"});
    }

    @Override
    public void onCommand(String[] args) {
            try {
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}