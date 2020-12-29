package tech.yojigen.pixiu.dto;

import com.google.gson.annotations.SerializedName;

public class CreateAccountDTO {
    @SerializedName("body")
    private UserData userData;

    public class UserData {
        @SerializedName("user_account")
        private String username;
        @SerializedName("password")
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
