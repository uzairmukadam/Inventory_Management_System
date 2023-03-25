package com.uzitech.inventory_management_system.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ViewRecordsModel {

    private int type;
    private String category_id;
    private String instruction, date_instruction;
    private ArrayList<String> individuals, individual_ids, products, product_ids;
    private ArrayList<ArrayList<String>> record_dates;
    private ArrayList<ArrayList<ArrayList<Map<String, Object>>>> records;
    private Date date;

    public void setModel(int type, String category_id) {
        this.type = type;
        this.category_id = category_id;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setDatesInstruction(String date_instruction) {
        this.date_instruction = date_instruction;
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

    public void setRecord_dates(ArrayList<ArrayList<String>> record_dates) {
        this.record_dates = record_dates;

        for (int i = 0; i < record_dates.size(); i++) {
            record_dates.get(i).add(0, date_instruction);
        }
    }

    public void setRecords(ArrayList<ArrayList<ArrayList<Map<String, Object>>>> records) {
        this.records = records;

        for (int i = 0; i < records.size(); i++) {
            records.get(i).add(0, null);
        }
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

    public ArrayList<ArrayList<String>> getRecord_dates() {
        return record_dates;
    }

    public ArrayList<ArrayList<Map<String, Object>>> getIndividualRecord(int individual) {
        if (individual > 0) {
            return records.get(individual);
        } else {
            return null;
        }
    }

    public ArrayList<Map<String, Object>> getDateRecord(int individual, int date) {
        if (individual > 0) {
            return getIndividualRecord(individual).get(date);
        } else {
            return null;
        }
    }

    public ArrayList<ArrayList<ArrayList<Map<String, Object>>>> getAllRecords() {
        return records;
    }
}
