package com.uzitech.inventory_management_system.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.uzitech.inventory_management_system.R;
import com.uzitech.inventory_management_system.viewmodels.OptionsViewModel;

public class OptionsFragment extends Fragment {

    OptionsViewModel viewModel;
    Button signout_button;
    LinearLayout admin_options_linearLayout;

    public OptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(OptionsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        signout_button = view.findViewById(R.id.sign_out_button);

        if (viewModel.model.getAccessLevel() == 0) {
            admin_options_linearLayout = view.findViewById(R.id.admin_options_linearLayout);
            admin_options_linearLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.toastMessage.observe(getViewLifecycleOwner(), integer -> toast(getString(integer)));

        viewModel.model.setNavController(Navigation.findNavController(view));

        signout_button.setOnClickListener(view1 -> viewModel.signOut());
    }

    public void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}