package com.simd.pvp.db.mybatis.common.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;

@Alias("ShardMemberVo")
@Data
public class ShardMemberVo {
	@Getter @Setter private String mbr_no;
	@Getter @Setter private int shard_no;
	@Getter @Setter	private Timestamp created_date;

}
