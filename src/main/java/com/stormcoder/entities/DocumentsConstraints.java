package com.stormcoder.entities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalTime;

@Component
public class DocumentsConstraints {
    private LocalTime signEditionTimeMin;
    private LocalTime signEditionTimeMax;

    @Value("${constraints.maxDocumentFlows}")
    private int maxDocumentFlows;

    @Value("${constraints.maxDocumentPerHour}")
    private int maxDocumentPerHour;

    @Value("${constraints.maxDocumentFlowsPerCompany}")
    private int maxDocumentFlowsPerCompany;

    public DocumentsConstraints(@Value("${constraints.signEditionTimeMinHours}") int timeMinHours,
                                @Value("${constraints.signEditionTimeMinMinutes}") int timeMinMinutes,
                                @Value("${constraints.signEditionTimeMaxHours}") int timeMaxHours,
                                @Value("${constraints.signEditionTimeMaxMinutes}") int timeMaxMinutes) {
        signEditionTimeMin = LocalTime.of(timeMinHours, timeMinMinutes);
        signEditionTimeMax = LocalTime.of(timeMaxHours, timeMaxMinutes);
    }

    public LocalTime getSignEditionTimeMin() {
        return signEditionTimeMin;
    }

    public LocalTime getSignEditionTimeMax() {
        return signEditionTimeMax;
    }

    public int getMaxDocumentFlows() {
        return maxDocumentFlows;
    }

    public int getMaxDocumentPerHour() {
        return maxDocumentPerHour;
    }

    public int getMaxDocumentFlowsPerCompany() {
        return maxDocumentFlowsPerCompany;
    }
}
