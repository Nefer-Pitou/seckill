package com.liyingyu.seckill.dao;

import com.liyingyu.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by YingyuLi on 2018/2/3.
 */
public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId 商品ID
     * @param killTime 进行减库存时的时间
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    /**
     * 根据商品ID查询商品信息
     * @param seckillId 商品ID
     * @return 商品信息
     */
    Seckill queryById(@Param("seckillId") long seckillId);

    /**
     * @param offset 起始下标
     * @param limit 偏移量（即要查的数量）
     * @return 所要查询的商品信息列表
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}
