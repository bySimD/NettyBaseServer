package com.simd.pvp.util.etc;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * 기타 유틸 글래스.
 */
public class EtcUtil
{
	/**
	 * 유티크 아이디를 생성한다.
	 * 
	 * @return 유니크 아이디.
	 */
	public static long generateUniqueID()
	{
		long uniqueID = -1;
		
		do
		{
			UUID uid = UUID.randomUUID();
			ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
			
			buffer.putLong(uid.getLeastSignificantBits());
			buffer.putLong(uid.getMostSignificantBits());
			
			BigInteger bi = new BigInteger(buffer.array());
			uniqueID = bi.longValue();
		} 
		while (uniqueID < 0);
		
		return uniqueID;
	}
}
