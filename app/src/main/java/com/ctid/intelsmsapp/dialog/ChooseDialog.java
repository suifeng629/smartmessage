package com.ctid.intelsmsapp.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ctid.intelsmsapp.R;


public class ChooseDialog extends DialogFragment {

    private static final String KEY_MSG = "msg";
    private static final String KEY_POSITIVE_BTN_TXT = "positiveBtnTxt";
    private static final String KEY_NEGATIVE_BTN_TXT = "negativeBtnTxt";

    private OnButtonClickListener onButtonClickListener;

    public static ChooseDialog newInstance(String msg, String positiveBtnTxt, String negativeBtnTxt, OnButtonClickListener onButtonClickListener) {
        ChooseDialog chooseDialog = new ChooseDialog();
        chooseDialog.setOnButtonClickListener(onButtonClickListener);
        Bundle arguments = new Bundle();
        arguments.putString(KEY_MSG, msg);
        arguments.putString(KEY_POSITIVE_BTN_TXT, positiveBtnTxt);
        arguments.putString(KEY_NEGATIVE_BTN_TXT, negativeBtnTxt);
        chooseDialog.setArguments(arguments);
        return chooseDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.choose_dialog_style);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String positiveBtnTxt = getArguments().getString(KEY_POSITIVE_BTN_TXT);
        String negativeBtnTxt = getArguments().getString(KEY_NEGATIVE_BTN_TXT);
        String msg = getArguments().getString(KEY_MSG);

        View view = inflater.inflate(R.layout.dialog_choose, container, false);
        TextView msgView = (TextView) view.findViewById(R.id.dialog_choose_msg);
        Button positiveBtn = (Button) view.findViewById(R.id.dialog_choose_btn_positive);
        Button negativeBtn = (Button) view.findViewById(R.id.dialog_choose_btn_negative);

        msgView.setText(msg);
        positiveBtn.setText(positiveBtnTxt);
        negativeBtn.setText(negativeBtnTxt);

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClickListener.onPositiveButtonClick(v);
            }
        });

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClickListener.onNegativeButtonClick(v);
            }
        });
        return view;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }


    public interface OnButtonClickListener {

        void onPositiveButtonClick(View v);

        void onNegativeButtonClick(View v);
    }

}
