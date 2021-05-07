package com.example.codeforces.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.DialogCompat;
import androidx.fragment.app.DialogFragment;

import com.example.codeforces.R;

public class CompareUsersDialog extends DialogFragment {

    private static final String TAG = "CompareUsersDialog";
    public interface OnHandlesSelected{
        void sendHandles(String handle1, String handle2);
    }
    public OnHandlesSelected mOnHandlesSelected;


    private EditText userHandleFirst;
    private EditText userHandleSecond;
    private Button actionCompareButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_compare_users,container,false);

            userHandleFirst = view.findViewById(R.id.user_handle_first);
            userHandleSecond = view.findViewById(R.id.user_handle_second);
            actionCompareButton = view.findViewById(R.id.compare_users_button);

            actionCompareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String handle1 = userHandleFirst.getText().toString();
                    String handle2 = userHandleSecond.getText().toString();

                    if(handle1.equals("") || handle2.equals("")){
                        Toast.makeText(getContext(), "Fill the required data please.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mOnHandlesSelected.sendHandles(handle1,handle2);
                    getDialog().dismiss();
                }

            });
            return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: attaching listener from comapare dialog to main activity");
        try {
            mOnHandlesSelected = (OnHandlesSelected) getTargetFragment();
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }
}
