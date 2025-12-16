package system;

import system.FileHandler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogManager {

    private final FileHandler fileHandler;
    private final String logFilePath;
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LogManager(String logFilePath) {
        this.fileHandler = new FileHandler();
        this.logFilePath = logFilePath;
    }

    // Core logging method
    public void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = "[" + timestamp + "] [" + level + "] " + message;

        // Persist log
        fileHandler.appendToFile(logFilePath, logEntry);

        System.out.println(logEntry);
    }

    // Convenience helpers
    public void info(String message) {
        log("INFO", message);
    }

    public void warning(String message) {
        log("WARN", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }
}
