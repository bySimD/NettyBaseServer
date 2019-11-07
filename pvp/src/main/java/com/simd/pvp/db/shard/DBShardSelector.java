package com.simd.pvp.db.shard;

public class DBShardSelector {
	private static ThreadLocal<Integer> shard_no = new ThreadLocal<>();
	private static ThreadLocal<Integer> other_shard_no = new ThreadLocal<>();

	public static void resetShardNo() {
		shard_no.remove();
	}
	public static void resetOtherShardNo() {
		other_shard_no.remove();
	}

	public static ThreadLocal<Integer> getShard_no() {
		return shard_no;
	}
	public static void setShard_no(int _shard_no) {
		DBShardSelector.shard_no.set(_shard_no);
	}
	public static ThreadLocal<Integer> getOther_shard_no() {
		return other_shard_no;
	}
	public static void setOther_shard_no(int _other_shard_no) {
		DBShardSelector.other_shard_no.set(_other_shard_no);
	}
}