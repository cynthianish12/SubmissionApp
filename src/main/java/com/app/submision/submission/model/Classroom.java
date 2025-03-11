package com.app.submision.submission.model;

public enum Classroom {
    Y1_A("Y1 A"),
    Y1_B("Y1 B"),
    Y1_C("Y1 C"),
    Y2_A("Y2 A"),
    Y2_B("Y2 B"),
    Y2_C("Y2 C"),
    Y2_D("Y2 D"),
    Y3_A("Y3 A"),
    Y3_B("Y3 B"),
    Y3_C("Y3 C"),
    Y3_D("Y3 D"),;

    private final String className;

    Classroom(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
