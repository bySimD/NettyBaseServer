package com.simd.pvp.db.mybatis.other.mapper;

import com.simd.pvp.db.mybatis.game.vo.MemberVo;
import com.simd.pvp.db.mybatis.support.OtherSchema;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@OtherSchema
public interface Other {
    @Transactional(readOnly = true)
    MemberVo getInfo(String mbr_no);
}