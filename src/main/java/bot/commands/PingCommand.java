package bot.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand extends Command{
    public PingCommand(){
        infoShort = "Ping the bot";
        info = "Pings the bot, returning the time it took for the bot to respond in milliseconds.";
        usage = "ping";
    }

    @Override
    public void execute(MessageReceivedEvent event, String params) {
        MessageChannel channel = event.getChannel();
        long time = System.currentTimeMillis();
        channel.sendMessage("Pong!").queue(response -> response.editMessageFormat("Pong! %dms", System.currentTimeMillis() - time).queue());
    }
}
