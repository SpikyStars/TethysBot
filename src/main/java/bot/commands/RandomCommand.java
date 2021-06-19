package bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class RandomCommand extends Command{
    private static final int MIN_LIMIT = -50000;
    private static final int MAX_LIMIT = 50000;
    private static final int ROLL_LIMIT = 50;

    public RandomCommand(){
        infoShort = "Generate a random number";
        info = "Generates a random integer within the range of the given minimum and maximum numbers (inclusive). You can optionally specify the number of times to roll; the default is 1.";
        usage = "rand <min> <max> [times]";
        example = "rand 1 6 2";
    }

    @Override
    public void execute(MessageReceivedEvent event, String params) throws CommandException {
        MessageChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        try {
            int[] args = Arrays.stream(params.split(" ")).mapToInt(Integer::parseInt).toArray();
            int min = Math.max(args[0], MIN_LIMIT);
            int max = Math.min(args[1], MAX_LIMIT);
            int numTimes = args.length > 2 ? Math.min(args[2], ROLL_LIMIT) : 1;
            embed.setTitle("Rolled " + numTimes + " time(s) in range " + min + " to " + max);
            embed.setDescription(getRollsString(min, max, numTimes));
        } catch (NumberFormatException e){
            throw new CommandException("Incorrect format!", "The minimum, maximum, and range need to be integers.");
        }
        channel.sendMessage(embed.build()).queue();
    }

    private String getRollsString(int min, int max, int numTimes){
        int range = max - min + 1;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numTimes; i++){
            int roll = (int)(Math.random() * range) + min;
            result.append(roll);
            if (numTimes - i != 1){
                result.append(", ");
            }
        }
        return result.toString();
    }
}
