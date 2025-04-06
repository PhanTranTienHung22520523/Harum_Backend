package com.Harum.Harum.DTO;

import com.Harum.Harum.Models.Users;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyOtpRequestDTO {
    private String email;
    private String otp;
    private Users user;

    public VerifyOtpRequestDTO() {
    }

    public VerifyOtpRequestDTO(String email, String otp, Users user) {
        this.email = email;
        this.otp = otp;
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
