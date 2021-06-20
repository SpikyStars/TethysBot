package bot.commands;

import bot.BotMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class UserInfoCommand extends CommandBase {

    @Override
    public void execute(SlashCommandEvent event) throws CommandException {
        Guild guild = event.getGuild();
        User author = event.getUser();
        try {
            OptionMapping userOption = event.getOption("userid");
            User user = userOption != null ? BotMain.jda.retrieveUserById(userOption.getAsString()).complete() : author;
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(user.getAsTag());
            String avatar = user.getAvatarUrl();
            if (avatar == null) avatar = user.getDefaultAvatarUrl();
            embed.setThumbnail(avatar);
            embed.addField("Account created", getTimestamp(user.getTimeCreated()), true);
            embed.setFooter("queried by "+ author.getAsTag());

            try {
                Member member = guild.retrieveMember(user).complete();
                embed.setColor(member.getColor());
                embed.addField("Account joined", getTimestamp(member.getTimeJoined()), true);
            } catch (ErrorResponseException ignored){}

            event.replyEmbeds(embed.build()).queue();
        } catch (NumberFormatException e){
            throw new CommandException("Invalid ID!", "The ID provided is not a valid Discord ID.");
        } catch (ErrorResponseException e2){
            throw new CommandException("User not found!", "A user with that ID does not exist.");
        }
    }

    private String getTimestamp(OffsetDateTime date){
        return DateTimeFormatter.ofPattern(BotMain.DATE_FORMAT).format(date.withOffsetSameInstant(ZoneId.of(BotMain.TIMEZONE).getRules().getOffset(Instant.now())));
    }
}
