package com.simd.pvp.service.session;

import io.netty.channel.Channel;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserSessionData
{
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	// 아이디
	private Long userID;
	
	// 닉네임
	private String nickName;

	// 채널
	private Channel channel;
	
	// 하트비트 카운트
	private int heartBeat;

	private Map<Object, Object> requestMap;


	private Map<Thread, Map<Object, Object>> responseMap;

	
	// 기본 생성자
	public UserSessionData()
	{
		Reset();
	}


	public boolean isOnline()
	{
		return this.channel != null && this.channel.isOpen();
	}
	
	// 하트비트 수를 증가한다.
	public int addAndGetHeartBeat()
	{
		return heartBeat++;
	}

	// 하트비트 수를 초기화 한다.
	public void resetHeartBeat()
	{
		this.heartBeat = 0;
	}

	public void closeSocket()
	{
		if (isOnline())
		{
			try
			{
				channel.close();
			}
			catch (Exception e)
			{
				logger.error("", e);
			}
		}
	}
	
	/**
	 * 프로토콜 요청 데이터 맵 객체를 리턴한다.
	 * 
	 * @return 요청 데이터 맵 객체
	 */
	public Map<Object, Object> getRequestMap()
	{
		return requestMap;
	}
	
	/**
	 * 프로토콜 요청 데이터 맵 객체를 설정한다.
	 * 
	 * @param requestMap 요청 데이터 맵 객체
	 */
	public void setRequestMap(Map<Object, Object> requestMap)
	{
		this.requestMap = requestMap;
	}
	
	/**
	 * 프로토콜 응답 데이터 맵 객체를 리턴한다.
	 * 
	 * @return 응답 데이터 맵 객체.
	 */
	public Map<Object, Object> getResponseMap()
	{
		Thread currentThread = Thread.currentThread();
		
		return responseMap.get(currentThread);
	}
	
	/**
	 * 프로토콜 응답 데이터 맵 객체를 설정한다.
	 * 
	 * @param responseMap 응답 데이터 맵 객체.
	 */
	public void setResponseMap(Map<Object, Object> responseMap)
	{
		Thread currentThread = Thread.currentThread();
		
		if (responseMap == null)
		{
			this.responseMap.remove(currentThread);
		}
		else
		{
			this.responseMap.put(currentThread, responseMap);
		}
	}


	public void Reset()
	{
		userID = null;
		nickName = null;
		channel = null;
		requestMap = null;
		responseMap = new HashMap<Thread, Map<Object, Object>>();

	}

	@Override
	public String toString() {
		return "WSBlastSessionData{" +
				"userID=" + userID +
				", nickName='" + nickName + '\'' +
				", channel=" + channel +
				", heartBeat=" + heartBeat +
				", requestMap=" + requestMap +
				", responseMap=" + responseMap +
				'}';
	}
}
