package com.example.smartsavr;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smartsavr.databinding.FragmentChoreBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class ChoreBottomSheetDialog extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener {
    // todo: perform input validation (date must be in the future, chore name must be non-empty, reward must be a valid dollar amount (2 decimal places) with a max of like $1000 or something

    public static final String TAG = "ChoreBottomSheetDialog";
    private FragmentChoreBottomSheetBinding binding;

    // todo: Use viewmodel for state
    private int year;
    private int month;
    private int dayOfMonth;

    private Calendar calendar;

    private Chore chore;


    private ChoreBottomSheetDialog ob;


    public ChoreBottomSheetDialog() {

    }

    public ChoreBottomSheetDialog(Chore chore) {

        this.chore = chore;
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) requireView().getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (this.chore != null){
            binding.choreNameFieldEditText.setText(chore.getTaskName());
           binding.rewardFieldEditText.setText(String.format("%s",chore.getRewardCents()), TextView.BufferType.EDITABLE);
           binding.choreTitleTextView.setText("Edit Chore");
           binding.saveChoreButton.setOnClickListener(v -> {
               chore.setTaskName(binding.choreNameFieldEditText.getText().toString());
               chore.setRewardCents(Integer.parseInt(binding.rewardFieldEditText.getText().toString()));
               chore.setDeadline(calendar==null?System.currentTimeMillis():calendar.getTimeInMillis());
               ParentChildChoresActivity.toDoCompletedDBReference.collectionReference.document(chore.getId()).set(chore);

               dismiss();
           });
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChoreBottomSheetBinding.inflate(inflater, container, false);

        Calendar calendar = Calendar.getInstance();

        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // TODO: do either add chore or edit chore depending on the screen
        binding.choreTitleTextView.setText(R.string.add_chore_title);
        binding.pickDateButton.setText(getDateString(calendar));

        setClickListeners();



        return binding.getRoot();
    }

    private void
    setClickListeners() {
        binding.saveChoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chore chore = new Chore(ParentChildChoresActivity.childID,calendar==null?System.currentTimeMillis():calendar.getTimeInMillis(),binding.choreNameFieldEditText.getText().toString(),Integer.parseInt(binding.rewardFieldEditText.getText().toString()));
                ParentChildChoresActivity.toDoCompletedDBReference.collectionReference.add(chore);
                dismiss();

            }

        });

        binding.pickDateButton.setOnClickListener(view -> {

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                    this, year, month, dayOfMonth);
            datePickerDialog.show();
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;

        binding.pickDateButton.setText(getDateString(calendar));
    }

    private static CharSequence getDateString(Calendar calendar) {
        return DateFormat.format("MM/dd/yyyy", calendar.getTime());
    }


}
