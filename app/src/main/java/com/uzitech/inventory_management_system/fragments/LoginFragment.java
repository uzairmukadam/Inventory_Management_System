package com.uzitech.inventory_management_system.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.uzitech.inventory_management_system.R;
import com.uzitech.inventory_management_system.viewmodels.LoginViewModel;


public class LoginFragment extends Fragment {

    private LoginViewModel viewModel;

    EditText email_editText, password_editText;
    Button login_button;
    ProgressBar loading_bar;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email_editText = view.findViewById(R.id.email_editText);
        password_editText = view.findViewById(R.id.passowrd_editText);
        login_button = view.findViewById(R.id.login_button);
        loading_bar = view.findViewById(R.id.loading_bar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.toastMessage.observe(getViewLifecycleOwner(), integer -> toast(getString(integer)));

        viewModel.model.setNavController(Navigation.findNavController(view));

        login_button.setOnClickListener(view1 -> {
            String email = email_editText.getText().toString();
            String password = password_editText.getText().toString();

            viewModel.login(email, password);
        });

        viewModel.inProgress.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                showProgressBar();
            } else {
                showLoginButton();
            }
        });

        viewModel.resetField.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                resetFields();
                viewModel.resetField.setValue(false);
            }
        });
    }

    void showProgressBar() {
        login_button.setVisibility(View.GONE);
        loading_bar.setVisibility(View.VISIBLE);
    }

    void showLoginButton() {
        login_button.setVisibility(View.VISIBLE);
        loading_bar.setVisibility(View.GONE);
    }

    void resetFields() {
        email_editText.setText("");
        password_editText.setText("");
        email_editText.requestFocus();
    }

    public void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}