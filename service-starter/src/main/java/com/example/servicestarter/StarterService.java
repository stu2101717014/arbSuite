package com.example.servicestarter;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableScheduling
public class StarterService {

    public static final int DELAY = 15000;

    public static final int DELAY_WAIT = 10000;

    public static boolean singleExecution = false;

    @Scheduled(fixedDelay = DELAY)
    public void start() {

        //                       port
        //netstat -aon | findstr 8082
        //             Thread_ID
        //taskkill /PID 26240 /F
        //mvn spring-boot:run

        Path parent = Paths.get(System.getProperty("user.dir")).getParent();

        //                            Betano    22Bet     BWin    WilliamHill  "palmsbet", "betwinner"
        String[] dirs = new String[]{"betano", "bets22", "bwin", "williamhill", "palmsbet"};

        if (!singleExecution) {
            try {
                String cmd = "cd  " + parent + "\\" + "dtos " + " & mvn clean install ";
                System.out.println(cmd);
                ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", cmd);
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println("    DTOS INSTALL       " + line);
                }
                p.waitFor();

            } catch (Exception e) {
                e.printStackTrace();
            }


            List<Thread> threads = new ArrayList<>();

            createAndExecuteThreads(dirs, parent, " & mvn clean install ", "    INSTALL     ", "          ", threads);

            singleExecution = true;
        }

        List<Thread> threads = new ArrayList<>();

        createAndExecuteThreads(dirs, parent, " & mvn spring-boot:run ", "     RUN    ", "           ", threads);

    }

    private void createAndExecuteThreads(String[] dirs, Path parent, String x, String x1, String ___________, List<Thread> threads) {
        for (String dir : dirs) {
            Thread current = getThread(parent, dir, x, x1, ___________);
            threads.add(current);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        try {
            for (Thread thread : threads) {
                thread.join(DELAY_WAIT);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Thread getThread(Path parent, String dir, String x, String x1, String x2) {
        Thread current = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String cmd = "cd  " + parent + "\\" + dir + x;
                    System.out.println(cmd);
                    ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", cmd);
                    builder.redirectErrorStream(true);
                    Process p = builder.start();
                    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while (true) {
                        line = r.readLine();
                        if (line == null) {
                            break;
                        }
                        System.out.println(x1 + dir.toUpperCase() + x2 + line);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        return current;
    }
}
