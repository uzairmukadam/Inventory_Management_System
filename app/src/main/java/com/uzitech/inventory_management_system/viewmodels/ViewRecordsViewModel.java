package com.uzitech.inventory_management_system.viewmodels;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.uzitech.inventory_management_system.models.ViewRecordsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ViewRecordsViewModel extends MainViewModel {

    public ViewRecordsModel viewRecordsModel;

    public MutableLiveData<Integer> toastMessage;
    public MutableLiveData<Boolean> data_loaded;

    ArrayList<Integer> quantities;
    ArrayList<Double> rates;

    public ViewRecordsViewModel() {
        toastMessage = new MutableLiveData<>();
        viewRecordsModel = new ViewRecordsModel();

        data_loaded = new MutableLiveData<>(false);
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

                getRecords();
            }
        });
    }

    public void getRecords() {

        firestoreAdapter.getAllRecords(viewRecordsModel.getType(), viewRecordsModel.getCategory_id()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

                ArrayList<ArrayList<String>> dates = new ArrayList<>();
                ArrayList<ArrayList<ArrayList<Map<String, Object>>>> records = new ArrayList<>();

                for (int i = 0; i < viewRecordsModel.getIndividual_ids().size(); i++) {
                    dates.add(i, new ArrayList<>());
                    records.add(i, new ArrayList<>());
                }


                for (DocumentSnapshot doc : task.getResult()) {
                    int index = viewRecordsModel.getIndividual_ids().indexOf(doc.getString("id"));

                    Date timestamp = Objects.requireNonNull(doc.getTimestamp("timestamp")).toDate();

                    dates.get(index).add(df.format(timestamp));
                    records.get(index).add((ArrayList<Map<String, Object>>) doc.getData().get("record"));
                }

                viewRecordsModel.setRecord_dates(dates);
                viewRecordsModel.setRecords(records);

                data_loaded.setValue(true);
            }
        });
    }

    public Map<String, Object> getRecordData(int individual, int date) {
        double total = 0.0;

        quantities = new ArrayList<>();
        rates = new ArrayList<>();

        for (int i = 0; i < viewRecordsModel.getProduct_ids().size(); i++) {
            quantities.add(0);
            rates.add(0.0);
        }

        if (individual > 0) {
            if (date > 0) {
                ArrayList<Map<String, Object>> data = viewRecordsModel.getDateRecord(individual, date);

                for (Map<String, Object> product_record : data) {
                    int index = viewRecordsModel.getProduct_ids().indexOf(product_record.get("product_id"));

                    long quantity = (long) product_record.get("quantity");
                    double rate = (double) product_record.get("rate");

                    quantities.set(index, (int) quantity);
                    rates.set(index, rate);

                    total += quantity * rate;
                }
            } else {
                ArrayList<ArrayList<Map<String, Object>>> data = viewRecordsModel.getIndividualRecord(individual);

                for (ArrayList<Map<String, Object>> date_record : data) {
                    if (date_record != null) {
                        for (Map<String, Object> product_record : date_record) {
                            int index = viewRecordsModel.getProduct_ids().indexOf(product_record.get("product_id"));

                            long quantity = (long) product_record.get("quantity");
                            double rate = (double) product_record.get("rate");

                            int temp_quantity = quantities.get(index);
                            temp_quantity += quantity;
                            quantities.set(index, temp_quantity);

                            double temp_rate = rates.get(index);
                            temp_rate += rate;
                            rates.set(index, temp_rate);

                            total += quantity * rate;
                        }
                    }
                }

                for (int i = 0; i < rates.size(); i++) {
                    double temp_rate = rates.get(i);
                    temp_rate /= data.size() - 1;
                    rates.set(i, temp_rate);
                }
            }
        } else {
            ArrayList<ArrayList<ArrayList<Map<String, Object>>>> data = viewRecordsModel.getAllRecords();

            for (ArrayList<ArrayList<Map<String, Object>>> ind : data) {
                for (ArrayList<Map<String, Object>> date_record : ind) {
                    if (date_record != null) {

                        for (Map<String, Object> product_record : date_record) {
                            int index = viewRecordsModel.getProduct_ids().indexOf(product_record.get("product_id"));

                            long quantity = (long) product_record.get("quantity");
                            double rate = (double) product_record.get("rate");

                            int temp_quantity = quantities.get(index);
                            temp_quantity += quantity;
                            quantities.set(index, temp_quantity);

                            double temp_rate = rates.get(index);
                            temp_rate += rate;
                            rates.set(index, temp_rate);

                            total += quantity * rate;
                        }
                    }
                }
            }

            for (int i = 0; i < rates.size(); i++) {
                double temp_rate = rates.get(i);
                temp_rate /= data.size();
                rates.set(i, temp_rate);
            }
        }

        Map<String, Object> recordData = new HashMap<>();
        recordData.put("quantities", quantities);
        recordData.put("rates", rates);
        recordData.put("total", total);

        return recordData;
    }
}