package ykn.sovava.myserver;

/**
 * Description: TODO
 *
 * @author: ykn
 * @date: 2022年05月23日 22:48
 **/
public class msgHandle {
//    private static String msg;
    private String header;
    private String context;

    public msgHandle(String msg) {
        String[] msgs = msg.split("\\|");
//        msgHandle.msg = msg;
        header = msgs[0];
        context = msgs[1];
    }

    public String getHeader() {
        System.out.println(header);
        return header;
    }

    public String getContext() {
        System.out.println(context);
        return context;
    }
}
