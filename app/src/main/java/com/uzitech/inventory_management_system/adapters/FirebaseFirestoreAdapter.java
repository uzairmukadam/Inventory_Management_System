package com.uzitech.inventory_management_system.adapters;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseFirestoreAdapter {

    FirebaseFirestore firestore;

    public FirebaseFirestoreAdapter() {
        firestore = FirebaseFirestore.getInstance();
    }

    public Task<DocumentSnapshot> getMetadata() {
        return firestore.collection("metadata")
                .document("android")
                .get();
    }

    public Task<DocumentSnapshot> getUser(String uid) {
        return firestore.collection("users")
                .document(uid)
                .get();
    }

    public Task<Void> updateUser(String uid) {
        Map<String, Object> data = new HashMap<>();
        data.put("is_login", true);

        return firestore.collection("users").document(uid).update(data);
    }

    public Task<DocumentReference> log(String uid, String device, int versionCode, boolean critical, String msg) {
        Map<String, Object> log = new HashMap<>();

        log.put("user", uid);
        log.put("device", device);
        log.put("version", versionCode);
        log.put("critical", critical);
        log.put("message", msg);
        log.put("timestamp", FieldValue.serverTimestamp());

        return firestore.collection("logs").add(log);
    }
}
