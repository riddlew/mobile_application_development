package com.riddlew.studentapp.model;

public enum AssessmentType {
    OA(0),
    PA(1);

    private final int value;
    private AssessmentType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AssessmentType fromString(String status) {
        switch(status.toLowerCase()) {
            case "oa":
            case "objective assessment":
                return OA;
            case "pa":
            case "performance assessment":
            default:
                return PA;
        }
    }
}
