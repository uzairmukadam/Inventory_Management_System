package com.uzitech.inventory_management_system.adapters;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthAdapter {

    FirebaseAuth auth;

    public FirebaseAuthAdapter() {
        auth = FirebaseAuth.getInstance();
    }

    public String getUID() {
        return auth.getUid();
    }

    public Task<AuthResult> login(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    public void signout() {
        auth.signOut();
    }
}
