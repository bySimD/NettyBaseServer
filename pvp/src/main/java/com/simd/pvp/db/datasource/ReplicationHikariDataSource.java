package com.simd.pvp.db.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Builder;
import lombok.Data;

@Data
public class ReplicationHikariDataSource extends HikariDataSource{

	private boolean is_master;
	private int shard_no;

	@Builder
	public ReplicationHikariDataSource(HikariConfig configuration, boolean is_master, int shard_no) {
		super(configuration);
		this.is_master = is_master;
		this.shard_no = shard_no;
	}
}