package com.riddlew.studentapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AssessmentDialogFragment extends DialogFragment {
    public interface OnAssessmentSelectedListener {
        void onAssessmentSelected(int courseId);
    }
    private OnAssessmentSelectedListener mListener;

    public AssessmentDialogFragment() {
        Log.d("TEST", "GET ALL ASSESSMENTS");
    }

    public AssessmentDialogFragment(int courseId) {
        Log.d("TEST", "GET ALL ASSESSMENTS FOR COURSE " + courseId);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Assessment");

        String[] test = { "Test 1", "Test 2", "Test 3", "Test 4" };
        builder.setItems(test, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onAssessmentSelected(i);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnAssessmentSelectedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
