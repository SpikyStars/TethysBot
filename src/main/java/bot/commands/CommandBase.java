package bot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public abstract class CommandBase {
    public abstract void execute(SlashCommandEvent event) throws CommandException;
}
