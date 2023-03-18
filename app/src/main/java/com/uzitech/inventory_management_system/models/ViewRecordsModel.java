package com.uzitech.inventory_management_system.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ViewRecordsModel {

    private int type;
    private String category_id;
    private String instruction;
    private ArrayList<String> individuals, individual_ids, products, product_ids, record_dates;
    private ArrayList<Map<String, Object>> records;
    private Date date;

    public void setModel(int type, String category_id) {
        this.type = type;
        this.category_id = category_id;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setIndividuals(ArrayList<String> individuals, ArrayList<String> individual_ids) {
        this.individuals = individuals;
        this.individuals.add(0, instruction);

        this.individual_ids = individual_ids;
        this.individual_ids.add(0, "None");
    }

    public void setProducts(ArrayList<String> products, ArrayList<String> product_ids) {
        this.products = products;
        this.product_ids = product_ids;
    }

    public void setRecord_dates(ArrayList<String> record_dates) {
        this.record_dates = record_dates;
    }

    public void setRecords(ArrayList<Map<String, Object>> records) {
        this.records = records;
    }

    public int getType() {
        return type;
    }

    public String getCategory_id() {
        return category_id;
    }

    public ArrayList<String> getIndividuals() {
        return individuals;
    }

    public ArrayList<String> getIndividual_ids() {
        return individual_ids;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public ArrayList<String> getProduct_ids() {
        return product_ids;
    }

    public ArrayList<String> getRecord_dates() {
        return record_dates;
    }

    public ArrayList<Map<String, Object>> getRecords(String date) {
        return null;
    }
}
