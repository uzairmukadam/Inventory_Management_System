package com.uzitech.inventory_management_system.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.uzitech.inventory_management_system.R;
import com.uzitech.inventory_management_system.models.EntryModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public void submitEntry(Date date, int index, ArrayList<Map<String, Object>> entries, Map<String, String> new_individual) {
        if (new_individual != null) {
            String individual_name = new_individual.get("name");

            if (individual_name.trim().isEmpty()) {
                toastMessage.setValue(R.string.check_individual_name);
            } else {
                addNewIndividual(new_individual, entries, date);
            }
            //update quantity
        } else {
            String id = entryModel.getIndividual_ids().get(index);

            addEntry(entries, id, date);
            //update quantity
        }
    }

    private void addNewIndividual(Map<String, String> individual, ArrayList<Map<String, Object>> entries, Date date) {
        firestoreAdapter.addIndividual(entryModel.getType(), individual).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    String id = task.getResult().getId();

                    addEntry(entries, id, date);
                }
            }
        });
    }

    private void addEntry(ArrayList<Map<String, Object>> entries, String id, Date date) {
        Map<String, Object> entry = new HashMap<>();

        entry.put("Timestamp", date);

        if (entryModel.getType() == 0) {
            entry.put("manufacturer", id);
        } else {
            entry.put("customer", id);
        }

        entry.put("entry", entries);

        firestoreAdapter.addEntry(entryModel.getType(), entry).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    updateProduct(entries);
                }
            }
        });
    }

    private void updateProduct(ArrayList<Map<String, Object>> entries) {
        Map<String, Integer> products = new HashMap<>();

        for (Map<String, Object> entry : entries) {
            String id = (String) entry.get("product_id");
            int quantity = (int) entry.get("quantity");

            products.put(id, quantity);
        }

        firestoreAdapter.updateProductQuantity(entryModel.getType(), products).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    navigateTo(R.id.action_entryFragment_to_dashboardFragment);
                }
            }
        });
    }
}
