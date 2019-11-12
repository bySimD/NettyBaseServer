package com.simd.pvp.db.mybatis.common.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Alias("ShardVo")
@Data
public class ShardVo {
	@Getter @Setter	private int shard_no;
	@Getter @Setter	private int user_count;
	@Getter @Setter	private byte acceptable;
	@Getter @Setter	private String extra_data;
}