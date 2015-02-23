package Controller;

/**
 * The different logging levels that there are
 */
public enum LoggingLevel {
    ERROR(1),
    WARNING(5),
    INFO(50),
    DEBUG(100);

    private final int level;

    LoggingLevel(int level) {
        this.level = level;
    }

    public int level() {
        return level;
    }
    
}
