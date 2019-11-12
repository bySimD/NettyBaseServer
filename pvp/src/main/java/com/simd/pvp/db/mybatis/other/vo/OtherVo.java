package com.simd.pvp.db.mybatis.other.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;

@Alias("OtherVo")
@Data
public class OtherVo {
	@Getter @Setter	private String mbr_no;
	@Getter @Setter	private Timestamp reg_date;
	@Getter @Setter	private String extra_data = "{}";

}