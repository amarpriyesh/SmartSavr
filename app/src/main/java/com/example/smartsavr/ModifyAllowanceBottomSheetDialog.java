package com.example.smartsavr;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    private OnSaveListener onSaveListener;

    public interface OnSaveListener {
        void acceptBalance(int balanceCents);
    }

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

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try {
            onSaveListener = (OnSaveListener) getActivity();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }

    private void setClickListeners() {
        binding.saveAllowanceButton.setOnClickListener(view -> {
            boolean add = binding.addBalanceRadioButton.isChecked();
            boolean subtract = binding.subtractBalanceRadioButton.isChecked();
            String amountString = (binding.amountFieldEditText.getText().toString());
            int amount = Utils.dollarStringToCents(amountString);


            if(TextUtils.isEmpty(amountString) || amount < 0)
            {
                Toast.makeText(getActivity(),"Enter valid amount",Toast.LENGTH_SHORT).show();
                return;
            }

            if (add) {
                child.setAccountBalanceCents(child.getAccountBalanceCents() + amount);
            }
            if (subtract) {
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

            onSaveListener.acceptBalance(child.getAccountBalanceCents());
            Log.d(TAG, "onClick: " + child.getParentId() + " " + child.getUsername());

            dismiss();
        });
    }
}
