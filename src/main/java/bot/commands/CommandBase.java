package bot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class CommandBase {
    public abstract CommandData createCommandData();
    public abstract void execute(SlashCommandEvent event) throws CommandException;
}
