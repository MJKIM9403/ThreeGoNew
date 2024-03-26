package com.io.threegonew.dto;

import java.util.List;

public class SharePlannerRequest {
    private Long plannerId;
    private String ownerId;
    private List<String> sharedWithUserIds;

    // getters and setters
    public Long getPlannerId() {
        return plannerId;
    }

    public void setPlannerId(Long plannerId) {
        this.plannerId = plannerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<String> getSharedWithUserIds() {
        return sharedWithUserIds;
    }

    public void setSharedWithUserIds(List<String> sharedWithUserIds) {
        this.sharedWithUserIds = sharedWithUserIds;
    }
}
