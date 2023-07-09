package com.example.smartsavr;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smartsavr.databinding.FragmentChoreBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChoreBottomSheetDialog extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    // todo: perform input validation (date must be in the future, chore name must be non-empty, reward must be a valid dollar amount (2 decimal places) with a max of like $1000 or something

    public static final String TAG = "ChoreBottomSheetDialog";
    public static final String CHILD_ID = "childId";
    private FragmentChoreBottomSheetBinding binding;

    // todo: Use viewmodel for state
    private int year;
    private int month;
    private int dayOfMonth;

    private int hour;
    private int minute;
    private String childId;

    private Chore chore;

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChoreBottomSheetBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            childId = bundle.getString(CHILD_ID);
        } else {
            Log.e(TAG, "Child ID is null");
        }

        Calendar calendar = Calendar.getInstance();
        long timeMilli2 = calendar.getTimeInMillis();


        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        this.hour = 23;
        this.minute = 59;
        calendar.set(year,month,dayOfMonth,hour,minute);

        // TODO: do either add chore or edit chore depending on the screen
        binding.choreTitleTextView.setText(R.string.add_chore_title);
        binding.pickDateButton.setText(getDateString(calendar));
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String strDate = dateFormat.format(calendar.getTime());
        binding.pickTimeButton.setText(strDate);

        setClickListeners();



        return binding.getRoot();
    }

    private Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;

    }

    private void setClickListeners() {
        if (this.chore == null) {



                // Create new chore
                binding.saveChoreButton.setOnClickListener(v -> {
                    String ChoreName = binding.choreNameFieldEditText.getText().toString();
                    if(TextUtils.isEmpty(ChoreName))
                    {
                        Toast.makeText(getActivity(),"Chore Cannot be Empty",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        String rewardText = binding.rewardFieldEditText.getText().toString();
                        Chore chore = new Chore(
                                childId,
                                getCalendar().getTimeInMillis(),
                                binding.choreNameFieldEditText.getText().toString(),
                                Utils.dollarStringToCents(rewardText));
                        ParentChildChoresActivity.toDoCompletedDBReference.collectionReference.add(chore);
                        dismiss();

                    }


                });

        } else {
            String editedChoreName = chore.getTaskName().toString();
            Log.d("TEST EMPTY CHORE",editedChoreName);
            if(TextUtils.isEmpty(editedChoreName))
            {
                Toast.makeText(getActivity(), "Chore cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                // Edit existing chore
                binding.choreNameFieldEditText.setText(chore.getTaskName());
                binding.rewardFieldEditText.setText(Utils.centsToDollarString(chore.getRewardCents(), false), TextView.BufferType.EDITABLE);
                binding.choreTitleTextView.setText(R.string.edit_chore);
                binding.saveChoreButton.setOnClickListener(v -> {
                    String inputChore = binding.choreNameFieldEditText.getText().toString();
                    if(TextUtils.isEmpty(inputChore))
                    {
                        Toast.makeText(getActivity(), "Chore cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else {
                        //chore.setTaskName(binding.choreNameFieldEditText.getText().toString());
                        chore.setTaskName(inputChore);
                        chore.setRewardCents(Utils.dollarStringToCents(binding.rewardFieldEditText.getText().toString()));
                        chore.setDeadline(getCalendar().getTimeInMillis());

                        ParentChildChoresActivity.toDoCompletedDBReference.collectionReference.document(chore.getId()).set(chore);
                        dismiss();
                    }
                });
            }
        }

        binding.pickDateButton.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                    this, year, month, dayOfMonth);
            datePickerDialog.show();
        });

        binding.pickTimeButton.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireActivity(),this,hour,minute,true);
            timePickerDialog.show();
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;

        binding.pickDateButton.setText(getDateString(calendar));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, this.year);
        calendar.set(Calendar.MONTH, this.month);
        calendar.set(Calendar.DAY_OF_MONTH,  this.dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        this.hour = hourOfDay;
        this.minute = minute;

        binding.pickTimeButton.setText(getTImeString(calendar));

    }



    private static CharSequence getDateString(Calendar calendar) {
        return DateFormat.format("MM/dd/yyyy", calendar.getTime());
    }

    private static CharSequence getTImeString(Calendar calendar) {
        return DateFormat.format("HH:mm", calendar.getTime());
    }


}
