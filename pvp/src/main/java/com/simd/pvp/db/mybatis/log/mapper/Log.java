package com.simd.pvp.db.mybatis.log.mapper;

import com.simd.pvp.db.mybatis.log.vo.LogVo;
import com.simd.pvp.db.mybatis.support.LogSchema;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@LogSchema
public interface Log {
    int setLog(@Param("logs") List<LogVo> logs);
}