package com.official.barberinc;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class VisitDialogFragment extends DialogFragment {

    public static final String TAG = "VisitDialogFragment";
    private final static String ID = "id";
    private final static String NAME = "name";

    public int id;
    public String name;


    public static VisitDialogFragment newInstance(int id, String name) {
        VisitDialogFragment deleteDialogFragment = new VisitDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        args.putString(NAME, name);
        deleteDialogFragment.setArguments(args);
        return deleteDialogFragment;
    }

    public interface DeleteDialogListener {
        void onDialogPositiveClick(DialogFragment dialogFragment);
        void onDialogNeutralClick(DialogFragment dialogFragment);
        void onDialogNegativeClick(DialogFragment dialogFragment);
    }

    DeleteDialogListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DeleteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement DeleteDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        name = getArguments().getString(NAME);
        id = getArguments().getInt(ID);

        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Edit Visit")
                .setMessage("What to do with " + name + "'s visit?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    listener.onDialogPositiveClick(VisitDialogFragment.this);
                })
                .setNeutralButton("Edit", (dialog, which) -> {
                    listener.onDialogNeutralClick(VisitDialogFragment.this);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    listener.onDialogNegativeClick(VisitDialogFragment.this);
                });
        return dialogBuilder.create();
    }
}
