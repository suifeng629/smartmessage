package com.ctid.intelsmsapp.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.ctid.intelsmsapp.R;


public class MessageDialog extends DialogFragment {

    private OnConfirmButtonClickListener onConfirmButtonClickListener;

    public static MessageDialog newInstance(String msg, String btnTxt, OnConfirmButtonClickListener onConfirmButtonClickListener) {

        MessageDialog dialog = new MessageDialog();
        dialog.setOnConfirmButtonClickListener(onConfirmButtonClickListener);
        Bundle args = new Bundle();
        args.putString("msg", msg);
        args.putString("btnTxt", btnTxt);
        dialog.setArguments(args);
        return dialog;
    }

    private void setOnConfirmButtonClickListener(OnConfirmButtonClickListener onConfirmButtonClickListener) {
        this.onConfirmButtonClickListener = onConfirmButtonClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.loading_dialog_style);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String msg = getArguments().getString("msg");
        String btnTxt = getArguments().getString("btnTxt");
        View view = inflater.inflate(R.layout.dialog_alert, container, false);
        TextView titleView = (TextView) view.findViewById(R.id.dialog_alert_txt_msg);
        titleView.setText(msg);

        Button confirmBtn = (Button) view.findViewById(R.id.dialog_alert_btn_confirm);
        if (btnTxt != null && !btnTxt.equals("")) {
            confirmBtn.setText(btnTxt);
        }
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onConfirmButtonClickListener != null) {
            onConfirmButtonClickListener.onConfirmButtonClick();
        }
    }

    public interface OnConfirmButtonClickListener {
        void onConfirmButtonClick();
    }

}
