package de.lyzev;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main {


    public static Main INSTANCE;

    public ShardManager shardMan;
    public boolean shutdown = false;

    public Main() throws LoginException, IllegalArgumentException {
        INSTANCE = this;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Main.INSTANCE.getShardMan().setStatus(OnlineStatus.OFFLINE);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }));
        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();

        builder.setToken(paste ur token here);
        builder.setActivity(Activity.playing("Bot is starting..."));
        builder.setStatus(OnlineStatus.ONLINE);

        shardMan = builder.build();

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
        String abos = "0";
        URL url = null;
        try {
            url = new URL("https://www.youtube.com/channel/UCUoGGhbzgJKLCOk89S5ztcw");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            while (true) {
                try {

                    Thread.sleep(1000);

                    InputStream in = url.openStream();

                    Thread.sleep(1000);

                    Scanner scan = new Scanner(in);

                    Thread.sleep(1000);

                    while (scan.hasNext()) {

                        Thread.sleep(50);

                        String str = scan.nextLine();
                        if (str.contains("Abonnenten")) {
                            String[] counter = str.split(" ");
                            if (counter[23].substring(1865).matches("[0-9]+")) {
                                if (!counter[23].substring(1865).matches(abos)) {
                                    Main.INSTANCE.getShardMan().setActivity(Activity.playing("Lyzev Counter: " + counter[23].substring(1865) + " | YT: www.youtube.com/channel/UCUoGGhbzgJKLCOk89S5ztcw"));
                                    abos = counter[23].substring(1865);
                                }
                            }
                        }
                    }
                    scan.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread.sleep(20000);
            }
        } catch (InterruptedException s) {
            s.printStackTrace();
        }
    }

    public ShardManager getShardMan() {
        return shardMan;
    }
}
