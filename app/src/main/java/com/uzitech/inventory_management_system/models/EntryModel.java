package com.uzitech.inventory_management_system.models;

import java.util.ArrayList;

public class EntryModel {

    private int type;
    private String category_id;

    private String instruction;

    private ArrayList<String> individuals, individual_ids, products, product_ids;
    private ArrayList<Double> product_rates;
    private ArrayList<Integer> product_quantities;


    public EntryModel() {
        individuals = new ArrayList<>();
        individual_ids = new ArrayList<>();
        products = new ArrayList<>();
        product_ids = new ArrayList<>();
        product_quantities = new ArrayList<>();
    }

    public void setEntry(int type, String category_id) {
        this.type = type;
        this.category_id = category_id;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setIndividuals(ArrayList<String> individuals) {
        this.individuals = individuals;
        this.individuals.add(0, instruction);
    }

    public void setIndividual_ids(ArrayList<String> individual_ids) {
        this.individual_ids = individual_ids;
        this.individual_ids.add(0, "None");

    }

    public void setProduct_rates(ArrayList<Double> product_rates) {
        this.product_rates = product_rates;
    }

    public int getType() {
        return type;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }

    public void setProduct_ids(ArrayList<String> product_ids) {
        this.product_ids = product_ids;
    }

    public void setProduct_quantities(ArrayList<Integer> product_quantities) {
        this.product_quantities = product_quantities;
    }

    public ArrayList<String> getIndividuals() {
        return individuals;
    }

    public ArrayList<String> getIndividual_ids() {
        return individual_ids;
    }

    public ArrayList<Double> getProduct_rates() {
        return product_rates;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public ArrayList<String> getProduct_ids() {
        return product_ids;
    }

    public ArrayList<Integer> getProduct_quantities() {
        return product_quantities;
    }
}
