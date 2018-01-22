package com.datbois.grademaster.response;

import com.datbois.grademaster.model.Status;

public class StatusResponse {

    private Status status;

    public StatusResponse(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
