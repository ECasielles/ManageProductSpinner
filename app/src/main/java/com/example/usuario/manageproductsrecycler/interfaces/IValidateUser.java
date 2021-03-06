package com.example.usuario.manageproductsrecycler.interfaces;

import android.util.Patterns;

import com.example.usuario.manageproductsrecycler.model.Error;

public interface IValidateUser extends IValidateAccount {

    // interface Presenter Adds code to superclass' presenter interface
    // So we need it to have a different name to make a clear distinction
    interface PresenterUser {
        int validateCredentialsEmail(String email);

        /*
        static int validateCredentialsEmail(String email) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                return Error.EMAIL_INVALID;
            return Error.OK;
        }
        */
    }

}
