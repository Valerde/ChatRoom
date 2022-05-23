package ykn.sovava.myclient.util;

/**
 * Description: TODO
 *
 * @author: ykn
 * @date: 2022年05月23日 22:48
 **/
public class msgHandle {
    private static String msg;

    public msgHandle(String msg) {
        this.msg = msg;
    }

    public static String getHeader() {
        return msg.split("|")[0];
    }

    public static String context() {
        return msg.split("|")[1];
    }
}
