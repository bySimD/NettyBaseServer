package com.simd.pvp.netty;

import com.simd.pvp.netty.channelInitializer.WebsocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GameServer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ServerBootstrap sb;

    @Value("${netty.port}")
    private int port;

    @Value("${netty.threads.worker}")
    protected int workerThreads;

    @Value("${netty.threads.acceptor}")
    protected int acceptorThreads;

    @Value("${netty.backlog}")
    private int backlog;

    @Autowired
    private WebsocketChannelInitializer websocketChannelInitializer;

    public void runGameServer(){
        EventLoopGroup acceptGroups;
        EventLoopGroup workGroups;

        // check system supports epoll
        if (Epoll.isAvailable()) {
            logger.info("Epoll Supported and selected");
            acceptGroups = new EpollEventLoopGroup(acceptorThreads);
            workGroups = new EpollEventLoopGroup(workerThreads);

            sb = new ServerBootstrap();
            sb.group(acceptGroups, workGroups)
                    .channel(EpollServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, backlog)
                    .childHandler(websocketChannelInitializer);
        } else {
            logger.info("Epoll Not Supported, NIO selected");
            acceptGroups = new NioEventLoopGroup(acceptorThreads);
            workGroups = new NioEventLoopGroup(workerThreads);

            sb = new ServerBootstrap();
            sb.group(acceptGroups, workGroups)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, backlog)
                    .childHandler(websocketChannelInitializer);

        }
        try {
//            sb.bind(port);
            ChannelFuture f = sb.bind(port).sync();
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
            acceptGroups.shutdownGracefully();
            workGroups.shutdownGracefully();
        }
    }


}
