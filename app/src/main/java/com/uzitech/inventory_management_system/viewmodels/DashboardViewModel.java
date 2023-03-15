package com.uzitech.inventory_management_system.viewmodels;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.uzitech.inventory_management_system.models.DashboardModel;

import java.util.HashMap;
import java.util.Map;

public class DashboardViewModel extends MainViewModel {

    public DashboardModel dashboardModel;

    public MutableLiveData<Integer> toastMessage;

    public MutableLiveData<Boolean> categoriesReady, productsReady;

    public DashboardViewModel() {
        toastMessage = new MutableLiveData<>();

        dashboardModel = new DashboardModel();

        categoriesReady = new MutableLiveData<>();
        productsReady = new MutableLiveData<>();

        getCategories();
    }

    private void getCategories() {
        firestoreAdapter.getCategories().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                dashboardModel.setCategories(task.getResult());

                categoriesReady.setValue(true);
            }
        });
    }

    public void setProducts(int index) {
        if (dashboardModel.getProducts() != null) {
            productsReady.setValue(true);

            firestoreAdapter.getProducts(dashboardModel.getCategoryId(index)).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Map<String, Integer> temp = new HashMap<>();

                    for (DocumentSnapshot doc : task.getResult()) {
                        String name = doc.getString("name");
                        int quantity = Math.toIntExact(doc.getLong("quantity"));

                        temp.put(name, quantity);
                    }

                    if (!dashboardModel.getProducts().equals(temp)) {
                        dashboardModel.setProducts(temp);

                        productsReady.setValue(true);
                    }
                }
            });
        } else {
            firestoreAdapter.getProducts(dashboardModel.getCategoryId(index)).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    Map<String, Integer> temp = new HashMap<>();

                    for (DocumentSnapshot doc : task.getResult()) {
                        String name = doc.getString("name");
                        int quantity = Math.toIntExact(doc.getLong("quantity"));

                        temp.put(name, quantity);
                    }

                    dashboardModel.setProducts(temp);

                    productsReady.setValue(true);
                }
            });
        }
    }
}
