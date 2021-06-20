package bot;

import bot.commands.CommandException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class DiscordListener extends ListenerAdapter {
    private static final Color ERROR_COLOR = Color.RED;

    @Override
    public void onSlashCommand(SlashCommandEvent event){
        try {
            BotMain.commands.get(event.getName()).execute(event);
        } catch (CommandException e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(e.getErrorTitle());
            embed.setDescription(e.getErrorDesc());
            embed.setColor(ERROR_COLOR);
            event.replyEmbeds(embed.build()).queue();
        }
    }
}
