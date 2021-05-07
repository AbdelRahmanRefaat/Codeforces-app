package com.example.codeforces.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.codeforces.R;

public class AddUserDialog extends DialogFragment {

    private static final String TAG = "AddUserDialog";

    public interface OnInputSelected{
        void sendInput(String handle);
    }
    public OnInputSelected mOnInputSelected;


    // widgets
    private EditText mHandleInput;
    private Button mActionAddUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_user,container,false);
        Log.d(TAG, "onCreateView: creating a fucken thing");

        mHandleInput = view.findViewById(R.id.handleET);

        mActionAddUser = view.findViewById(R.id.addUserBtn);


        mHandleInput.setFocusable(true);
        mActionAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String handle = mHandleInput.getText().toString();
                    if(!handle.equals("")){
                       // Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("Users");
                       // Toast.makeText(fragment.getContext(), "" + handle, Toast.LENGTH_SHORT).show();
                        mOnInputSelected.sendInput(handle);
                    }
                    getDialog().dismiss();
            }

        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: attaching listener from add dialog to main activity");
        try {
            mOnInputSelected =  (OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: " + e.getMessage());
        }
    }
}
