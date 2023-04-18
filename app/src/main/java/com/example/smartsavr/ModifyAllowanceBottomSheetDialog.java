package com.example.smartsavr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smartsavr.databinding.FragmentModifyAllowanceBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModifyAllowanceBottomSheetDialog extends BottomSheetDialogFragment {

    public static final String TAG = "ModifyAllowanceBottomSheetDialog";

    private FragmentModifyAllowanceBottomSheetBinding binding;

    public ModifyAllowanceBottomSheetDialog() {}

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) requireView().getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentModifyAllowanceBottomSheetBinding.inflate(inflater, container, false);
        setClickListeners();
        return binding.getRoot();
    }

    private void setClickListeners() {
        binding.saveAllowanceButton.setOnClickListener(view -> dismiss());
    }
}
