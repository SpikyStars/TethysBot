package bot.commands;

import bot.BotMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class UserInfoCommand extends Command{

    public UserInfoCommand(){
        infoShort = "Bring up info on a user";
        info = "Brings up some information about a Discord user. More information is present if that user is in the server.";
        usage = "userinfo <userid>";
    }

    @Override
    public void execute(MessageReceivedEvent event, String params) throws CommandException {
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        User author = event.getAuthor();
        try {
            User user = BotMain.jda.retrieveUserById(params).complete();
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

            channel.sendMessage(embed.build()).queue();
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
