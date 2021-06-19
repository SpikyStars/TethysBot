package bot;

import bot.commands.Command;
import bot.commands.CommandException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class DiscordListener extends ListenerAdapter {
    private static final Color ERROR_COLOR = Color.RED;

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if (content.startsWith(BotMain.PREFIX)){
            String commandText = content.substring(BotMain.PREFIX.length());
            String commandKey = commandText.split(" ")[0].toLowerCase().strip();
            Command command = BotMain.commands.get(commandKey);
            String params = commandText.substring(commandKey.length()).strip();
            if (command != null){
                try {
                    command.execute(event, params);
                } catch (CommandException e){
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle(e.getErrorTitle());
                    embed.setDescription(e.getErrorDesc());
                    embed.setColor(ERROR_COLOR);
                    message.getChannel().sendMessage(embed.build()).queue();
                }
            }
        }
    }
}
