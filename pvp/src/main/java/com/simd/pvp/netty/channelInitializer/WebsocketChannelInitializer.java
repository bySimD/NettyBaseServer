package com.simd.pvp.netty.channelInitializer;

import com.simd.pvp.netty.handler.WebSocketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebsocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    WebSocketHandler webSocketHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        //WebSocket의 모든 요청과 응답을 처리하는 처리기입니다.
        //설정은 handler의 맵에 들어가서 설정합니다.
        //SimpleChannelInboundHanler 은 Inbound data를 사용합니다.
        ChannelPipeline pipeline = socketChannel.pipeline();
//        pipeline.addLast("PacketDecoder", new PacketDecoder());
//        //HttpServerCodec는 HTTP에 대한 요청을 처리하여 decoding하고 다시 encoding하는 netty의 기본처리입니다.
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());
//        //httpObjectAggregator는 내장처리기로 HTTP 요청을 처리하고 FULL요청이 유저에게 도착한 경우에만 다음처리기로 전달합니다.
        pipeline.addLast("HttpObjectAggregator",new HttpObjectAggregator(128 * 1024));
        //WebSocketServerProtocolHandler는 들어오는 WebSocket요청을 다음 처리기로 전달하는 경우
        // WebSocket http handler 및 approval을 처리하도록 확장되는 처리기 입니다.
        pipeline.addLast("WebSocketServerProtocolHandler",new WebSocketServerProtocolHandler("/TsumBlast/websocket/blast.do"));
        pipeline.addLast("webSockettHandler", webSocketHandler);

    }
}
