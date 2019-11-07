package com.simd.pvp.db.routing;

import com.google.common.base.Preconditions;
import com.simd.pvp.config.consts.DBconsts;
import com.simd.pvp.db.shard.DBShardGroup;
import com.simd.pvp.db.shard.DBShardSelector;
import lombok.Builder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

// Game DB와 같이 마스터1~10/슬레이브1~10 그룹으로 라우팅해야할 경우
public class GroupRoutingDatasource extends AbstractRoutingDataSource {

	private DBShardGroup db_shard_group;
	private boolean is_other;

	@Builder
	public GroupRoutingDatasource(DBShardGroup _db_shard_group, boolean _is_other) {
		this.db_shard_group = _db_shard_group;
		this.is_other = _is_other;
	}

	@Override
	public void afterPropertiesSet() {

		Preconditions.checkArgument(db_shard_group != null,  new IllegalArgumentException("Property 'db_shard_group' is required"));

		setTargetDataSources(db_shard_group.getDataSourceList());
		super.afterPropertiesSet();
	}

	@Override
	protected Object determineCurrentLookupKey() {

		boolean is_read_only = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
		int shard_no = this.is_other ?
				(DBShardSelector.getOther_shard_no().get() != null ? DBShardSelector.getOther_shard_no().get() : 1)
				: (DBShardSelector.getShard_no().get() != null ? DBShardSelector.getShard_no().get() : 1);

		String lookup_key = DBconsts.UNDERSCORE+shard_no;

		if(is_read_only == true) {
			return DBconsts.SLAVE+lookup_key;
		} else {
			return DBconsts.MASTER+lookup_key;
		}
	}
}