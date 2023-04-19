package com.example.smartsavr;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smartsavr.databinding.FragmentModifyAllowanceBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.QuerySnapshot;

public class ModifyAllowanceBottomSheetDialog extends BottomSheetDialogFragment {

    public static final String TAG = "ModifyAllowanceBottomSheetDialog";

    private FragmentModifyAllowanceBottomSheetBinding binding;

    private final Child child;

    public ModifyAllowanceBottomSheetDialog(Child child) {
        this.child = child;
    }

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
        binding.saveAllowanceButton.setOnClickListener(view -> {
            boolean add = binding.addBalanceRadioButton.isChecked();
            boolean subtract = binding.subtractBalanceRadioButton.isChecked();
            String amountString = (binding.amountFieldEditText.getText().toString());
            int amount = Utils.dollarStringToCents(amountString);

            if (add) {
                child.setAccountBalanceCents(child.getAccountBalanceCents() + amount);
            } else if (subtract) {
                child.setAccountBalanceCents(child.getAccountBalanceCents() - amount);
            }

            ParentChildDetailActivity.childDBReference.collectionReference.whereEqualTo("username", child.getUsername()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        for (Child child : querySnapshot.toObjects(Child.class)) {
                            ParentChildDetailActivity.childDBReference.collectionReference.document(child.getId()).set(this.child);
                        }
                    } else {
                        Log.d(TAG, "querySnapshot is null");
                    }
                }
            });

            dismiss();
        });
    }
}
