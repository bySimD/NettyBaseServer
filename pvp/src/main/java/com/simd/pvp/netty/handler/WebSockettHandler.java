package com.simd.pvp.netty.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simd.pvp.repository.ChannelFragmentRepository;
import com.simd.pvp.protocol.base.ProtocolService;
import com.simd.pvp.service.session.UserSessionData;
import com.simd.pvp.repository.UserSessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;

@Service
@ChannelHandler.Sharable
public class WebSockettHandler extends AbstractWebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ChannelFragmentRepository channelFragmentRepository;
    /**
     * 사용자 세션 매니저 객체.
     */
    @Autowired
    protected UserSessionManager wsUserSessionManager;

    @Autowired
    private ProtocolService protocolService;

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

//		om.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        wsUserSessionManager.addSession(ctx.channel());
    }

    @Override
    protected void doChannelRead0(ChannelHandlerContext ctx, byte[] bytes) {

        UserSessionData sessionData = wsUserSessionManager.getUserSessionData(ctx.channel());
        protocolService.processMessage(sessionData, new TextWebSocketFrame(new String(bytes, Charset.defaultCharset())));
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        UserSessionData userSessionData = wsUserSessionManager.getUserSessionData(ctx.channel());
        logger.info("[Close session] UID: {}, Nick :{}", userSessionData.getUserID(), userSessionData.getNickName());
        protocolService.connectionClose(userSessionData);
        channelFragmentRepository.removeFragmentCache(ctx);
    }
}