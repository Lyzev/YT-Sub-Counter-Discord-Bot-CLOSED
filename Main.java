package de.lyzev;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main {


    public static Main INSTANCE;

    public JDA shardMan;
    public boolean shutdown = false;
    
    private static String token = "YOUR DISCORD BOT TOKEN";
    private static String ytApiToken = "YOUR YouTube Data API (v3) TOKEN";
    private static String channelid = "CHANNEL ID OF THE YT-CHANNEL";

    public Main() throws LoginException, IllegalArgumentException {
        INSTANCE = this;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Main.INSTANCE.getJDA().getPresence().setStatus(OnlineStatus.OFFLINE);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }));

        JDABuilder builder = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_BANS, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_INVITES);

        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        builder.setActivity(Activity.playing("Bot is starting..."));
        builder.setStatus(OnlineStatus.ONLINE);

        JDA jda = builder.build();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shardMan = jda;

        System.out.println("Bot online.");
        shutdown();
    }

    public static void main(String[] args) {

        try {
            new Main();
        } catch (LoginException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {

        new Thread(() -> {

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while ((line = reader.readLine()) != null) {

                    if (line.equalsIgnoreCase("exit")) {
                        shutdown = true;
                        if (shardMan != null) {
                            shardMan.shutdown();


                            System.out.println("Bot offline.");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(this::counter).start();
    }

    public void counter() {

        URL url = null;

        String subs = "0";

        try {
            url = new URL("https://www.googleapis.com/youtube/v3/channels?part=statistics&id=" + channelid + "&key=" + ytApiToken);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            while (true) {

                Scanner scanner = new Scanner(url.openStream());

                StringBuilder sb = new StringBuilder();

                while (scanner.hasNext()) {
                    sb.append(scanner.next());
                }

                JSONObject root = (JSONObject) new JSONParser().parse(sb.toString());

                JSONArray items = (JSONArray) root.get("items");

                JSONObject item = (JSONObject) items.get(0);

                JSONObject statistics = (JSONObject) item.get("statistics");

                String subscriberCount = (String) statistics.get("subscriberCount");

                if (!subs.equals(subscriberCount)) {
                    shardMan.getPresence().setActivity(Activity.playing(subscriberCount + " | Subscriber-Counter"));
                    subs = subscriberCount;
                }

                Thread.sleep(60000);
            }
        } catch (InterruptedException | IOException | ParseException s) {
            s.printStackTrace();
        }
    }

    public JDA getJDA() {
        return shardMan;
    }
}
