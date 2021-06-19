package bot.commands;

import bot.BotMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class HelpCommand extends Command{
    private static final int pageSize = 10;
    private ArrayList<String> sortedCommandList;

    public HelpCommand(){
        loadCommandList();
    }

    @Override
    public void execute(MessageReceivedEvent event, String params) throws CommandException{
        MessageChannel channel = event.getChannel();
        String argument = params.split(" ")[0];
        EmbedBuilder embed = new EmbedBuilder();
        if ((argument.equals("") || argument.matches("\\d+"))) {
            createPageEmbed(embed, argument);
        } else {
            createCommandEmbed(embed, argument);
        }
        channel.sendMessage(embed.build()).queue();
    }

    private void createPageEmbed(EmbedBuilder embed, String argument){
        int curPage;
        int pageDiv = sortedCommandList.size() / pageSize;
        int pageCount = (sortedCommandList.size() % pageSize == 0) && (sortedCommandList.size() >= pageSize) ? pageDiv : ++pageDiv;
        try {
            curPage = Integer.parseInt(argument);
            if (curPage < 1){
                curPage = 1;
            } else if (curPage > pageDiv){
                curPage = pageCount;
            }
        } catch (NumberFormatException e){
            curPage = 1;
        }
        for (int i = (curPage - 1) * pageSize; i < (curPage * pageSize); i++){
            if (i >= sortedCommandList.size()) break;
            String commandName = sortedCommandList.get(i);
            embed.addField(commandName, BotMain.commands.get(commandName).getInfoShort(), false);
        }
        embed.setFooter("Page " + curPage + "/" + pageCount);
    }

    private void createCommandEmbed(EmbedBuilder embed, String argument) throws CommandException{
        Command command = BotMain.commands.get(argument);
        if (command != null){
            embed.setTitle(argument);
            embed.addField("Usage", "`!"+ command.getUsage() + "`", false);
            embed.setDescription(command.getInfo());
            if (command.getExample() != null){
                embed.addField("Example", "`!" + command.getExample() + "`", false);
            }
        } else {
            throw new CommandException("Invalid command specified!",
                    "Command `" + argument.substring(0, 50) + "` is not a valid command. Use `!help` to view a list of all valid commands.");
        }
    }

    private void loadCommandList(){
        ArrayList<String> commandList = new ArrayList<>(BotMain.commands.keySet());
        commandList.sort(String::compareToIgnoreCase);
        sortedCommandList = commandList;
    }
}
