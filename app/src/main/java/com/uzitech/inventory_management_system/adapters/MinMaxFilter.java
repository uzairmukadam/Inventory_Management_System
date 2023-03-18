package com.uzitech.inventory_management_system.adapters;

import android.text.InputFilter;
import android.text.Spanned;

public class MinMaxFilter implements InputFilter {

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
