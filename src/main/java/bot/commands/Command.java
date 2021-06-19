package bot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
    @Getter protected String infoShort;
    @Getter protected String info;
    @Getter protected String usage;
    @Getter protected String example;

    public abstract void execute(MessageReceivedEvent event, String params) throws CommandException;
}
