package com.ctid.intelsmsapp.enums;


public enum ConnectionMsgEnum {

    SUCCESS(100), FAILED(101), KEY_STORE_ERROR(102), SERVER_ERROR(103), TIMEOUT(104), INIT_CHANNEL(106), UNREGISTER(108), NEED_SYNC(109),
    REGISTER_FAIL(110), REGISTER_FAIL_RETRY(111), GET_CARDGPAC(112), GOTO_MAINACTIVITY(113);

    private int msgType;

    private ConnectionMsgEnum(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgType() {
        return msgType;
    }

    public static ConnectionMsgEnum valueOf(int value) {
        switch (value) {
            case 100:
                return SUCCESS;
            case 101:
                return FAILED;
            case 102:
                return KEY_STORE_ERROR;
            case 103:
                return SERVER_ERROR;
            case 104:
                return TIMEOUT;
            case 106:
                return INIT_CHANNEL;
            case 108:
                return UNREGISTER;
            case 109:
                return NEED_SYNC;
            case 110:
                return REGISTER_FAIL;
            case 111:
                return REGISTER_FAIL_RETRY;
            case 112:
                return GET_CARDGPAC;
            case 113:
                return GOTO_MAINACTIVITY;
            default:
                return FAILED;
        }
    }
}
