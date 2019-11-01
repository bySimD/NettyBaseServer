package com.simd.pvp.util.etc;

import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * JSON 처리와 관련된 유틸 클래스.
 */
public class JsonUtil
{	
	/**
	 * JSON 문자열을 파싱해서 <code>Map</code> 형태로 리턴한다.
	 * 
	 * @param jsonString 파싱 할 JSON 문자열.
	 * @return 파싱 결과 <code>Map</code> 객체.
	 * @throws ParseException 파싱 예외 발생시.
	 */
	@SuppressWarnings("unchecked")
	public static Map<Object, Object> getDataMap(String jsonString) throws ParseException
	{
		JSONParser jsonParser = new JSONParser();
		return (Map<Object, Object>)jsonParser.parse(jsonString);
	}
	
	/**
	 * <code>Map</code> 객체를 JSON 문자열로 변환해서 리턴한다.
	 * 
	 * @param dataMap 변환할 <code>Map</code> 객체. 
	 * @return 변환 결과 JSON 문자열.
	 */
	public static String getJsonString(Map<Object, Object> dataMap)
	{
		return JSONValue.toJSONString(dataMap);
	}
	
	/**
	 * 여러개의 <code>Map</code> 리스트를 하나의 <code>Map</code> 객체로 통합해서 리턴한다.
	 * 
	 * @param mapList 통합할 <code>Map</code> 리스트.
	 * @return 통합된 <code>Map</code> 객체.
	 */
	public static Map<Object, Object> getIntegratedMap(List<Map<Object, Object>> mapList)
	{
		Map<Object, Object> resMap = new HashMap<Object, Object>();
		
		for (Map<Object, Object> map : mapList)
		{
			for (Object key : map.keySet())
			{
				resMap.put(key, map.get(key));
			}
		}
		
		return resMap;
	}
	
	/**
	 * <code>destMap</code>에 <code>srcMap</code>을 합친다.
	 * 
	 * @param destMap
	 * @param srcMap
	 */
	public static void doIntegrateMap(Map<Object, Object> destMap, Map<Object, Object> srcMap)
	{
		if (srcMap == null)
		{
			return;
		}
		
		for (Object key : srcMap.keySet())
		{
			destMap.put(key, srcMap.get(key));
		}
	}
	
	/**
	 * <code>List</code> 객체를 담은 <code>Map</code> 객체를 리턴한다.
	 * 
	 * @param keyString <code>Map</code>에 담을 키스트링.
	 * @param mapList <code>Map</code>에 담을 <code>List</code> 객체.
	 * @return <code>List</code> 객체를 담은 <code>Map</code> 객체.
	 */
	public static Map<Object, Object> getListMap(String keyString, List<Map<Object, Object>> mapList)
	{
		Map<Object, Object> resMap = new HashMap<Object, Object>();
		resMap.put(keyString,  mapList);
		
		return resMap;
	}
	
	/**
	 * <code>Map</code> 객체로부터 <code>Map</code> 객체를 추출한다.
	 * 
	 * @param dataMap JSON 문자열 파싱 결과 <code>Map</code> 객체.
	 * @param key 데이터 키.
	 * @return <code>Map</code> 결과 값.
	 */
	@SuppressWarnings("unchecked")
	public static Map<Object, Object> getMapValue(Map<Object, Object> dataMap, String key)
	{
		return (Map<Object, Object>) dataMap.get(key);
	}
	
	/**
	 * <code>Map</code> 객체로부터 <code>List</code> 객체를 추출한다.
	 * 
	 * @param dataMap JSON 문자열 파싱 결과 <code>Map</code> 객체.
	 * @param key 데이터 키.
	 * @return <code>List</code> 결과 값.
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<Object, Object>> getListValue(Map<Object, Object> dataMap, String key)
	{
		return (List<Map<Object, Object>>) dataMap.get(key);
	}
	
	/**
	 * <code>Map</code> 객체로부터 <code>boolean</code> 값을 추출한다. (<code>ClassCastException</code> 발생 방지 용)
	 * 
	 * @param dataMap JSON 문자열 파싱 결과 <code>Map</code> 객체.
	 * @param key 데이터 키.
	 * @return <code>boolean</code> 결과 값.
	 */
	public static boolean getBoolValue(Map<Object, Object> dataMap, String key)
	{
		return Boolean.valueOf(dataMap.get(key).toString());
	}
	
	/**
	 * <code>Map</code> 객체로부터 <code>int</code> 값을 추출한다. (<code>ClassCastException</code> 발생 방지 용)
	 * 
	 * @param dataMap JSON 문자열 파싱 결과 <code>Map</code> 객체.
	 * @param key 데이터 키.
	 * @return <code>int</code> 결과 값.
	 */
	public static int getIntValue(Map<Object, Object> dataMap, String key)
	{
		return Integer.valueOf(dataMap.get(key).toString());
	}
	
	/**
	 * <code>Map</code> 객체로부터 <code>long</code> 값을 추출한다. (<code>ClassCastException</code> 발생 방지 용)
	 * 
	 * @param dataMap JSON 문자열 파싱 결과 <code>Map</code> 객체.
	 * @param key 데이터 키.
	 * @return <code>long</code> 결과 값.
	 */
	public static long getLongValue(Map<Object, Object> dataMap, String key)
	{
		return Long.valueOf(dataMap.get(key).toString());
	}
	
	/**
	 * <code>Map</code> 객체로부터 <code>float</code> 값을 추출한다. (<code>ClassCastException</code> 발생 방지 용)
	 * 
	 * @param dataMap JSON 문자열 파싱 결과 <code>Map</code> 객체.
	 * @param key 데이터 키.
	 * @return <code>float</code> 결과 값.
	 */
	public static float getFloatValue(Map<Object, Object> dataMap, String key)
	{
		return Float.valueOf(dataMap.get(key).toString());
	}
	
	/**
	 * <code>Map</code> 객체로부터 <code>double</code> 값을 추출한다. (<code>ClassCastException</code> 발생 방지 용)
	 * 
	 * @param dataMap JSON 문자열 파싱 결과 <code>Map</code> 객체.
	 * @param key 데이터 키.
	 * @return <code>double</code> 결과 값.
	 */
	public static double getDoubleValue(Map<Object, Object> dataMap, String key)
	{
		return Double.valueOf(dataMap.get(key).toString());
	}
	
	/**
	 * <code>Map</code> 객체로부터 <code>String</code> 값을 추출한다.
	 * 
	 * @param dataMap JSON 문자열 파싱 결과 <code>Map</code> 객체.
	 * @param key 데이터 키.
	 * @return <code>String</code> 결과 값.
	 */
	public static String getStringValue(Map<Object, Object> dataMap, String key)
	{
		return (String) dataMap.get(key);
	}
	
	/**
	 * <code>Map</code> 객체로부터 <code>String</code> 값을 추출한다.
	 * 
	 * @param dataMap JSON 문자열 파싱 결과 <code>Map</code> 객체.
	 * @param key 데이터 키.
	 * @param defaultValue 키가 없을 경우 기본 값.
	 * @return <code>String</code> 결과 값.
	 */
	public static String getStringValue(Map<Object, Object> dataMap, String key, String defaultValue)
	{
		return dataMap.containsKey(key) ? getStringValue(dataMap, key) : defaultValue;
	}
	
	/**
	 * <code>Map</code> 객체로부터 <code>Long</code> 리스트 객체를 추출한다.
	 * 
	 * @param dataMap JSON 문자열 파싱 결과 <code>Map</code> 객체.
	 * @param key 데이터 키.
	 * @return <code>List</code> 결과 값.
	 */
	@SuppressWarnings("unchecked")
	public static List<Long> getLongListValue(Map<Object, Object> dataMap, String key)
	{
		return (List<Long>)dataMap.get(key);
	}
	
	/**
	 * <code>Map</code> 객체로부터 <code>Long</code> 리스트의 문자열(콤마 구분) 값을 추출한다.
	 * 
	 * @param dataMap JSON 문자열 파싱 결과 <code>Map</code> 객체.
	 * @param key 데이터 키.
	 * @return <code>String</code> 결과 값.
	 */
	public static String getLongListToStringValue(Map<Object, Object> dataMap, String key)
	{
		final char delimiter = ',';
		
		List<Long> longList = getLongListValue(dataMap, key);
		
		StringBuffer sb = new StringBuffer();
		boolean isFirst = true;
		
		for (long longValue : longList)
		{
			if (isFirst)
			{
				isFirst = false;
			}
			else
			{
				sb.append(delimiter);
			}
			
			sb.append(longValue);
		}
		
		return sb.toString();
	}
	
	/**
	 * <code>Map</code> 객체로부터 문자열(콤마 구분) 값의 <code>Long</code> 리스트를 추출한다.
	 * 
	 * @param dataMap JSON 문자열 파싱 결과 <code>Map</code> 객체.
	 * @param key 데이터 키.
	 * @return <code>List</code> 결과 값.
	 */
	public static List<Long> getStringValueToLongList(Map<Object, Object> dataMap, String key)
	{
		final String delimiter = ",";
		
		List<Long> longList = new LinkedList<Long>();
		
		String stringArray[] = getStringValue(dataMap, key).split(delimiter);
		
		for (int i = 0; i < stringArray.length; i++)
		{
			if (!stringArray[i].equals(""))
			{
				longList.add(Long.parseLong(stringArray[i]));
			}
		}
		
		return longList;
	}
	
	/**
	 * <code>HttpServletRequest</code> 객체로부터 <code>Map</code> 객체를 추출한다.
	 * 
	 * @param req <code>HttpServletRequest</code> 객체.
	 * @param key 데이터 키.
	 * @return 추출한 <code>Map</code> 객체.
	 */
	@SuppressWarnings("unchecked")
	public static Map<Object, Object> getMapValue(HttpServletRequest req, String key)
	{
		return (Map<Object, Object>) req.getAttribute(key);
	}

	/**
	 * <code>HttpServletRequest</code> 객체로부터 <code>String</code> 객체를 추출한다.
	 *
	 * @param req <code>HttpServletRequest</code> 객체.
	 * @param key 데이터 키.
	 * @return 추출한 <code>Object</code> 객체.
	 */
	public static Object getMapValueObject(HttpServletRequest req, String key)
	{
		return req.getAttribute(key);
	}
}
