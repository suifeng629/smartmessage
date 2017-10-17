package com.ctid.intelsmsapp.activity;

import android.app.Activity;
import android.app.FragmentTransaction;

import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.dialog.ChooseDialog;
import com.ctid.intelsmsapp.dialog.LoadingDialog;
import com.ctid.intelsmsapp.dialog.MessageDialog;
import com.ctid.intelsmsapp.enums.ConnectionMsgEnum;

/**
 * ClassName: 类名称<br>
 * Description: 类描述<br>
 *
 * @author sunzhiwei<br>
 * @date 2017/10/10
 * @email sunzhiwei@ctid.com.cn
 */
public class DialogEnabledActivity extends Activity {
    protected final String TAG_LOADING_DIALOG = "loadingDialog";
    protected final String TAG_ALERT_DIALOG = "alertDialog";
    protected final String TAG_CHOOSE_DIALOG = "chooseDialog";

    /**
     * 显示一个loading dialog
     */
    public void showLoadingDialog(String msg) {
        LoadingDialog loadingDialog = LoadingDialog.newInstance(msg);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(loadingDialog, TAG_LOADING_DIALOG);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 关闭一个loading dialog
     */
    public void dismissLoadingDialog() {
        LoadingDialog loadingDialog = (LoadingDialog) getFragmentManager().findFragmentByTag(TAG_LOADING_DIALOG);
        if (loadingDialog != null) {
            loadingDialog.dismissAllowingStateLoss();
        }
    }

    /**
     * 显示一个alert dialog
     *
     * @param connectionMsgEnum            dialog的消息类型
     * @param onConfirmButtonClickListener dialog的dismiss、cancel状态的响应方法
     */
    public void showAlertDialog(ConnectionMsgEnum connectionMsgEnum, MessageDialog.OnConfirmButtonClickListener onConfirmButtonClickListener) {
        int msgIndex = 0;
        switch (connectionMsgEnum) {
            case FAILED:
                msgIndex = R.string.msg_error_201;
                break;
            case SERVER_ERROR:
                msgIndex = R.string.msg_error_202;
                break;
            case TIMEOUT:
                msgIndex = R.string.msg_error_301;
                break;
            default:
                break;

        }
        MessageDialog messageDialog = MessageDialog.newInstance(getString(msgIndex), getString(R.string.confirm), onConfirmButtonClickListener);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(messageDialog, TAG_ALERT_DIALOG);
        transaction.commitAllowingStateLoss();
    }


    /**
     * 显示一个alert dialog
     *
     * @param msg                          dialog上显示的消息
     * @param btnTxt                       dialog上按钮显示的文字
     * @param onConfirmButtonClickListener dialog的dismiss、cancel状态的响应方法
     */
    public void showAlertDialog(String msg, String btnTxt, MessageDialog.OnConfirmButtonClickListener onConfirmButtonClickListener) {
        MessageDialog messageDialog = MessageDialog.newInstance(msg, btnTxt, onConfirmButtonClickListener);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(messageDialog, TAG_ALERT_DIALOG);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 显示一个带有两个选项的dialog
     *
     * @param msg                   dialog上显示的信息
     * @param positiveBtnTxt        左侧按钮文字
     * @param negativeBtnTxt        右侧按钮文字
     * @param onButtonClickListener 按钮响应方法
     */
    public void showChooseDialog(String msg, String positiveBtnTxt, String negativeBtnTxt, ChooseDialog.OnButtonClickListener onButtonClickListener) {
        ChooseDialog chooseDialog = ChooseDialog.newInstance(msg, positiveBtnTxt, negativeBtnTxt, onButtonClickListener);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(chooseDialog, TAG_CHOOSE_DIALOG);
        transaction.commitAllowingStateLoss();
    }

    /**
     * Dismiss choose dialog
     */
    public void dismissChooseDialog() {
        ChooseDialog chooseDialog = (ChooseDialog) getFragmentManager().findFragmentByTag(TAG_CHOOSE_DIALOG);
        if (chooseDialog != null) {
            chooseDialog.dismissAllowingStateLoss();
        }
    }
}
