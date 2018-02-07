package com.liyingyu.seckill.service;

import com.liyingyu.seckill.dto.Exposer;
import com.liyingyu.seckill.dto.SeckillExecution;
import com.liyingyu.seckill.entity.Seckill;
import com.liyingyu.seckill.exception.RepeatKillException;
import com.liyingyu.seckill.exception.SeckillCloseException;
import com.liyingyu.seckill.exception.SeckillException;

import java.util.List;

/**
 * Created by YingyuLi on 2018/2/5.
 */
public interface SeckillService {
    /**
     * 查询所有秒杀商品信息
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询特定Id的秒杀商品信息
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 若秒杀开启则输出秒杀接口地址，
     * 否则输出系统当前时间和秒杀开启时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId 秒杀商品Id
     * @param userPhone 用户手机号
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException,RepeatKillException,SeckillCloseException;
}
