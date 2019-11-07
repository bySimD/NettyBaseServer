package com.simd.pvp.db.routing;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.simd.pvp.config.consts.DBconsts;
import lombok.Builder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.Map;

// Common DB와 같이 마스터 슬레이브로만 라우팅해야할 경우
public class RoutingDatasource extends AbstractRoutingDataSource {
    private DataSource master_data_source;
    private DataSource slave_data_source;

    @Builder
    public RoutingDatasource(DataSource master_data_source, DataSource slave_data_source) {
        this.master_data_source = master_data_source;
        this.slave_data_source = slave_data_source;
    }

    @Override
    public void afterPropertiesSet() {

        Preconditions.checkArgument(master_data_source != null,  new IllegalArgumentException("Property 'data_source' is required"));

        Map<Object,Object> target_data_source = Maps.newHashMap();
        target_data_source.put(DBconsts.MASTER,master_data_source);
        target_data_source.put(DBconsts.SLAVE,slave_data_source);
        setTargetDataSources(target_data_source);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {

        boolean is_read_only = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if(is_read_only == true) {
            return DBconsts.SLAVE;
        } else {
            return DBconsts.MASTER;
        }
    }
}
