package com.uzitech.inventory_management_system.fragments;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.uzitech.inventory_management_system.R;
import com.uzitech.inventory_management_system.adapters.MinMaxFilter;
import com.uzitech.inventory_management_system.viewmodels.CreateRecordViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateRecordFragment extends Fragment {

    private int type;
    CreateRecordViewModel viewModel;
    ArrayAdapter<String> individualsAdapter;
    Spinner individuals_spinner;
    TextView date_textView;
    Button date_button, submit;
    ArrayList<LinearLayout> product_records;
    LinearLayout product_record_linerLayout;
    View new_individual_layout;
    Date date;
    SimpleDateFormat df;

    public CreateRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        date = Calendar.getInstance().getTime();

        df = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

        if (getArguments() != null) {
            type = getArguments().getInt("type");
            String product_id = getArguments().getString("product_id");

            viewModel = new ViewModelProvider(this).get(CreateRecordViewModel.class);
            viewModel.createRecordModel.setModel(type, product_id);
            viewModel.createRecordModel.setDate(date);

            if (type == 0) {
                viewModel.createRecordModel.setInstruction(getString(R.string.select_manufacturer));
            } else if (type == 1) {

                viewModel.createRecordModel.setInstruction(getString(R.string.select_customer));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_record, container, false);

        individuals_spinner = view.findViewById(R.id.individuals_spinner);
        product_record_linerLayout = view.findViewById(R.id.product_entry_linearLayout);
        date_textView = view.findViewById(R.id.date_textView);
        new_individual_layout = view.findViewById(R.id.new_individual_layout);
        submit = view.findViewById(R.id.submit_entry_button);

        date_textView.setText(df.format(date));
        date_button = view.findViewById(R.id.edit_date_button);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.toastMessage.observe(getViewLifecycleOwner(), integer -> toast(getString(integer)));
        viewModel.model.setNavController(Navigation.findNavController(view));

        viewModel.getIndividuals();

        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        submit.setOnClickListener(view1 -> viewModel.createRecord(individuals_spinner.getSelectedItemPosition(), product_records));

        viewModel.setFrame.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                setIndividualsSpinner();
            }
        });
    }

    void setIndividualsSpinner() {
        individualsAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                viewModel.createRecordModel.getIndividuals());

        individualsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        individuals_spinner.setAdapter(individualsAdapter);

        setFrame();

        viewModel.setFrame.setValue(false);

        individuals_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (viewModel.createRecordModel.getIndividuals().get(i).equals("New +")) {
                    new_individual_layout.setVisibility(View.VISIBLE);
                    setNewIndividual();
                } else {
                    new_individual_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void setNewIndividual() {
        // pending task
    }

    void setFrame() {
        product_records = new ArrayList<>();

        product_record_linerLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout entry = null;

        if (viewModel.createRecordModel.getProducts().size() > 0) {
            for (String product : viewModel.createRecordModel.getProducts()) {
                int index = viewModel.createRecordModel.getProducts().indexOf(product);

                entry = (LinearLayout) inflater
                        .inflate(R.layout.product_entry_field, null, false);

                TextView product_name = entry.findViewById(R.id.product_name_textView);
                product_name.setText(product);

                if (type == 1) {
                    EditText prduct_quantity = entry.findViewById(R.id.product_quantity_editText);
                    int quantity = viewModel.createRecordModel.getProduct_quantities().get(index);

                    prduct_quantity.setHint(String.valueOf(quantity));
                    prduct_quantity.setFilters(new InputFilter[]{
                            new MinMaxFilter(quantity)
                    });
                }

                EditText product_rate = entry.findViewById(R.id.product_rate_editText);
                product_rate.setText(String.valueOf(viewModel.createRecordModel.getProduct_rates().get(index)));
            }

            product_records.add(entry);
            product_record_linerLayout.addView(entry);
        }
    }

    public void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}

