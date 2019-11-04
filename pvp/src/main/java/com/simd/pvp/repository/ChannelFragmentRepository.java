package com.simd.pvp.repository;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChannelFragmentRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<ChannelHandlerContext, List<byte[]>> fragmentCacheMap = new HashMap<>();

    public List<byte[]> getFragmentCache(ChannelHandlerContext ctx) {
//        List<byte[]> fragmentCache = fragmentCacheMap.get(ctx);
//        if (fragmentCache == null) {
//            fragmentCache = new ArrayList<>();
//            fragmentCacheMap.put(ctx, fragmentCache);
//        }
//        return fragmentCache;
        return fragmentCacheMap.computeIfAbsent(ctx, k -> new ArrayList<byte[]>());
    }

    public void clearFragmentCache(ChannelHandlerContext ctx) {
        List<byte[]> fragmentCache = fragmentCacheMap.get(ctx);
        if (fragmentCache != null) {
            fragmentCache.clear();
        }
    }

    public void removeFragmentCache(ChannelHandlerContext ctx) {
        fragmentCacheMap.remove(ctx);
    }

}
