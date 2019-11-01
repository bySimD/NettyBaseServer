package com.simd.pvp.netty;

import com.simd.pvp.netty.handler.WebSockettHandler;
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
    private WebSockettHandler webSockettHandler;

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
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) {
                            addPipeline(sc);
                        }
                    });
        } else {
            logger.info("Epoll Not Supported, NIO selected");
            acceptGroups = new NioEventLoopGroup(acceptorThreads);
            workGroups = new NioEventLoopGroup(workerThreads);

            sb = new ServerBootstrap();
            sb.group(acceptGroups, workGroups)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, backlog)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) {
                            addPipeline(sc);
                        }
                    });

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

    private void addPipeline(SocketChannel sc) {
        //WebSocket의 모든 요청과 응답을 처리하는 처리기입니다.
        //설정은 handler의 맵에 들어가서 설정합니다.
        //SimpleChannelInboundHanler 은 Inbound data를 사용합니다.
        ChannelPipeline pipeline = sc.pipeline();
//        pipeline.addLast("PacketDecoder", new PacketDecoder());
//        //HttpServerCodec는 HTTP에 대한 요청을 처리하여 decoding하고 다시 encoding하는 netty의 기본처리입니다.
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());
//        //httpObjectAggregator는 내장처리기로 HTTP 요청을 처리하고 FULL요청이 유저에게 도착한 경우에만 다음처리기로 전달합니다.
        pipeline.addLast("HttpObjectAggregator",new HttpObjectAggregator(128 * 1024));
        //WebSocketServerProtocolHandler는 들어오는 WebSocket요청을 다음 처리기로 전달하는 경우
        // WebSocket http handler 및 approval을 처리하도록 확장되는 처리기 입니다.
        pipeline.addLast("WebSocketServerProtocolHandler",new WebSocketServerProtocolHandler("/TsumBlast/websocket/blast.do"));
        pipeline.addLast("webSockettHandler", webSockettHandler);


    }

}
