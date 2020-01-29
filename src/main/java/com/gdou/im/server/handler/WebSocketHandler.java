package com.gdou.im.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.gdou.im.protocol.PacketCodeC;
import com.gdou.im.protocol.data.Data;
import com.gdou.im.protocol.data.request.LoginRequest;
import com.gdou.im.protocol.data.request.MessageRequest;
import com.gdou.im.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @ProjectName: demo
 * @Package: com.gdou.im.server.handler
 * @ClassName: ChatHandler
 * @Author: carrymaniac
 * @Description: ChatHandler
 * @Date: 2020/1/28 12:01 上午
 * @Version:
 */
@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{


    //用于记录和管理所有客户端的channel，可以把相应的channel保存到一整个组中
    //DefaultChannelGroup：用于对应ChannelGroup，进行初始化
    private static ChannelGroup channelClient =  new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        log.info("接收到了websocket包,内容为:{}",text);
        Packet packet = JSONObject.parseObject(text, Packet.class);
        if(packet!=null){
            Data decode = PacketCodeC.INSTANCE.decode(packet);
            //分发到下一个Handler
            if(decode instanceof MessageRequest){
                log.info("向下转型完成,内容为:{}",decode);
                ctx.fireChannelRead((MessageRequest)decode);
            }else if(decode instanceof LoginRequest){
                log.info("向下转型完成,内容为:{}",decode);
                ctx.fireChannelRead((LoginRequest)decode);
            }
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("建立了链接");
        channelClient.add(ctx.channel());
        super.handlerAdded(ctx);
    }
}
