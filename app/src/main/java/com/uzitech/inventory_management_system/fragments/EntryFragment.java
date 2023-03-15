package com.uzitech.inventory_management_system.fragments;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.uzitech.inventory_management_system.viewmodels.EntryViewModel;

import java.util.ArrayList;

public class EntryFragment extends Fragment {

    private int type;
    private String product_id;

    EntryViewModel viewModel;

    ArrayAdapter<String> individualsAdapter;

    Spinner individuals_spinner;

    ArrayList<LinearLayout> product_entries;
    LinearLayout product_entry_linerLayout;

    public EntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getInt("type");
            product_id = getArguments().getString("product_id");

            viewModel = new ViewModelProvider(this).get(EntryViewModel.class);

            viewModel.entryModel.setEntry(type, product_id);

            if (type == 0) {
                viewModel.entryModel.setInstruction(getString(R.string.select_manufacturer));
            } else if (type == 1) {

                viewModel.entryModel.setInstruction(getString(R.string.select_customer));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entry, container, false);

        individuals_spinner = view.findViewById(R.id.individuals_spinner);
        product_entry_linerLayout = view.findViewById(R.id.product_entry_linearLayout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.toastMessage.observe(getViewLifecycleOwner(), integer -> toast(getString(integer)));

        viewModel.model.setNavController(Navigation.findNavController(view));

        viewModel.getIndividuals();

        viewModel.setFrame.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                setSpinner();
            }
        });
    }

    void setSpinner() {
        individualsAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                viewModel.entryModel.getIndividuals());

        individualsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        individuals_spinner.setAdapter(individualsAdapter);

        setFrame();

        viewModel.setFrame.setValue(false);

        individuals_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void setFrame() {
        product_entries = new ArrayList<>();

        product_entry_linerLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout entry = null;

        if (viewModel.entryModel.getProducts().size() > 0) {
            for (String product : viewModel.entryModel.getProducts()) {
                int index = viewModel.entryModel.getProducts().indexOf(product);

                entry = (LinearLayout) inflater
                        .inflate(R.layout.product_entry_field, null, false);

                TextView product_name = entry.findViewById(R.id.product_name_textView);
                product_name.setText(product);

                if (type == 1) {
                    EditText prduct_quantity = entry.findViewById(R.id.product_quantity_editText);
                    int quantity = viewModel.entryModel.getProduct_quantities().get(index);

                    prduct_quantity.setHint(String.valueOf(quantity));
                    prduct_quantity.setFilters(new InputFilter[]{
                            new MinMaxFilter(quantity)
                    });
                }

                EditText product_rate = entry.findViewById(R.id.product_rate_editText);
                product_rate.setText(String.valueOf(viewModel.entryModel.getProduct_rates().get(index)));
            }

            product_entries.add(entry);
            product_entry_linerLayout.addView(entry);
        }
    }

    public void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}

class MinMaxFilter implements InputFilter {

    private final int mIntMax;

    public MinMaxFilter(int maxValue) {
        this.mIntMax = maxValue;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(mIntMax, input))
                return null;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean isInRange(int b, int c) {
        return b > 0 ? c >= 0 && c <= b : c >= b && c <= 0;
    }
}