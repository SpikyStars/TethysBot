package bot;

import bot.commands.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class BotMain {
    public static String dateFormat;
    public static String timezone;
    public static boolean isGlobal;

    public static Map<String, CommandBase> commands = new HashMap<>();
    public static JDA jda;
    public static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("config.json"));
            JsonObject config = gson.fromJson(reader, JsonObject.class);
            loadSettings(config);
            String token = config.get("token").getAsString();
            jda = JDABuilder.createDefault(token).addEventListeners(new DiscordListener()).build();
            jda.awaitReady();
            if (isGlobal){
                registerCommands();
            } else {
                String guildId = config.get("guild_id").getAsString();
                registerCommandsLocal(guildId);
            }
        } catch (FileNotFoundException e){
            System.err.println("config.json not found - please create this file in the main directory");
        } catch (LoginException e){
            System.err.println("Issue logging into Discord");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void loadSettings(JsonObject config){
        dateFormat = config.get("date_format").getAsString();
        timezone = config.get("timezone").getAsString();
        isGlobal = config.get("is_global").getAsBoolean();
    }

    private static void registerCommands(){
        loadCommandMap();
        commands.forEach((k, v) -> jda.upsertCommand(v.createCommandData()).queue());
        jda.retrieveCommands().queue(response -> System.out.println("Registered " + response.size() + " commands!"));
    }

    private static void registerCommandsLocal(String guildId) {
        Guild guild = jda.getGuildById(guildId);
        if (guild != null){
            loadCommandMap();
            commands.forEach((k, v) -> guild.upsertCommand(v.createCommandData()).queue());
            guild.retrieveCommands().queue(response -> System.out.println("Registered " + response.size() + " commands!"));
        } else {
            System.err.println("A valid guild ID was not provided in the config");
        }
    }

    private static void loadCommandMap(){
        commands.put("ping", new PingCommand());
        commands.put("rand", new RandomCommand());
        commands.put("userinfo", new UserInfoCommand());
    }
}
