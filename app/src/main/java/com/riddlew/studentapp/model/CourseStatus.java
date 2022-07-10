package com.riddlew.studentapp.model;

import java.util.Locale;

public enum CourseStatus {
    IN_PROGRESS(0),
    COMPLETED(1),
    DROPPED(2),
    PLAN_TO_TAKE(3);

    private final int value;
    private CourseStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CourseStatus fromString(String status) {
        switch(status.toLowerCase()) {
            case "in progress":
                return IN_PROGRESS;
            case "completed":
                return COMPLETED;
            case "dropped":
                return DROPPED;
            case "plan to take":
            default:
                return PLAN_TO_TAKE;
        }
    }
}
