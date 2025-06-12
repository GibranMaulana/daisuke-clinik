package com.example.model;

/**
 * Enum representing different levels of admin privileges
 */
public enum AdminLevel {
    REGULAR("REGULAR"),
    SUPER("SUPER");
    
    private final String level;
    
    AdminLevel(String level) {
        this.level = level;
    }
    
    public String getLevel() {
        return level;
    }
    
    @Override
    public String toString() {
        return level;
    }
    
    public static AdminLevel fromString(String level) {
        for (AdminLevel adminLevel : AdminLevel.values()) {
            if (adminLevel.level.equalsIgnoreCase(level)) {
                return adminLevel;
            }
        }
        return REGULAR; // Default to regular admin
    }
}
