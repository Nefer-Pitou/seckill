package com.liyingyu.seckill.dao;

import com.liyingyu.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by YingyuLi on 2018/2/5.
 */
//配置Spring和Junit整合，Junit启动时加载SpringIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit所要加载的spring配置文件
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() throws Exception {
        /***
         * 第一次运行：插入 1 条数据
         * 第二次运行：插入 0 条数据
         */
        long seckillId = 1000L;
        long userPhone = 1312323201L;
        int count = successKilledDao.insertSuccessKilled(seckillId,userPhone);
        System.out.println("插入 "+count+" 条数据");
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long seckillId = 1001L;
        long userPhone = 13102031021L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());

    }

}