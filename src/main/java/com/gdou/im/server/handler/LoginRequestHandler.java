package com.gdou.im.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.gdou.im.protocol.Packet;
import com.gdou.im.protocol.data.request.LoginRequest;
import com.gdou.im.protocol.data.response.LoginResponse;
import com.gdou.im.session.Session;
import com.gdou.im.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.gdou.im.protocol.command.Command.LOGIN_RESPONSE;

/**
 * @ProjectName: demo
 * @Package: com.gdou.im.server.handler
 * @ClassName: LoginRequestHandler
 * @Author: carrymaniac
 * @Description:
 * @Date: 2020/1/28 1:34 下午
 * @Version:
 */
@Slf4j
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequest msg) throws Exception {
        LoginResponse response = new LoginResponse();
        Packet packet = new Packet();
        if(valid(msg)){
            //随机生成ID
            String userId = randomUserId();
            response.setSuccess(true);
            response.setUserId(userId);
            response.setUserName(msg.getUserName());
            //绑定session和channel
            SessionUtil.bindSession(new Session(userId,msg.getUserName()),ctx.channel());
            log.info("用户:{}登陆成功",msg.getUserName());
            SessionUtil.broadcast("用户: "+msg.getUserName()+"已上线,他的ID为: "+userId);
        }else {
            response.setReason("账号密码校验失败");
            response.setSuccess(false);
            log.info("用户:{}登陆失败",msg.getUserName());
        }
        packet.setData(JSONObject.toJSONString(response));
        packet.setCommand(LOGIN_RESPONSE);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(packet)));

    }


    /**
     * 进行登陆校验，todo 之后可以在这个方法中加入数据库进行校验
     * @param loginRequest
     * @return
     */
    private boolean valid(LoginRequest loginRequest) {
        return true;
    }

    private static String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    /**
     * channel没有链接到远程节点的时候
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Session session = SessionUtil.getSession(ctx.channel());
        log.info("用户{}下线了，移除其session",session.getUserName());
        SessionUtil.unBindSession(ctx.channel());
        SessionUtil.broadcast("用户: "+session.getUserName()+"已下线");
    }

    /**
     * Channel已经被创建但还未被注册到EventLoop
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }




}
