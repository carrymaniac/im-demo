package com.gdou.im.server;

import com.gdou.im.server.handler.WebSocketHandler;
import com.gdou.im.server.handler.LoginRequestHandler;
import com.gdou.im.server.handler.MessageRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @ProjectName: demo
 * @Package: com.gdou.im.server
 * @ClassName: NettyServer
 * @Author: carrymaniac
 * @Description: netty的服务器端
 * @Date: 2020/1/4 1:08 下午
 * @Version:
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        //定义线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>(){
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //Http编解码器，HttpServerCodec()
                        ch.pipeline().addLast(new HttpServerCodec());
                        //大数据流处理
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                        //HttpObjectAggregator：聚合器，聚合了FullHTTPRequest、FullHTTPResponse。。。，当你不想去管一些HttpMessage的时候，直接把这个handler丢到管道中，让Netty自行处理即可
                        ch.pipeline().addLast(new HttpObjectAggregator(2048*64));
                        //WebSocketServerProtocolHandler：给客户端指定访问的路由（/ws），是服务器端处理的协议，当前的处理器处理一些繁重的复杂的东西，运行在一个WebSocket服务端
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
                        ch.pipeline().addLast(new WebSocketHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        ch.pipeline().addLast(new LoginRequestHandler());

                    }
                });
        ChannelFuture future = serverBootstrap.bind(8080).sync();
    }
}
