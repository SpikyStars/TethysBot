package bot.commands;

import bot.BotMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class UserInfoCommand extends CommandBase {

    @Override
    public CommandData createCommandData() {
        return new CommandData("userinfo", "Bring up info on a user")
                .addOption(OptionType.STRING, "userid", "Discord id of user", false);
    }

    @Override
    public void execute(SlashCommandEvent event) throws CommandException {
        Guild guild = event.getGuild();
        User author = event.getUser();
        try {
            OptionMapping userOption = event.getOption("userid");
            User user = userOption != null ? BotMain.jda.retrieveUserById(userOption.getAsString()).complete() : author;
            EmbedBuilder embed = new EmbedBuilder();
            setAuthorDetails(embed, author);
            setUserDetails(embed, user, guild);
            embed.setTimestamp(Instant.now());
            event.replyEmbeds(embed.build()).queue();
        } catch (NumberFormatException e){
            throw new CommandException("Invalid ID!", "The ID provided is not a valid Discord ID.");
        } catch (ErrorResponseException e2){
            throw new CommandException("User not found!", "A user with that ID does not exist.");
        }
    }

    private void setAuthorDetails(EmbedBuilder embed, User author){
        embed.setFooter("queried by "+ author.getAsTag(), author.getEffectiveAvatarUrl());
    }

    private void setUserDetails(EmbedBuilder embed, User user, Guild guild){
        embed.setTitle(user.getAsTag());
        embed.setThumbnail(user.getEffectiveAvatarUrl());
        embed.addField("Account created", getTimestamp(user.getTimeCreated()), true);
        try {
            Member member = guild.retrieveMember(user).complete();
            embed.setColor(member.getColor());
            embed.addField("Account joined", getTimestamp(member.getTimeJoined()), true);
            OffsetDateTime boosted = member.getTimeBoosted();
            if (boosted != null){
                embed.addField("Boosting server since", getTimestamp(boosted), true);
            }
            StringBuilder sb = new StringBuilder();
            for (Role r : member.getRoles()){
                sb.append(r.getName());
                sb.append(", ");
            }
            int length = sb.length();
            sb.delete(length - 2, length - 1);
            embed.addField("Roles", sb.toString(), true);
        } catch (ErrorResponseException ignored){}
    }

    private String getTimestamp(OffsetDateTime date){
        return DateTimeFormatter.ofPattern(BotMain.dateFormat).format(date.withOffsetSameInstant(ZoneId.of(BotMain.timezone).getRules().getOffset(Instant.now())));
    }
}
