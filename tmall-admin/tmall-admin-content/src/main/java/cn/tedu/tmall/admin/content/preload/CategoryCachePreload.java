package cn.tedu.tmall.admin.content.preload;

import cn.tedu.tmall.admin.content.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

@Deprecated
@Slf4j
// @Component
public class CategoryCachePreload implements ApplicationRunner {

    @Autowired
    private ICategoryService categoryService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        categoryService.rebuildCache();
    }

}
