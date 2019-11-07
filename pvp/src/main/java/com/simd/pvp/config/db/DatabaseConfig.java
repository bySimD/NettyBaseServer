package com.simd.pvp.config.db;

import com.simd.pvp.config.consts.DBconsts;
import com.simd.pvp.db.datasource.ReplicationHikariDataSource;
import com.simd.pvp.db.properties.DatabaseProperties;
import com.simd.pvp.db.properties.SimDDatabaseProperties;
import com.simd.pvp.db.routing.GroupRoutingDatasource;
import com.simd.pvp.db.routing.RoutingDatasource;
import com.simd.pvp.db.shard.DBShardGroup;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class DatabaseConfig {

    protected Function<DatabaseProperties, ReplicationHikariDataSource> makeDataSource = (_database_properties) -> {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(_database_properties.getDriver_class_name());
        hikariConfig.setJdbcUrl(_database_properties.getUrl());
        hikariConfig.setUsername(_database_properties.getUser_name());
        hikariConfig.setPassword(_database_properties.getPassword());

        hikariConfig.setMaximumPoolSize(_database_properties.getMax_pool_size());
        hikariConfig.setConnectionTimeout(_database_properties.getConnection_timeout());
        hikariConfig.setValidationTimeout(_database_properties.getValidation_timeout());
        hikariConfig.setIdleTimeout(_database_properties.getIdle_timeout());
        hikariConfig.setMaxLifetime(_database_properties.getMax_life_time());
        hikariConfig.setMinimumIdle(_database_properties.getMinimum_idle());

        hikariConfig.setConnectionTestQuery(DBconsts.TEST_QUERY);
        hikariConfig.setPoolName(DBconsts.POOL_NAME);

        hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", _database_properties.isCache_prep_stmts());
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", _database_properties.getPrep_stmt_cache_size());
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", _database_properties.getPrep_stmt_cache_sql_limit());
        hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", _database_properties.isUse_server_prep_stmts());

        return ReplicationHikariDataSource.builder()
                .configuration(hikariConfig)
                .is_master(_database_properties.getIs_master() == 1)
                .shard_no(_database_properties.getShard_no())
                .build();
    };

    protected DBShardGroup groupDataSource(List<ReplicationHikariDataSource> _data_sources) {
        DBShardGroup dbShardGroup = new DBShardGroup();
        Map<Object,Object> data_sources =
                _data_sources.stream()
                        .filter(ReplicationHikariDataSource::is_master)
                        .collect(Collectors.toMap(e-> DBconsts.MASTER+DBconsts.UNDERSCORE+e.getShard_no(), Function.identity()));
        data_sources.putAll(_data_sources.stream()
                .filter(e->!e.is_master())
                .collect(Collectors.toMap(e-> DBconsts.SLAVE+DBconsts.UNDERSCORE+e.getShard_no(), Function.identity())));
        dbShardGroup.setDataSourceList(data_sources);
        return dbShardGroup;
    }

    protected DataSource routingDataSource(DBShardGroup _db_shard_group, boolean _is_other) {
        return GroupRoutingDatasource.builder()
                ._db_shard_group(_db_shard_group)
                ._is_other(_is_other)
                .build();
    }

    protected DataSource routingDataSource(DataSource _master_data_source, DataSource _slave_data_source) {
        return RoutingDatasource.builder()
                .master_data_source(_master_data_source)
                .slave_data_source(_slave_data_source)
                .build();
    }

    protected DataSource dataSource(DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Primary
    protected PlatformTransactionManager transactionManager(DataSource _data_source) {
        return new DataSourceTransactionManager(_data_source);
    }
}

@Configuration
@EnableConfigurationProperties(SimDDatabaseProperties.class)
@EnableTransactionManagement
class CommonDatabaseConfig extends DatabaseConfig {

    @Autowired
    private SimDDatabaseProperties simDDatabaseProperties;

    @Bean(name = "commonDataSources")
    public List<ReplicationHikariDataSource> dataSources() {
        return simDDatabaseProperties.getCommon().stream()
                .map(makeDataSource)
                .collect(Collectors.toList()
                );
    }

    @Bean(name = "commonMasterDataSource")
    public ReplicationHikariDataSource masterDataSource() {
        return dataSources().stream()
                .filter(ReplicationHikariDataSource::is_master)
                .findFirst().get();
    }

    @Bean(name = "commonSlaveDataSource")
    public ReplicationHikariDataSource slaveDataSource() {
        return dataSources().stream()
                .filter(e->!e.is_master())
                .findFirst().get();
    }

    @Bean(name = "commonRoutingDataSource")
    public DataSource routingDataSource() {
        return super.routingDataSource(masterDataSource(), slaveDataSource());
    }

    @Bean(name = "commonDataSource")
    public DataSource dataSource() {
        return super.dataSource(routingDataSource());
    }

    @Bean(name = "commonTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("commonDataSource") DataSource _data_source) {
        return super.transactionManager(_data_source);
    }
}

@Configuration
@EnableConfigurationProperties(SimDDatabaseProperties.class)
@EnableTransactionManagement
class GameDatabaseConfig extends DatabaseConfig {

    @Autowired
    private SimDDatabaseProperties simDDatabaseProperties;

    @Bean(name = "gameDataSources")
    public List<ReplicationHikariDataSource> dataSources() {
        return simDDatabaseProperties.getGame().stream()
                .map(makeDataSource)
                .collect(Collectors.toList()
                );
    }

    @Bean(name = "gameGroup")
    public DBShardGroup groupDataSource() {
        return super.groupDataSource(dataSources());
    }


    @Bean(name = "gameRoutingDataSource")
    public DataSource routingDataSource() {
        return super.routingDataSource(groupDataSource(), false);
    }

    @Bean(name = "gameDataSource")
    public DataSource dataSource() {
        return super.dataSource(routingDataSource());
    }

    @Bean(name = "gameTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("commonDataSource") DataSource _data_source) {
        return super.transactionManager(_data_source);
    }
}

@Configuration
@EnableTransactionManagement
class OtherDatabaseConfig extends DatabaseConfig {

    @Autowired @Qualifier("gameGroup") DBShardGroup _game_group;

    @Bean(name = "otherRoutingDataSource")
    public DataSource otherRoutingDataSource() {
        return super.routingDataSource(_game_group,true);
    }

    @Bean(name = "otherDataSource")
    public DataSource otherDataSource() {
        return super.dataSource(otherRoutingDataSource());
    }

    @Bean(name = "otherTransactionManager")
    public PlatformTransactionManager otherTransactionManager() {
        return super.transactionManager(otherDataSource());
    }
}

@Configuration
@EnableConfigurationProperties(SimDDatabaseProperties.class)
@EnableTransactionManagement
class LogDatabaseConfig extends DatabaseConfig {

    @Autowired private SimDDatabaseProperties simDDatabaseProperties;

    @Bean(name = "logDataSources")
    public List<ReplicationHikariDataSource> dataSources() {
        return simDDatabaseProperties.getLog().stream()
                .map(makeDataSource)
                .collect(Collectors.toList());
    }

    @Bean(name = "logGroup")
    public DBShardGroup groupDataSource() {
        return super.groupDataSource(dataSources());
    }

    @Bean(name = "logRoutingDataSource")
    public DataSource routingDataSource() {
        return super.routingDataSource(groupDataSource(),false);
    }

    @Bean(name = "logDataSource")
    public DataSource dataSource() {
        return super.dataSource(routingDataSource());
    }

    @Bean(name = "logTransactionManager")
    public PlatformTransactionManager transactionManager() {
        return super.transactionManager(dataSource());
    }
}

@Configuration
class ChainedTransactionConfig {

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(
            @Qualifier("commonTransactionManager")PlatformTransactionManager commonTxManager,
            @Qualifier("gameTransactionManager")PlatformTransactionManager gameTxManager,
            @Qualifier("otherTransactionManager")PlatformTransactionManager otherTxManager) {
        return new ChainedTransactionManager(commonTxManager, gameTxManager, otherTxManager);
    }
}
