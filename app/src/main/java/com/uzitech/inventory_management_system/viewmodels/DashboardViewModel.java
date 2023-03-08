package com.uzitech.inventory_management_system.viewmodels;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.uzitech.inventory_management_system.models.DashboardModel;

public class DashboardViewModel extends MainViewModel {

    public DashboardModel dashboardModel;

    public MutableLiveData<Integer> toastMessage;

    public MutableLiveData<Boolean> categoriesReady, productsReady;

    public ArrayAdapter<String> categoriesAdapter;

    public DashboardViewModel() {
        toastMessage = new MutableLiveData<>();
        categoriesReady = new MutableLiveData<>(false);
        productsReady = new MutableLiveData<>(false);

        dashboardModel = new DashboardModel();

        getCategories();
    }

    void getCategories() {
        firestoreAdapter.getCategories().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    dashboardModel.setCategories(task.getResult());

                    categoriesReady.setValue(true);
                }
            }
        });
    }

    public void setProducts(int index) {
        firestoreAdapter.getProducts(dashboardModel.getCategoryId(index)).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    dashboardModel.setProducts(task.getResult());

                    productsReady.setValue(true);
                }
            }
        });
    }
}
