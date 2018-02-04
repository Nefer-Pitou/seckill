package com.liyingyu.seckill.dao;

import com.liyingyu.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by YingyuLi on 2018/2/5.
 */
//配置Spring和Junit整合，Junit启动时加载SpringIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit所要加载的spring配置文件
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SeckillDaoTest {
    @Resource
    private SeckillDao seckillDao;
    @Test
    public void reduceNumber() throws Exception {
        Date date = new Date();
        int count = seckillDao.reduceNumber(1000L,date);
        System.out.println("减少了 "+count+" 件库存");
    }

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getSeckillId()+" : "+seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(0,10);
        for(Seckill s : seckills){
            System.out.println(s);
        }
    }

}