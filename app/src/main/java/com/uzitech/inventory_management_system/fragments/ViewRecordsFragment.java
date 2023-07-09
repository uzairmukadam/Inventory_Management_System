package com.uzitech.inventory_management_system.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uzitech.inventory_management_system.R;
import com.uzitech.inventory_management_system.viewmodels.ViewRecordsViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class ViewRecordsFragment extends Fragment {

    ViewRecordsViewModel viewModel;
    Spinner individuals_spinner, dates_spinner;
    ArrayAdapter<String> individuals_adapter, dates_adapter;
    LinearLayout records_layout, total_layout;
    TextView total_cost_textView;
    ArrayList<TextView> product_quantity_textViews, product_rate_textViews;
    FloatingActionButton share_record_fab;

    DecimalFormat decimalFormat;

    public ViewRecordsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int type = getArguments().getInt("type");
            String product_id = getArguments().getString("product_id");

            viewModel = new ViewModelProvider(this).get(ViewRecordsViewModel.class);

            viewModel.viewRecordsModel.setModel(type, product_id);

            if (type == 0) {
                viewModel.viewRecordsModel.setInstruction(getString(R.string.select_manufacturer));
            } else if (type == 1) {

                viewModel.viewRecordsModel.setInstruction(getString(R.string.select_customer));
            }

            viewModel.viewRecordsModel.setDatesInstruction(getString(R.string.select_date));

            decimalFormat = new DecimalFormat("0.00");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_records, container, false);

        individuals_spinner = view.findViewById(R.id.view_individuals_spinner);
        dates_spinner = view.findViewById(R.id.record_dates_spinner);
        records_layout = view.findViewById(R.id.view_record_container);
        total_layout = view.findViewById(R.id.view_records_total_field);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.toastMessage.observe(getViewLifecycleOwner(), integer -> toast(getString(integer)));
        viewModel.model.setNavController(Navigation.findNavController(view));

        viewModel.getProducts();

        viewModel.data_loaded.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                setIndividualsSpinner();
                setFrame();
            }
        });
    }

    void setIndividualsSpinner() {
        individuals_adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                viewModel.viewRecordsModel.getIndividuals());

        individuals_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        individuals_spinner.setAdapter(individuals_adapter);

        individuals_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    setDatesSpinner(i);
                } else {
                    //reset dates spinner
                    dates_spinner.setVisibility(View.GONE);
                    setValues(i, 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void setDatesSpinner(int pos) {
        dates_adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                viewModel.viewRecordsModel.getRecord_dates().get(pos));

        dates_spinner.setVisibility(View.VISIBLE);

        dates_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dates_spinner.setAdapter(dates_adapter);

        dates_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setValues(individuals_spinner.getSelectedItemPosition(), i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void setFrame() {

        product_quantity_textViews = new ArrayList<>();
        product_rate_textViews = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (String product : viewModel.viewRecordsModel.getProducts()) {
            LinearLayout record = (LinearLayout) inflater.inflate(R.layout.view_records_fields, null, false);

            TextView name, quantity, rate;

            name = record.findViewById(R.id.view_product_name);
            quantity = record.findViewById(R.id.view_product_quantity);
            rate = record.findViewById(R.id.view_product_rate);

            name.setText(product);
            quantity.setText(String.valueOf(0));
            rate.setText(String.valueOf(0));

            product_quantity_textViews.add(quantity);
            product_rate_textViews.add(rate);
            records_layout.addView(record);
        }

        total_cost_textView = total_layout.findViewById(R.id.total_cost_textView);
        total_cost_textView.setText(String.valueOf(0));

        viewModel.data_loaded.setValue(false);
    }

    void setValues(int individual, int date) {
        Map<String, Object> data = viewModel.getRecordData(individual, date);

        ArrayList<Integer> quantities = (ArrayList<Integer>) data.get("quantities");
        ArrayList<Double> rates = (ArrayList<Double>) data.get("rates");
        double total = (double) data.get("total");

        for (int i = 0; i < viewModel.viewRecordsModel.getProduct_ids().size(); i++) {
            TextView quantity_textView = product_quantity_textViews.get(i);
            TextView rate_textView = product_rate_textViews.get(i);

            assert quantities != null;
            assert rates != null;
            quantity_textView.setText(String.valueOf(quantities.get(i)));
            rate_textView.setText(decimalFormat.format(rates.get(i)));
            total_cost_textView.setText(decimalFormat.format(total));
        }
    }

    public void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}