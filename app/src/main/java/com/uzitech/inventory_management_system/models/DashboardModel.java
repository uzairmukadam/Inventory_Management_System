package com.uzitech.inventory_management_system.models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardModel {

    ArrayList<String> categories, category_ids;
    Map<String, Integer> products;

    public DashboardModel() {
        categories = new ArrayList<>();
        category_ids = new ArrayList<>();
    }

    public void setCategories(QuerySnapshot querySnapshot) {
        for (DocumentSnapshot doc : querySnapshot) {
            String category = doc.getString("name");
            String id = doc.getId();

            categories.add(category);
            category_ids.add(id);
        }
    }

    public void setProducts(QuerySnapshot querySnapshot) {
        products = new HashMap<>();

        for (DocumentSnapshot doc : querySnapshot) {
            String name = doc.getString("name");
            int quantity = Math.toIntExact(doc.getLong("quantity"));

            products.put(name, quantity);
        }
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public String getCategoryId(int index) {
        return category_ids.get(index);
    }

    public Map<String, Integer> getProducts() {
        return products;
    }
}
