package com.simd.pvp.db.mybatis.log.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;

@Alias("LogVo")
@Data
public class LogVo {
	@Getter @Setter	private String mbr_no;
	@Getter @Setter	private Timestamp reg_date;
	@Getter @Setter	private String extra_data = "{}";

}