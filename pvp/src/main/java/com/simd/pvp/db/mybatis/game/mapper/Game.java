package com.simd.pvp.db.mybatis.game.mapper;

import com.simd.pvp.db.mybatis.game.vo.MemberVo;
import com.simd.pvp.db.mybatis.support.GameSchema;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@GameSchema
public interface Game {
    @Transactional(readOnly = true)
    MemberVo getInfo(String mbr_no);
    int setInfo(MemberVo memberVo);
}