package com.liyingyu.seckill.service.impl;

import com.liyingyu.seckill.dto.Exposer;
import com.liyingyu.seckill.dto.SeckillExecution;
import com.liyingyu.seckill.entity.Seckill;
import com.liyingyu.seckill.service.SeckillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YingyuLi on 2018/2/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-*.xml")
public class SeckillServiceImplTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private SeckillService seckillService;
//
//    public void setSeckillService(SeckillService seckillService) {
//        this.seckillService = seckillService;
//    }

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() throws Exception {
        Seckill seckill = seckillService.getById(1000L);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        Exposer exposer = seckillService.exportSeckillUrl(1002L);
        logger.info("exposer={}",exposer);
        System.out.println(exposer);
    }

    @Test
    public void executeSeckill() throws Exception {
        SeckillExecution seckillExecution = seckillService.executeSeckill(1002L,13011021201L,"d7556b2f79bde11d61d1265356083546");
        logger.info("seckillExecution={}",seckillExecution);
        System.out.println(seckillExecution);
    }

}