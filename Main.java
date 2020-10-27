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

        builder.setToken(paste here your token);
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
        try {
            while (true) {
                try {

                    URL url = new URL("https://www.youtube.com/channel/UCUoGGhbzgJKLCOk89S5ztcw");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    InputStream in = url.openStream();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Scanner scan = new Scanner(in);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    while (scan.hasNext()) {
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String str = scan.nextLine();
                        if (str.contains("Abonnenten")) {
                            String[] counter = str.split(" ");
                            if (counter[23].substring(1865) != null && counter[23] != null && counter[23].substring(1865).matches("[0-9]+")) {
                                if (!counter[23].substring(1865).matches(abos)) {
                                    Main.INSTANCE.getShardMan().setActivity(Activity.playing("Lyzev Counter: " + counter[23].substring(1865) + " | YT: www.youtube.com/channel/UCUoGGhbzgJKLCOk89S5ztcw"));
                                    abos = counter[23].substring(1865);
                                }
                            }
                        }
                    }
                    scan.close();

                } catch (IOException ignored) {
                }
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ShardManager getShardMan() {
        return shardMan;
    }
}
