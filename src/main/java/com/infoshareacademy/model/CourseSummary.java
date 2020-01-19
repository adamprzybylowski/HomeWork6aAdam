package com.infoshareacademy.model;

import java.util.List;

public class CourseSummary {

    private String courseName;

    private List<String> attendees;

    public CourseSummary(String courseName, List<String> attendees) {
        this.courseName = courseName;
        this.attendees = attendees;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CourseSummary{");
        sb.append("courseName='").append(courseName).append('\'');
        sb.append(", attendees=").append(attendees);
        sb.append('}');
        return sb.toString();
    }
}
