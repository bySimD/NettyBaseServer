package com.simd.pvp.db.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = SimDDatabaseProperties.PREFIX)
public class SimDDatabaseProperties {
    public static final String PREFIX = "datasource";

    private List<DatabaseProperties> common;
    private List<DatabaseProperties> game;
    private List<DatabaseProperties> log;


}
