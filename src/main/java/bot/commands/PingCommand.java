package bot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class PingCommand extends CommandBase {

    @Override
    public void execute(SlashCommandEvent event) {
        long time = System.currentTimeMillis();
        event.reply("Pong!").queue(response -> response.editOriginal("Pong! " + (System.currentTimeMillis() - time) + "ms").queue());
    }
}
