//package com.nurbk.ps.photosfromtheworldprivate.viewmodel.viewstates;
//
//import androidx.annotation.Nullable;
//
///**
// * Data validation state of the login form.
// */
//public class LoginFormState {
//
//    @Nullable
//    private final Integer usernameError;
//    @Nullable
//    private final Integer passwordError;
//
//    private final boolean isDataValid;
//
//    public LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
//        this.usernameError = usernameError;
//        this.passwordError = passwordError;
//        this.isDataValid = false;
//    }
//
//    public LoginFormState(boolean isDataValid) {
//        this.usernameError = null;
//        this.passwordError = null;
//        this.isDataValid = isDataValid;
//    }
//
//    @Nullable
//    public Integer getUsernameError() {
//        return usernameError;
//    }
//
//    @Nullable
//    public Integer getPasswordError() {
//        return passwordError;
//    }
//
//    public boolean isDataValid() {
//        return isDataValid;
//    }
//}
