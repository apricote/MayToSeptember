package com.toelle.maytoseptember.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
        if (level.level() > Settings.LOGGING_LEVEL.level()) {
            return;
        }

        String levelString = "[" + level + "]";
        String sourceString = "[" + sun.reflect.Reflection.getCallerClass(2).getCanonicalName() + "]";
        String timeString = "[" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS")) + "]";

        String loggedMessage = timeString + levelString + sourceString + ": " + msg + System.lineSeparator();

        System.out.print(loggedMessage);
        try {
            Files.write(Paths.get("log.txt"), loggedMessage.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}