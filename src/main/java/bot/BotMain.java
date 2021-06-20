package bot;

import bot.commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BotMain {
    public static final String DATE_FORMAT = "MM/dd/yyyy hh:mm:ss a";
    public static final String TIMEZONE = "America/Los_Angeles";

    public static Map<String, CommandBase> commands = new HashMap<>();
    public static JDA jda;

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("token.txt"));
            String token = reader.readLine();
            jda = JDABuilder.createDefault(token).addEventListeners(new DiscordListener()).build();
            jda.awaitReady();
            registerCommandsLocal();
        } catch (FileNotFoundException e){
            System.err.println("File containing bot token not found - please create a token.txt containing the bot's token in the main directory");
        } catch (IOException e){
            System.err.println("Issue with reading from token.txt");
        } catch (LoginException e){
            System.err.println("Issue logging into Discord");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void registerCommands(){
        jda.upsertCommand(new CommandData("ping", "Ping the bot")).queue();
        jda.upsertCommand(new CommandData("rand", "Generate a random number")
                .addOption(OptionType.INTEGER, "min", "minimum number range", true)
                .addOption(OptionType.INTEGER, "max", "maximum number range", true)
                .addOption(OptionType.INTEGER, "times", "number of times to roll", false)).queue();
        jda.upsertCommand(new CommandData("userinfo", "Bring up info on a user")
                .addOption(OptionType.STRING, "userid", "Discord id of user", false)).queue();
        commands.put("ping", new PingCommand());
        commands.put("rand", new RandomCommand());
        commands.put("userinfo", new UserInfoCommand());
        jda.retrieveCommands().queue(response -> System.out.println("Registered " + response.size() + " commands!"));
    }

    private static void registerCommandsLocal() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("test_guild.txt"));
        Guild guild = jda.getGuildById(reader.readLine());
        guild.upsertCommand(new CommandData("ping", "Ping the bot")).queue();
        guild.upsertCommand(new CommandData("rand", "Generate a random number")
                .addOption(OptionType.INTEGER, "min", "minimum number range", true)
                .addOption(OptionType.INTEGER, "max", "maximum number range", true)
                .addOption(OptionType.INTEGER, "times", "number of times to roll", false)).queue();
        guild.upsertCommand(new CommandData("userinfo", "Bring up info on a user")
                .addOption(OptionType.STRING, "userid", "Discord id of user", false)).queue();
        commands.put("ping", new PingCommand());
        commands.put("rand", new RandomCommand());
        commands.put("userinfo", new UserInfoCommand());
        guild.retrieveCommands().queue(response -> System.out.println("Registered " + response.size() + " commands!"));
    }
}
