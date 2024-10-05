package cn.tedu.tmall.admin.mall.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis的配置类
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 18:17
 */
@Slf4j
@Configuration
@MapperScan({
        "cn.tedu.tmall.admin.mall.dao.persist.mapper",
})
public class MyBatisConfiguration {

    public MyBatisConfiguration() {
        log.info("创建配置类对象：MyBatisConfiguration");
    }

}
