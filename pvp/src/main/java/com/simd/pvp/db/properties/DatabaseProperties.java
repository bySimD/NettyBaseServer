package com.simd.pvp.db.properties;

import lombok.Data;

@Data
public class DatabaseProperties {
    private String driver_class_name;

    private String url;

    private String user_name;

    private String password;

    private int max_pool_size;

    private int connection_timeout;

    private int validation_timeout;

    private int idle_timeout;

    private int max_life_time;

    private int maximum_idle;

    private int minimum_idle;

    private boolean cache_prep_stmts;

    private int prep_stmt_cache_size;

    private int prep_stmt_cache_sql_limit;

    private boolean use_server_prep_stmts;

    private short is_master;

    private int shard_no;

}
