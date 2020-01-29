package com.gdou.im.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.gdou.im.protocol.Packet;
import com.gdou.im.protocol.command.Command;
import com.gdou.im.protocol.data.Data;
import com.gdou.im.protocol.data.request.MessageRequest;
import com.gdou.im.protocol.data.response.MessageResponse;
import com.gdou.im.session.Session;
import com.gdou.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @ProjectName: demo
 * @Package: com.gdou.im.server.handler
 * @ClassName: DataHandler
 * @Author: carrymaniac
 * @Description:
 * @Date: 2020/1/28 1:15 下午
 * @Version:
 */
@Slf4j
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequest msg) throws Exception {
        log.info("拿到了数据，到达此处了:{}",msg);
        Session session = SessionUtil.getSession(ctx.channel());
        //开始写回去
        Packet packetForConfirm = new Packet();
        packetForConfirm.setCommand(Command.MESSAGE_RESPONSE);
        MessageResponse responseForConfirm = new MessageResponse();
        responseForConfirm.setMessage(msg.getMessage());
        responseForConfirm.setFromUserName("你");
        packetForConfirm.setData(JSONObject.toJSONString(responseForConfirm));
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(packetForConfirm)));

        //构建response
        Channel channel = SessionUtil.getChannel(msg.getToId());
        if(channel!=null&& SessionUtil.hasLogin(channel)){
            MessageResponse response = new MessageResponse();
            response.setFromUserId(session.getUserId());
            response.setFromUserName(session.getUserName());
            response.setMessage(msg.getMessage());
            Packet packetForToId = new Packet();
            packetForToId.setData(JSONObject.toJSONString(response));
            packetForToId.setCommand(Command.MESSAGE_RESPONSE);
            channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(packetForToId)));
        }else {
            log.info("用户并不在线");
        }
    }
}
