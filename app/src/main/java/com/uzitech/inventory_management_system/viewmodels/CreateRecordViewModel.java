package com.uzitech.inventory_management_system.viewmodels;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.uzitech.inventory_management_system.R;
import com.uzitech.inventory_management_system.models.CreateRecordModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateRecordViewModel extends MainViewModel {

    public CreateRecordModel createRecordModel;
    public MutableLiveData<Integer> toastMessage;
    public MutableLiveData<Boolean> setFrame;

    public CreateRecordViewModel() {
        toastMessage = new MutableLiveData<>();

        setFrame = new MutableLiveData<>(false);

        createRecordModel = new CreateRecordModel();
    }

    public void getIndividuals() {
        firestoreAdapter.getIndividuals(createRecordModel.getCategory_id(), createRecordModel.getType()).addOnCompleteListener(task -> {
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

                createRecordModel.setIndividuals(individuals, individual_ids);

                getProducts();
            }
        });
    }

    private void getProducts() {
        firestoreAdapter.getProducts(createRecordModel.getCategory_id()).addOnCompleteListener(task -> {
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
                    if (createRecordModel.getType() == 0) {
                        rate = doc.getDouble("purchase_rate");
                    } else if (createRecordModel.getType() == 1) {
                        rate = doc.getDouble("sale_rate");
                    }

                    products.add(product_name);
                    product_ids.add(id);
                    product_quantities.add(quantity);
                    product_rates.add(rate);
                }

                createRecordModel.setProducts(products, product_ids, product_quantities, product_rates);

                setFrame.setValue(true);
            }
        });
    }

    public void createRecord(int index, ArrayList<LinearLayout> product_records) {
        ArrayList<Map<String, Object>> records = new ArrayList<>();

        for (LinearLayout product : product_records) {

            EditText product_quantity_editText = product.findViewById(R.id.product_quantity_editText);
            String quantity_val = product_quantity_editText.getText().toString();
            int quantity = 0;
            if (!quantity_val.trim().isEmpty()) {
                quantity = Integer.parseInt(product_quantity_editText.getText().toString());
            }

            if (quantity != 0) {
                Map<String, Object> product_record = new HashMap<>();

                TextView product_name_textView = product.findViewById(R.id.product_name_textView);
                String product_name = product_name_textView.getText().toString();
                String product_id = createRecordModel.getProduct_ids()
                        .get(createRecordModel.getProducts().indexOf(product_name));


                EditText product_rate_editText = product.findViewById(R.id.product_rate_editText);
                Double rate = Double.valueOf(product_rate_editText.getText().toString());

                product_record.put("product_id", product_id);
                product_record.put("quantity", quantity);
                product_record.put("rate", rate);

                records.add(product_record);
            }
        }

        Map<String, String> new_user = null;

        if (index == createRecordModel.getIndividuals().size() - 1) {
            new_user = new HashMap<>();
        }

        submitRecord(createRecordModel.getDate(), index, records, new_user);
    }

    public void submitRecord(Date date, int index, ArrayList<Map<String, Object>> records, Map<String, String> new_individual) {
        if (new_individual != null) {
            String individual_name = new_individual.get("name");

            assert individual_name != null;
            if (individual_name.trim().isEmpty()) {
                toastMessage.setValue(R.string.check_individual_name);
            } else {
                addNewIndividual(new_individual, records, date);
            }
        } else {
            String id = createRecordModel.getIndividual_ids().get(index);

            addRecord(records, id, date);
        }
    }

    private void addNewIndividual(Map<String, String> individual, ArrayList<Map<String, Object>> records, Date date) {
        firestoreAdapter.addIndividual(createRecordModel.getType(), individual).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String id = task.getResult().getId();
                addRecord(records, id, date);
            }
        });
    }

    private void addRecord(ArrayList<Map<String, Object>> records, String id, Date date) {
        Map<String, Object> entry = new HashMap<>();

        entry.put("timestamp", date);
        entry.put("category", createRecordModel.getCategory_id());
        entry.put("id", id);
        entry.put("record", records);
        entry.put("user", model.getUid());

        firestoreAdapter.addEntry(createRecordModel.getType(), entry).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateProduct(records);
            }
        });
    }

    private void updateProduct(ArrayList<Map<String, Object>> records) {
        Map<String, Integer> products = new HashMap<>();

        for (Map<String, Object> entry : records) {
            String id = (String) entry.get("product_id");
            int quantity = (int) entry.get("quantity");

            products.put(id, quantity);
        }

        firestoreAdapter.updateProductQuantity(createRecordModel.getType(), products).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                toastMessage.setValue(R.string.record_added);
                navigateTo(R.id.action_entryFragment_to_dashboardFragment);
            }
        });
    }
}
