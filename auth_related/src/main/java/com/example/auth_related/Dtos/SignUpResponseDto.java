package com.example.auth_related.Dtos;

public class SignUpResponseDto {
    private RequestStatus requestStatus;
    private String signupMsg;

    public RequestStatus getRequestStatus() {
        return requestStatus;

    }

    public String getSignupMsg() {
        return signupMsg;
    }

    public void setSignupMsg(String signupMsg) {
        this.signupMsg = signupMsg;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
