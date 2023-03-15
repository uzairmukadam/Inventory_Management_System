package com.uzitech.inventory_management_system.viewmodels;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.uzitech.inventory_management_system.models.EntryModel;

import java.util.ArrayList;

public class EntryViewModel extends MainViewModel {

    public EntryModel entryModel;

    public MutableLiveData<Integer> toastMessage;

    public MutableLiveData<Boolean> setFrame;

    public EntryViewModel() {
        toastMessage = new MutableLiveData<>();

        setFrame = new MutableLiveData<>(false);

        entryModel = new EntryModel();
    }

    public void getIndividuals() {
        firestoreAdapter.getIndividuals(entryModel.getCategory_id(), entryModel.getType()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<String> individuals = new ArrayList<>();
                ArrayList<String> individual_ids = new ArrayList<>();

                for (DocumentSnapshot doc : task.getResult()) {
                    String individual = doc.getString("name");
                    String id = doc.getId();

                    individuals.add(individual);
                    individual_ids.add(id);
                }

                individuals.add("New +");
                individual_ids.add("None");

                entryModel.setIndividuals(individuals);
                entryModel.setIndividual_ids(individual_ids);

                getProducts();
            }
        });
    }

    private void getProducts() {
        firestoreAdapter.getProducts(entryModel.getCategory_id()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<String> products = new ArrayList<>();
                ArrayList<String> product_ids = new ArrayList<>();
                ArrayList<Integer> product_quantities = new ArrayList<>();
                ArrayList<Double> product_rates = new ArrayList<>();

                for (DocumentSnapshot doc : task.getResult()) {
                    String product_name = doc.getString("name");
                    String id = doc.getId();
                    int quantity = Math.toIntExact(doc.getLong("quantity"));

                    Double rate = null;
                    if (entryModel.getType() == 0) {
                        rate = doc.getDouble("purchase_rate");
                    } else if (entryModel.getType() == 1) {
                        rate = doc.getDouble("sale_rate");
                    }

                    products.add(product_name);
                    product_ids.add(id);
                    product_quantities.add(quantity);
                    product_rates.add(rate);
                }

                entryModel.setProducts(products);
                entryModel.setProduct_ids(product_ids);
                entryModel.setProduct_quantities(product_quantities);
                entryModel.setProduct_rates(product_rates);


                setFrame.setValue(true);
            }
        });
    }
}
