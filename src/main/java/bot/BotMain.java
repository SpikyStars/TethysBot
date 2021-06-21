package bot;

import bot.commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

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
    public static final boolean IS_GLOBAL = false;

    public static Map<String, CommandBase> commands = new HashMap<>();
    public static JDA jda;

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("token.txt"));
            String token = reader.readLine();
            jda = JDABuilder.createDefault(token).addEventListeners(new DiscordListener()).build();
            jda.awaitReady();
            if (IS_GLOBAL){
                registerCommands();
            } else {
                registerCommandsLocal();
            }
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
        loadCommandMap();
        commands.forEach((k, v) -> jda.upsertCommand(v.createCommandData()).queue());
        jda.retrieveCommands().queue(response -> System.out.println("Registered " + response.size() + " commands!"));
    }

    private static void registerCommandsLocal() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("guild_id.txt"));
            Guild guild = jda.getGuildById(reader.readLine());
            if (guild != null){
                loadCommandMap();
                commands.forEach((k, v) -> guild.upsertCommand(v.createCommandData()).queue());
                guild.retrieveCommands().queue(response -> System.out.println("Registered " + response.size() + " commands!"));
            } else {
                System.err.println("A valid guild ID was not provided in guild_id.txt");
            }
        } catch (IOException e){
            System.err.println("Issue with reading from guild_id.txt");
        }
    }

    private static void loadCommandMap(){
        commands.put("ping", new PingCommand());
        commands.put("rand", new RandomCommand());
        commands.put("userinfo", new UserInfoCommand());
    }
}
