package cn.tedu.tmall.admin.mall.preload;

import cn.tedu.tmall.admin.mall.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 项目一启动就要在es中加载商品数据
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 19:34
 */
@Slf4j
@Component
public class GoodsSearchPreload implements ApplicationRunner {

    @Autowired
    private IGoodsService goodsService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("开始执行【重建商品的搜索数据】的数据预热");
        goodsService.rebuildSearch();
    }

}