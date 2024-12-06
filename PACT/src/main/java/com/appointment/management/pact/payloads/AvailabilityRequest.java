package com.appointment.management.pact.payloads;

import java.util.List;

public class AvailabilityRequest {
    private String workingHourStarts;
    private String workingHourEnds;
    private List<String> availableDates;

    // Getters and Setters
    public String getWorkingHourStarts() {
        return workingHourStarts;
    }

    public void setWorkingHourStarts(String workingHourStarts) {
        this.workingHourStarts = workingHourStarts;
    }

    public String getWorkingHourEnds() {
        return workingHourEnds;
    }

    public void setWorkingHourEnds(String workingHourEnds) {
        this.workingHourEnds = workingHourEnds;
    }

    public List<String> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<String> availableDates) {
        this.availableDates = availableDates;
    }
}
