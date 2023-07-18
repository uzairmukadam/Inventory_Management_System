package com.uzitech.inventory_management_system.models;

import android.view.View;

import java.util.ArrayList;
import java.util.Date;

public class CreateRecordModel {

    private int type;
    private String category_id;
    private String instruction;
    private ArrayList<String> individuals, individual_ids, products, product_ids;
    private ArrayList<Double> product_rates;
    private ArrayList<Integer> product_quantities;
    private Date date;

    private View new_individual;

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

    public void setProducts(ArrayList<String> products, ArrayList<String> product_ids, ArrayList<Integer> product_quantities, ArrayList<Double> product_rates) {
        this.products = products;
        this.product_ids = product_ids;
        this.product_quantities = product_quantities;
        this.product_rates = product_rates;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setNew_individual(View new_individual){
        this.new_individual = new_individual;
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

    public ArrayList<Integer> getProduct_quantities() {
        return product_quantities;
    }

    public ArrayList<Double> getProduct_rates() {
        return product_rates;
    }

    public Date getDate() {
        return date;
    }

    public View getNew_individual(){
        return new_individual;
    }
}
