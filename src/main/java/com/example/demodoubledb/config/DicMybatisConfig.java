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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author lidaye
 * @date 2020/12/3
 */
@Configuration
@MapperScan(basePackages = {"com.example.demodoubledb.mapper.dic"}, sqlSessionTemplateRef = "dicSqlSessionTemplate")
public class DicMybatisConfig {
    @Value("${spring.datasource.dic.filters}")
    String filters;
    @Value("${spring.datasource.dic.driver-class-name}")
    String driverClassName;
    @Value("${spring.datasource.dic.username}")
    String username;
    @Value("${spring.datasource.dic.password}")
    String password;
    @Value("${spring.datasource.dic.url}")
    String url;


    @Bean(name="dicDataSource")
    @ConfigurationProperties(prefix="spring.datasource.dic")
    public DataSource dicDataSource() throws SQLException {


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


    @Bean(name="dicSqlSessionFactory")
    public SqlSessionFactory dicSqlSessionFactory(@Qualifier("dicDataSource")DataSource dataSource) {
        // 创建Mybatis的连接会话工厂实例
        SqlSessionFactoryBean bean=new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //添加XML目录
        ResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
        try{
            bean.setMapperLocations(resolver.getResources("classpath:mapper/dic/*Mapper.xml"));
            return bean.getObject();
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean(name="dicSqlSessionTemplate")
    public SqlSessionTemplate dicSqlSessionTemplate(@Qualifier("dicSqlSessionFactory")SqlSessionFactory sqlSessionFactory) {
        SqlSessionTemplate template=new SqlSessionTemplate(sqlSessionFactory);
        return  template;
    }
}
