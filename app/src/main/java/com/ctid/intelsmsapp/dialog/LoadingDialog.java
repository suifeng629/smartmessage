package com.ctid.intelsmsapp.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ctid.intelsmsapp.R;

/**
 * ClassName: 类名称<br>
 * Description: 类描述<br>
 *
 * @author sunzhiwei<br>
 * @date 2017/10/10
 * @email sunzhiwei@ctid.com.cn
 */
public class LoadingDialog extends DialogFragment {
    public static LoadingDialog newInstance(String msg) {
        LoadingDialog loadingDialog = new LoadingDialog();
        Bundle arguments = new Bundle();
        arguments.putString("msg", msg);
        loadingDialog.setArguments(arguments);
        return loadingDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.loading_dialog_style);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, container, false);
        TextView titleView = (TextView) view.findViewById(R.id.loading_dialog_title);
        String msg = getArguments().getString("msg");
        titleView.setText(msg);
        return view;
    }
}
