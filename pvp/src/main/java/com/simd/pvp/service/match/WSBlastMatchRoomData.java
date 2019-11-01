package com.simd.pvp.service.match;

import com.simd.pvp.protocol.base.AbstractProtocolController;
import com.simd.pvp.service.session.UserSessionData;
import com.simd.pvp.util.etc.ConcurrentList;
import com.simd.pvp.util.etc.EtcUtil;
import com.simd.pvp.util.etc.JsonUtil;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatchers;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Data
public class WSBlastMatchRoomData
{
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	// 방 아이디
	private Long roomID;

	// 사용자 최대 수
	private int playerMaxCount;

	// 브로드 캐스트용
	private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	// 사용자 세션 리스트
	private ConcurrentList<UserSessionData> sessionList;

	public WSBlastMatchRoomData()
	{
		roomID = EtcUtil.generateUniqueID();
		sessionList = new ConcurrentList<UserSessionData>(new ArrayList<UserSessionData>());

	}

	public synchronized void enterRoom(UserSessionData sessionData) {
		channelGroup.add(sessionData.getChannel());
		sessionList.add(sessionData);
	}

	public synchronized void leaveRoom(UserSessionData sessionData)
	{
		channelGroup.remove(sessionData.getChannel());
		sessionList.remove(sessionData);
	}

	public void outBroadCastGroup(UserSessionData sessionData)
	{
		channelGroup.remove(sessionData.getChannel());
	}


	public void disposeRoom()
	{
		sessionList.clear();
		channelGroup.clear();
	}

	public void broadCastRoom(AbstractProtocolController protocol, Map<Object, Object> resMap, UserSessionData exceptSessionData)
	{
		try
		{
			String resData = JsonUtil.getJsonString(resMap);

			if (exceptSessionData !=null)
				channelGroup.writeAndFlush(new TextWebSocketFrame(resData), ChannelMatchers.isNot(exceptSessionData.getChannel()));
			else
				channelGroup.writeAndFlush(new TextWebSocketFrame(resData));

		}
		catch (Exception e)
		{
			logger.error("", e);
		}
	}

	@Override
	public String toString() {
		return "WSBlastMatchRoomData: {" +
				"roomID=" + roomID +
				", playerMaxCount=" + playerMaxCount +
				", sessionList=" + sessionList +
				'}';
	}

}
