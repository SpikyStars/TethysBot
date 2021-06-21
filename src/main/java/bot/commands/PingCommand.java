package bot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class PingCommand extends CommandBase {

    public CommandData createCommandData(){
        return new CommandData("ping", "Ping the bot");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        long time = System.currentTimeMillis();
        event.reply("Pong!").queue(response -> response.editOriginal("Pong! " + (System.currentTimeMillis() - time) + "ms").queue());
    }
}
