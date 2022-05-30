package ykn.sovava.myclient.util;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * 客户端向别的客户端发消息，经服务器转发后发给客户端，发给服务器端的消息格式应该是：<p>
 * HEADER|f1,f2,f3|context<p>
 * 服务器发给对应客户端的消息应该是<p>
 * HEADER|from_nickName|context
 * 那么,就需要建立两个"|"<p>
 * 登陆的时候,格式为:<p>
 * HEADER|nickName| (此处有空格)<p>
 * 有人登录提醒的时候,格式为<p>
 * HEADER|nickName| (此处有空格)<p>
 * 有人离开时,格式为<p>
 * HEADER| | 完<p>
 * 服务器通知有人离开时,格式为:<p>
 * HEADER|nickName| 完<p>
 * 服务器发送给客户端踢出的消息为<p>
 * HEADER| | 完    ,之后,该客户端发送离开的消息,服务器转发<p>
 * 服务器向客户端发送建群的消息为<p>
 * HEADER|f1,f2,f3|groupName<p>
 *
 * @author: ykn
 * @date: 2022年05月23日 22:48
 **/
public class msgHandle {
    private String header;
    private String target;
    private String context;

    public msgHandle(String msg) {
        String[] msgs = msg.split("\\|");
        header = msgs[0];
        target = msgs[1];
        context = msgs[2];
    }

    public String getHeader() {
        return header;
    }

    public String getContext() {
        return context;
    }

    public List<String> getGrouperName() {
        String[] msgss = target.split(",");
        return Arrays.asList(msgss);
    }

    public String getNickName() {
        return target;
    }

    public String getGroupName() {
        return context;
    }
}
