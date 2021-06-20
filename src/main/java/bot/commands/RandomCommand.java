package bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Objects;

public class RandomCommand extends CommandBase {
    private static final int MIN_LIMIT = -50000;
    private static final int MAX_LIMIT = 50000;
    private static final int ROLL_LIMIT = 50;

    @Override
    public void execute(SlashCommandEvent event){
        EmbedBuilder embed = new EmbedBuilder();
        try {
            int min = Math.max(Integer.parseInt(Objects.requireNonNull(event.getOption("min")).getAsString()), MIN_LIMIT);
            int max = Math.min(Integer.parseInt(Objects.requireNonNull(event.getOption("max")).getAsString()), MAX_LIMIT);
            OptionMapping timesOption = event.getOption("times");
            int times = timesOption != null ? Math.min(Integer.parseInt(timesOption.getAsString()), ROLL_LIMIT) : 1;
            embed.setTitle("Rolled " + times + " time(s) in range " + min + " to " + max);
            embed.setDescription(getRollsString(min, max, times));
            event.replyEmbeds(embed.build()).queue();
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
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
