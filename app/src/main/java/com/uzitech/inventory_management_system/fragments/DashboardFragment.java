package com.uzitech.inventory_management_system.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.uzitech.inventory_management_system.viewmodels.DashboardViewModel;

import java.util.Map;

public class DashboardFragment extends Fragment {

    private DashboardViewModel viewModel;
    Spinner category_spinner;

    ArrayAdapter<String> categoriesAdapter;
    Button entry_purchase_button, entry_sale_button, view_purchases_button, view_sales_button, options_button;
    LinearLayout view_linearLayout, product_count_linearLayout;

    boolean execute_onResume;

    int category_index;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        execute_onResume = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        category_spinner = view.findViewById(R.id.category_spinner);
        entry_purchase_button = view.findViewById(R.id.entry_purchase_button);
        entry_sale_button = view.findViewById(R.id.entry_sale_button);
        view_purchases_button = view.findViewById(R.id.view_purchases_button);
        view_sales_button = view.findViewById(R.id.view_sales_button);
        options_button = view.findViewById(R.id.options_button);
        view_linearLayout = view.findViewById(R.id.view_linearLayout);
        product_count_linearLayout = view.findViewById(R.id.product_count_linearLayout);

        if (viewModel.model.getAccessLevel() > 1) {
            view_linearLayout.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.toastMessage.observe(getViewLifecycleOwner(), integer -> toast(getString(integer)));

        viewModel.model.setNavController(Navigation.findNavController(view));

        if (viewModel.model.getAccessLevel() > 1) {
            view_linearLayout.setVisibility(View.GONE);
        }

        options_button.setOnClickListener(view1 -> viewModel.navigateTo(R.id.action_dashboardFragment_to_optionsFragment));

        entry_purchase_button.setOnClickListener(view1 -> loadPurchaseEntry());
        entry_sale_button.setOnClickListener(view1 -> loadSaleEntry());

        if (viewModel.model.getAccessLevel() < 2) {
            view_purchases_button.setOnClickListener(view1 -> loadPurchaseView());
            view_sales_button.setOnClickListener(view1 -> loadSaleView());
        }

        viewModel.categoriesReady.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                setSpinner();
                viewModel.categoriesReady.setValue(false);
            }
        });

        viewModel.productsReady.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                setCountFrame();
                viewModel.productsReady.setValue(false);
            }
        });
    }

    void loadPurchaseEntry() {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0);
        bundle.putString("product_id", viewModel.dashboardModel.getCategoryId(category_index));
        viewModel.model.getNavController().navigate(R.id.action_dashboardFragment_to_entryFragment, bundle);
    }

    void loadSaleEntry() {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putString("product_id", viewModel.dashboardModel.getCategoryId(category_index));
        viewModel.model.getNavController().navigate(R.id.action_dashboardFragment_to_entryFragment, bundle);
    }

    void loadPurchaseView() {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0);
        bundle.putString("product_id", viewModel.dashboardModel.getCategoryId(category_index));
        viewModel.model.getNavController().navigate(R.id.action_dashboardFragment_to_viewFragment, bundle);
    }

    void loadSaleView() {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putString("product_id", viewModel.dashboardModel.getCategoryId(category_index));
        viewModel.model.getNavController().navigate(R.id.action_dashboardFragment_to_viewFragment, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (execute_onResume) {
            setSpinner();
            category_spinner.setSelection(category_index);
            viewModel.categoriesReady.setValue(false);
        } else {
            execute_onResume = true;
        }

    }

    void setSpinner() {
        categoriesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                viewModel.dashboardModel.getCategories());

        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        category_spinner.setAdapter(categoriesAdapter);

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category_index = i;
                viewModel.setProducts(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void setCountFrame() {
        product_count_linearLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout product_info;

        if (viewModel.dashboardModel.getProducts().size() > 0) {
            for (Map.Entry<String, Integer> product : viewModel.dashboardModel.getProducts().entrySet()) {
                product_info = (LinearLayout) inflater
                        .inflate(R.layout.product_quantity_field, null, false);

                TextView label = product_info.findViewById(R.id.product_name);
                label.setText(product.getKey());

                TextView itemCount = product_info.findViewById(R.id.product_quantity);
                itemCount.setText(String.valueOf(product.getValue()));

                product_count_linearLayout.addView(product_info);
            }
        } else {
            product_info = (LinearLayout) inflater
                    .inflate(R.layout.no_product_field, null, false);
            product_count_linearLayout.addView(product_info);
        }
    }

    public void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}