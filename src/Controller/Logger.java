package Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * This class is responsible for all the logging that need to be done.
 * At the moment it just takes all the messages and, based on their LoggingLevel, decides whether to only write it
 * to file or also to Console.
 */
public class Logger {

    /**
     * Write a Message to the log
     *
     * @param msg   the message to be written
     * @param level the level of the Message
     */
    public static void log(String msg, LoggingLevel level) {
        if (level.level() <= Settings.LOGGING_LEVEL.level()) {
            System.out.println(level + ": " + msg);
        }

        String loggedMessage = level + ": " + msg + "\n";

        try {
            Files.write(Paths.get("log.txt"), loggedMessage.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}