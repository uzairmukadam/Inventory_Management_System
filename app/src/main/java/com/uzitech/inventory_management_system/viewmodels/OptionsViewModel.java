package com.uzitech.inventory_management_system.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.uzitech.inventory_management_system.R;

public class OptionsViewModel extends MainViewModel {

    public MutableLiveData<Integer> toastMessage;

    public OptionsViewModel() {
        toastMessage = new MutableLiveData<>();
    }

    public void signOut() {
        firestoreAdapter.updateUser(model.getUid(), false).addOnCompleteListener(task -> {
            authAdapter.signout();
            log(false, 6);
            navigateTo(R.id.action_optionsFragment_to_splashFragment);
        });
    }
}
