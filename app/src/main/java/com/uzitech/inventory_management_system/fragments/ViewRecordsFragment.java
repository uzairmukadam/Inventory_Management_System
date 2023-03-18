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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uzitech.inventory_management_system.R;
import com.uzitech.inventory_management_system.viewmodels.ViewRecordsViewModel;

import java.util.ArrayList;

public class ViewRecordsFragment extends Fragment {

    private int type;
    private String product_id;
    ViewRecordsViewModel viewModel;
    Spinner individuals_spinner, dates_spinner;
    ArrayAdapter<String> individuals_adapter, dates_adapter;
    LinearLayout records_layout;
    TextView total_cost_textView;
    ArrayList<TextView> product_quantity_textViews, product_rate_textViews;
    FloatingActionButton share_record_fab;

    public ViewRecordsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getInt("type");
            product_id = getArguments().getString("product_id");

            viewModel = new ViewModelProvider(this).get(ViewRecordsViewModel.class);

            viewModel.viewRecordsModel.setModel(type, product_id);

            if (type == 0) {
                viewModel.viewRecordsModel.setInstruction(getString(R.string.select_manufacturer));
            } else if (type == 1) {

                viewModel.viewRecordsModel.setInstruction(getString(R.string.select_customer));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_records, container, false);

        individuals_spinner = view.findViewById(R.id.view_individuals_spinner);
        dates_spinner = view.findViewById(R.id.record_dates_spinner);
        records_layout = view.findViewById(R.id.view_record_container);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.toastMessage.observe(getViewLifecycleOwner(), integer -> toast(getString(integer)));
        viewModel.model.setNavController(Navigation.findNavController(view));

        viewModel.getProducts();

        viewModel.individuals_loaded.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    setIndividualsSpinner();
                    viewModel.individuals_loaded.setValue(false);
                }
            }
        });
    }

    void setIndividualsSpinner() {
        individuals_adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                viewModel.viewRecordsModel.getIndividuals());

        individuals_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        individuals_spinner.setAdapter(individuals_adapter);

        setFrame();

        individuals_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    viewModel.getRecords(i);
                } else {
                    //reset dates spinner
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void setDatesSpinner() {
        dates_adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                viewModel.viewRecordsModel.getRecord_dates());

        dates_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dates_spinner.setAdapter(dates_adapter);

        dates_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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

        LinearLayout total = (LinearLayout) inflater.inflate(R.layout.view_records_total_field, null, false);
        total_cost_textView = total.findViewById(R.id.total_cost_textView);
        total_cost_textView.setText(String.valueOf(0));
        records_layout.addView(total);
    }

    public void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}