package com.example.demodoubledb.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author lidaye
 * @date 2020/12/3
 */
@Configuration
@MapperScan(basePackages = {"com.example.demodoubledb.mapper.base"}, sqlSessionTemplateRef = "baseSqlSessionTemplate")
public class BaseMybatisConfig {
    @Value("${spring.datasource.base.filters}")
    String filters;
    @Value("${spring.datasource.base.driver-class-name}")
    String driverClassName;
    @Value("${spring.datasource.base.username}")
    String username;
    @Value("${spring.datasource.base.password}")
    String password;
    @Value("${spring.datasource.base.url}")
    String url;


    @Bean(name="baseDataSource")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource.base")
    public DataSource baseDataSource() throws SQLException {


        DruidDataSource druid = new DruidDataSource();
        // 监控统计拦截的filters
        druid.setFilters(filters);
        // 配置基本属性
        druid.setDriverClassName(driverClassName);
        druid.setUsername(username);
        druid.setPassword(password);
        druid.setUrl(url);

        return druid;
    }


    @Bean(name="baseSqlSessionFactory")
    @Primary
    public SqlSessionFactory baseSqlSessionFactory(@Qualifier("baseDataSource")DataSource dataSource) {
        // 创建Mybatis的连接会话工厂实例
        SqlSessionFactoryBean bean=new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //添加XML目录
        ResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
        try{
            bean.setMapperLocations(resolver.getResources("classpath:mapper/base/*Mapper.xml"));
            return bean.getObject();
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean(name="baseSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate baseSqlSessionTemplate(@Qualifier("baseSqlSessionFactory")SqlSessionFactory sqlSessionFactory) {
        SqlSessionTemplate template=new SqlSessionTemplate(sqlSessionFactory);
        return  template;
    }
}
