package com.simd.pvp.service.session;

import com.simd.pvp.service.session.UserSessionData;
import com.simd.pvp.util.etc.EtcUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;


@Component
public class UserSessionManager
{
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private HashMap<Channel, UserSessionData> sessionMapByWebSocket;

	private HashMap<Long, UserSessionData> sessionMapByUserID;

	public UserSessionManager()
	{
		sessionMapByWebSocket = new HashMap<Channel, UserSessionData>();
		sessionMapByUserID = new HashMap<Long, UserSessionData>();
	}

	public void addSession(Channel channel)
	{

		UserSessionData userSessionData = new UserSessionData();
		userSessionData.setChannel(channel);

		sessionMapByWebSocket.put(channel, userSessionData);

	}

	public void removeSession(Channel webSocketSession)
	{
		sessionMapByWebSocket.remove(webSocketSession);
	}

	public UserSessionData getUserSessionData(Channel webSocketSession)
	{
		return sessionMapByWebSocket.get(webSocketSession);
	}

	public void addLoginSession(long userID, UserSessionData userSession)
	{
		if (sessionMapByUserID.get(userID) != null) {
			sessionMapByUserID.get(userID).closeSocket();
		}

		sessionMapByUserID.put(userID, userSession);

	}

	public long addLoginSession(String nickName, UserSessionData userSession)
	{
		long userID = EtcUtil.generateUniqueID();
		
		addLoginFBSession(userID, nickName, userSession);
		
		return userID;
	}

	public void addLoginFBSession(Long userID, String nickName, UserSessionData userSession)
	{
		userSession.setUserID(userID);
		userSession.setNickName(nickName);
		
		addLoginSession(userID, userSession);
	}

	public void removeLoginSession(long userID)
	{
		sessionMapByUserID.remove(userID);
	}

	public UserSessionData getUserSessionData(long userID)
	{
		return sessionMapByUserID.get(userID);
	}

	public Collection<UserSessionData> getLoginUserSessionList()
	{
		return sessionMapByUserID.values();
	}
	

}
