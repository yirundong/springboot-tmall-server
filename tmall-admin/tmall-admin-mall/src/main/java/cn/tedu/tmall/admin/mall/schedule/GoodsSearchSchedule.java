package cn.tedu.tmall.admin.mall.schedule;

import cn.tedu.tmall.admin.mall.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 处理es缓存中的商品数据,相当于更新
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 19:28
 */
@Slf4j
@Component
public class GoodsSearchSchedule {

    @Autowired
    private IGoodsService goodsService;

    @Scheduled(cron = "0 30 12 * * ?")
    public void rebuildSearch() {
        log.debug("开始执行【重建商品的搜索数据】计划任务");
        goodsService.rebuildSearch();
    }

}