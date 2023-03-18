package com.uzitech.inventory_management_system.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.uzitech.inventory_management_system.models.ViewRecordsModel;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Map;

public class ViewRecordsViewModel extends MainViewModel {

    public ViewRecordsModel viewRecordsModel;

    public MutableLiveData<Integer> toastMessage;
    public MutableLiveData<Boolean> individuals_loaded, dates_loaded;

    public ViewRecordsViewModel() {
        toastMessage = new MutableLiveData<>();
        viewRecordsModel = new ViewRecordsModel();

        individuals_loaded = new MutableLiveData<>(false);
        dates_loaded = new MutableLiveData<>(false);
    }

    public void getProducts() {
        firestoreAdapter.getProducts(viewRecordsModel.getCategory_id()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<String> products = new ArrayList<>();
                ArrayList<String> product_ids = new ArrayList<>();

                for (DocumentSnapshot doc : task.getResult()) {
                    String product_name = doc.getString("name");
                    String id = doc.getId();

                    products.add(product_name);
                    product_ids.add(id);
                }

                viewRecordsModel.setProducts(products, product_ids);


                getIndividuals();
            }
        });
    }

    public void getIndividuals() {
        firestoreAdapter.getIndividuals(viewRecordsModel.getCategory_id(), viewRecordsModel.getType()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<String> individuals = new ArrayList<>();
                ArrayList<String> individual_ids = new ArrayList<>();

                for (DocumentSnapshot doc : task.getResult()) {
                    String individual = doc.getString("name");
                    String id = doc.getId();

                    individuals.add(individual);
                    individual_ids.add(id);
                }

                viewRecordsModel.setIndividuals(individuals, individual_ids);

                individuals_loaded.setValue(true);
            }
        });
    }

    public void getRecords(int pos) {
        String id = viewRecordsModel.getIndividual_ids().get(pos);

        firestoreAdapter.getRecords(id, viewRecordsModel.getType(), viewRecordsModel.getCategory_id()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<String> dates = new ArrayList<>();
                    ArrayList<ArrayList<Map<String, Object>>> records = new ArrayList<>();

                    for(DocumentSnapshot doc: task.getResult()){
                        //add individual records
                    }
                }
            }
        });
    }
}