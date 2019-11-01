package com.simd.pvp.protocol.base;

import com.simd.pvp.service.session.UserSessionData;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProtocolService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, AbstractProtocolController> protocolMap;

    public ProtocolService() {
        this.protocolMap = new HashMap<>();
    }

    public void addProtocol(String _className, AbstractProtocolController _class){
        protocolMap.put(_className, _class);
    }

    public AbstractProtocolController getProtocol(String _className) {
        return protocolMap.get(_className);
    }

    public void connectionClose( UserSessionData userSessionData) {

    }

    public void processMessage(UserSessionData sessionData, WebSocketFrame message)
    {

    }

    protected AbstractProtocolController getProtocolController(boolean isClientToServer, long protocolID, String protocolName)
    {
        AbstractProtocolController protocolController = null;

        try
        {
            protocolController = protocolMap.get(protocolName);
        }
        catch (Exception e)
        {
            logger.error(e.toString());

            protocolController = null;
        }

        return protocolController;
    }

}
