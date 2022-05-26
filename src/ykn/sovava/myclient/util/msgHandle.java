package ykn.sovava.myclient.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description:
 *
 * @author: ykn
 * @date: 2022年05月23日 22:48
 **/
public class msgHandle {
    private static  String msg;

    public msgHandle(String msg) {
        msgHandle.msg = msg;
    }

    public String getHeader() {
        return msg.split("\\|")[0];
    }

    public String context() {
        return msg.split("\\|")[1];
    }

    public List<String> grouper(){
        String[] msgs = msg.split("\\|")[1].split(":");
        return Arrays.asList(msgs);
    }
}
