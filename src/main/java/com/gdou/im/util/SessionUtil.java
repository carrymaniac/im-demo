package com.gdou.im.util;

import com.alibaba.fastjson.JSONObject;
import com.gdou.im.attribute.Attributes;
import com.gdou.im.protocol.Packet;
import com.gdou.im.protocol.data.response.MessageResponse;
import com.gdou.im.session.Session;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.gdou.im.protocol.command.Command.SYSTEM_MESSAGE_RESPONSE;

public class SessionUtil {
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static boolean hasLogin(Channel channel) {

        return channel.hasAttr(Attributes.SESSION);
    }

    public static Session getSession(Channel channel) {

        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {

        return userIdChannelMap.get(userId);
    }

    public static void broadcast(String message){
        Packet packet = new Packet();
        packet.setCommand(SYSTEM_MESSAGE_RESPONSE);
        MessageResponse response = new MessageResponse();
        response.setMessage(message);
        response.setFromUserName("系统提醒");
        packet.setData(JSONObject.toJSONString(response));
        Set<Map.Entry<String, Channel>> entries = userIdChannelMap.entrySet();
        for(Map.Entry<String, Channel> entry :entries){
            Channel channel = entry.getValue();
            channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(packet)));
        }
    }
}
