package com.uzitech.inventory_management_system.viewmodels;

import androidx.lifecycle.MutableLiveData;

public class EntryViewModel extends MainViewModel {

    public MutableLiveData<Integer> toastMessage;

    public EntryViewModel() {
        toastMessage = new MutableLiveData<>();
    }
}
