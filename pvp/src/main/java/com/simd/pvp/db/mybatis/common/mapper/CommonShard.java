package com.simd.pvp.db.mybatis.common.mapper;

import com.simd.pvp.db.mybatis.common.vo.ShardMemberVo;
import com.simd.pvp.db.mybatis.common.vo.ShardVo;
import com.simd.pvp.db.mybatis.support.CommonSchema;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@CommonSchema
public interface CommonShard
{
    @Transactional(readOnly = true)
    List<ShardVo> getShard();
    @Transactional(readOnly = true)
    ShardMemberVo getShardMember(String mbr_no);
    int setShrdMember(ShardMemberVo shardMemberVo);
}
