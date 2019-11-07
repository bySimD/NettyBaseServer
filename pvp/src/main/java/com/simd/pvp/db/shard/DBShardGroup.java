package com.simd.pvp.db.shard;

import java.util.Map;

public class DBShardGroup {
	private Map<Object, Object> data_source_list;

	public Map<Object, Object> getDataSourceList() {
		return data_source_list;
	}
	public void setDataSourceList(Map<Object, Object> _data_source_list) {
		this.data_source_list = _data_source_list;
	}
}
