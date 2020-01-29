package com.gdou.im.protocol;

import com.alibaba.fastjson.JSONObject;
import com.gdou.im.protocol.data.Data;
import com.gdou.im.protocol.data.request.LoginRequest;
import com.gdou.im.protocol.data.request.MessageRequest;
import com.gdou.im.protocol.data.response.LoginResponse;

import java.util.HashMap;
import java.util.Map;

import static com.gdou.im.protocol.command.Command.*;

/**
 * @ProjectName: demo
 * @Package: com.gdou.im.protocol
 * @ClassName: PacketCodeC
 * @Author: carrymaniac
 * @Description:
 * @Date: 2020/1/28 12:38 下午
 * @Version:
 */
public class PacketCodeC {
    private static final int MAGIC_NUMBER = 0x12345678;
    public static final PacketCodeC INSTANCE = new PacketCodeC();

    private final Map<Integer, Class<? extends Data>> packetTypeMap;

    private PacketCodeC(){
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequest.class);
        packetTypeMap.put(LOGIN_REQUEST, LoginRequest.class);
    }


    public Data decode(Packet packet){
        int command = packet.getCommand();
        //获取到类，使用JSON开始解析
        Class<? extends Data> dataType = packetTypeMap.get(command);
        //解析并返回
        Data data = JSONObject.parseObject(packet.getData(), dataType);
        return data;
    }

}
