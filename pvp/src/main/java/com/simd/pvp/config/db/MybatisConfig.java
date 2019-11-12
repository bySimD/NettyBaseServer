package com.simd.pvp.config.db;

import com.simd.pvp.db.mybatis.support.CommonSchema;
import com.simd.pvp.db.mybatis.support.GameSchema;
import com.simd.pvp.db.mybatis.support.LogSchema;
import com.simd.pvp.db.mybatis.support.OtherSchema;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

public abstract class MyBatisConfig {
    protected static final String BASE_PACKAGE_PREFIX = "com.simd.pvp.db";
    protected static final String ENTITY_PACKAGE_PREFIX = "com.simd.pvp.db.mybatis.*.vo";
    protected static final String CONFIG_LOCATION_PATH = "classpath:mybatis/mybatis-config.xml";

    protected static final String MAPPER_LOCATIONS_PATH_COMMON = "classpath:mybatis/common/*.xml";
    protected static final String MAPPER_LOCATIONS_PATH_GAME = "classpath:mybatis/game/*.xml";
    protected static final String MAPPER_LOCATIONS_PATH_OTHER = "classpath:mybatis/other/*.xml";
    protected static final String MAPPER_LOCATIONS_PATH_LOG = "classpath:mybatis/log/*.xml";


    protected void configureSqlSessionFactory(SqlSessionFactoryBean _session_factory_bean, DataSource _data_source, String _mapper_path) throws IOException {
        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
        _session_factory_bean.setDataSource(_data_source);
        _session_factory_bean.setTypeAliasesPackage(ENTITY_PACKAGE_PREFIX);
        _session_factory_bean.setConfigLocation(pathResolver.getResource(CONFIG_LOCATION_PATH));
        _session_factory_bean.setMapperLocations(pathResolver.getResources(_mapper_path));
    }
}

@Configuration
@MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE_PREFIX, annotationClass = CommonSchema.class, sqlSessionFactoryRef = "commonSqlSessionFactory")
class CommonMybatisConfig extends MyBatisConfig {

    @Bean(name = "commonSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("commonDataSource") DataSource _data_source) throws Exception {
        SqlSessionFactoryBean session_factory_bean = new SqlSessionFactoryBean();
        configureSqlSessionFactory(session_factory_bean, _data_source, MAPPER_LOCATIONS_PATH_COMMON);
        return session_factory_bean.getObject();
    }

    @Bean(name = "commonSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("commonSqlSessionFactory") SqlSessionFactory _sql_session_factory) throws Exception {

        return new SqlSessionTemplate(_sql_session_factory);
    }
}

@Configuration
@MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE_PREFIX, annotationClass = GameSchema.class, sqlSessionFactoryRef = "gameSqlSessionFactory")
class GameMybatisConfig extends MyBatisConfig {

    @Bean(name = "gameSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("gameDataSource") DataSource _data_source) throws Exception {
        SqlSessionFactoryBean session_factory_bean = new SqlSessionFactoryBean();
        configureSqlSessionFactory(session_factory_bean, _data_source, MAPPER_LOCATIONS_PATH_GAME);
        return session_factory_bean.getObject();
    }

    @Bean(name = "gameSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("gameSqlSessionFactory") SqlSessionFactory _sql_session_factory) throws Exception {

        return new SqlSessionTemplate(_sql_session_factory);
    }
}

@Configuration
@MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE_PREFIX, annotationClass = OtherSchema.class, sqlSessionFactoryRef = "otherSqlSessionFactory")
class OtherMybatisConfig extends MyBatisConfig {

    @Bean(name = "otherSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("otherDataSource") DataSource _data_source) throws Exception {
        SqlSessionFactoryBean session_factory_bean = new SqlSessionFactoryBean();
        configureSqlSessionFactory(session_factory_bean, _data_source, MAPPER_LOCATIONS_PATH_OTHER);
        return session_factory_bean.getObject();
    }

    @Bean(name = "otherSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("otherSqlSessionFactory") SqlSessionFactory _sql_session_factory) throws Exception {

        return new SqlSessionTemplate(_sql_session_factory);
    }
}

@Configuration
@MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE_PREFIX, annotationClass = LogSchema.class, sqlSessionFactoryRef = "logSqlSessionFactory")
class LogMybatisConfig extends MyBatisConfig {

    @Bean(name = "logSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("logDataSource") DataSource _data_source) throws Exception {
        SqlSessionFactoryBean session_factory_bean = new SqlSessionFactoryBean();
        configureSqlSessionFactory(session_factory_bean, _data_source, MAPPER_LOCATIONS_PATH_LOG);
        return session_factory_bean.getObject();
    }

    @Bean(name = "logSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("logSqlSessionFactory") SqlSessionFactory _sql_session_factory) throws Exception {

        return new SqlSessionTemplate(_sql_session_factory);
    }
}