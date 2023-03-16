package com.uzitech.inventory_management_system.adapters;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

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

    public Task<Void> updateUser(String uid, boolean login) {
        Map<String, Object> data = new HashMap<>();
        data.put("is_login", login);

        return firestore.collection("users").document(uid).update(data);
    }

    public Task<QuerySnapshot> getCategories() {
        return firestore.collection("categories").orderBy("name").get();
    }

    public Task<QuerySnapshot> getProducts(String category) {
        return firestore.collection("products")
                .whereEqualTo("category", category).orderBy("index").get();
    }

    public Task<DocumentReference> addIndividual(int type, Map<String, String> individual) {
        String collection = null;

        switch (type) {
            case 0:
                collection = "manufacturers";
                break;
            case 1:
                collection = "customers";
                break;
        }

        return firestore.collection(collection).add(individual);
    }

    public Task<DocumentReference> addEntry(int type, Map<String, Object> entry) {
        String collection = null;

        switch (type) {
            case 0:
                collection = "purchases";
                break;
            case 1:
                collection = "sales";
                break;
        }

        return firestore.collection(collection).add(entry);
    }

    public Task<Void> updateProductQuantity(int type, Map<String, Integer> quantity) {
        int increment_factor = 0;

        switch (type) {
            case 0:
                increment_factor = 1;
                break;
            case 1:
                increment_factor = -1;
                break;
        }

        WriteBatch batch = firestore.batch();

        for (Map.Entry<String, Integer> product : quantity.entrySet()) {
            batch.update(firestore.collection("products").document(product.getKey()),
                    "quantity", FieldValue.increment((long) product.getValue() * increment_factor));
        }

        return batch.commit();
    }

    public Task<QuerySnapshot> getIndividuals(String category, int type) {
        String collection = null;

        switch (type) {
            case 0:
                collection = "manufacturers";
                break;
            case 1:
                collection = "customers";
                break;
        }

        assert collection != null;
        return firestore.collection(collection)
                .whereArrayContains("categories", category).get();
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
