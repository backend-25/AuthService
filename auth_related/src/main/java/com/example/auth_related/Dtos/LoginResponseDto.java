package com.example.auth_related.Dtos;

public class LoginResponseDto {
    private RequestStatus requestStatus;

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
