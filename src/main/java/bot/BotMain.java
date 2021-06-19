package bot;

import bot.commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BotMain {
    public static final String PREFIX = "!";
    public static final String DATE_FORMAT = "MM/dd/yyyy hh:mm:ss a";
    public static final String TIMEZONE = "America/Los_Angeles";

    public static Map<String, Command> commands = new HashMap<>();
    public static JDA jda;

    public static void main(String[] args) {

        try {
            registerCommands();
            BufferedReader reader = new BufferedReader(new FileReader("token.txt"));
            String token = reader.readLine();
            jda = JDABuilder.createDefault(token).addEventListeners(new DiscordListener()).build();
        } catch (FileNotFoundException e){
            System.err.println("File containing bot token not found - please create a token.txt containing the bot's token in the main directory");
        } catch (IOException e2){
            System.err.println("Issue with reading from token.txt");
        } catch (LoginException e3){
            System.err.println("Issue logging into Discord");
        }

    }

    private static void registerCommands(){
        commands.put("ping", new PingCommand());
        commands.put("rand", new RandomCommand());
        commands.put("userinfo", new UserInfoCommand());

        commands.put("help", new HelpCommand());
        System.out.println("Registered " + commands.size() + " commands!");
    }
}
