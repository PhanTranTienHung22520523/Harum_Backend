package com.Harum.Harum.Services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

        private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
        private final Random random = new Random();

        // Tạo OTP
        public String generateOtp(String email) {
            String otp = String.valueOf(100000 + random.nextInt(900000));
            long expiryTime = System.currentTimeMillis() + (10 * 60 * 1000); // Hết hạn sau 10 phút
            otpStore.put(email, new OtpData(otp, expiryTime));
            return otp;
        }

        // Kiểm tra OTP có hợp lệ không
        public boolean validateOtp(String email, String otp) {
            OtpData otpData = otpStore.get(email);
            if (otpData == null || System.currentTimeMillis() > otpData.expiryTime) {
                otpStore.remove(email);
                return false;
            }
            return otpData.otp.equals(otp);
        }

        // Xóa OTP sau khi xác thực thành công
        public void removeOtp(String email) {
            otpStore.remove(email);
        }

        private static class OtpData {
            String otp;
            long expiryTime;

            public OtpData(String otp, long expiryTime) {
                this.otp = otp;
                this.expiryTime = expiryTime;
            }
        }

}
