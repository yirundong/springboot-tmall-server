package cn.tedu.tmall.admin.content.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CategoryCacheSchedule {

    // fixedRate：执行频率，以【上一次开始执行的时间】来计算下一次的执行时间，以毫秒为单位
    // fixedDelay：执行间隔，以【上一次执行结束的时间】来计算下一次的执行时间，以毫秒为单位
    // cron：使用cron表达式来配置，cron表达式的值是一个字符串，由6~7个域组成，各域之间使用空格分隔
    // -- 在cron表达式中，各域从左至右分别表示：秒 分 时 日 月 周（星期） [年]
    // -- 各域的值可以使用通配符
    // -- 使用星号（*）表示任意值
    // -- 使用问号（?）表示不关心此域的值，仅可以用于“日”和“周”这2个域的值
    // -- 各域的值可以使用 x/y 格式的值，x表示起始值，y表示间隔周期
    // -- 例如在“分”的域位置设置为 1/5，则表示“分”的值为1时开始执行，且每间隔5分钟执行一次
    // cron表达式示例：
    // "56 34 12 13 2 ? 2023"表示：2023年2月13日12:34:56执行任务，不关心当天星期几
    // "0/30 * * * * ?"表示：每分钟的0秒时执行，且每30秒执行一次
    // 更多内容参考：
    // https://segmentfault.com/a/1190000021574315
    // https://blog.csdn.net/study_665/article/details/123506946
    @Scheduled(cron = "0 0 10 ? 8 MON")
    public void rebuildCache() {
        log.debug("CategoryCacheSchedule.rebuildCache()");
    }

}
