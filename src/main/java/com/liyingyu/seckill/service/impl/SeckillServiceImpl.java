package com.liyingyu.seckill.service.impl;

import com.liyingyu.seckill.dao.SeckillDao;
import com.liyingyu.seckill.dao.SuccessKilledDao;
import com.liyingyu.seckill.dto.Exposer;
import com.liyingyu.seckill.dto.SeckillExecution;
import com.liyingyu.seckill.entity.Seckill;
import com.liyingyu.seckill.entity.SuccessKilled;
import com.liyingyu.seckill.enums.SeckillStateEnum;
import com.liyingyu.seckill.exception.RepeatKillException;
import com.liyingyu.seckill.exception.SeckillCloseException;
import com.liyingyu.seckill.exception.SeckillException;
import com.liyingyu.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by YingyuLi on 2018/2/5.
 */
//@Component  @Controller
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //注入Service依赖，SeckillDao的实现类由MyBatis自动生成并配置到Spring容器中
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    //用于混淆md5
    private final String slat = "JKLHjhkj&*(%gGKG￥%#%……%&￥#J";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转化特定字符串过程，不可逆
        String md5 = getMd5(seckillId);
        return new Exposer(true, md5, seckillId);
    }
    //通过商品id和slat生成MD5
    private String getMd5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 使用注解控制事务方法的优点：
     * 1、开发团队达成一致约定，明确标注事物方法的编程风格
     * 2、保证事务方法的执行时间尽可能短，不要穿插其他网络操作如网络请求和本地IO请求或剥离到食物方法外
     * 3、不是所有方法都需要事务
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMd5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        Date nowTime = new Date();
        try{
            //减库存
            int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
            if(updateCount <= 0){
                //没有更新到记录，秒杀已经结束了
                throw new SeckillCloseException("seckill is closed");
            }else {
                //秒杀成功了，记录用户秒杀记录
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    //用户重复秒杀
                    throw new RepeatKillException("seckill repeated");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        }catch(SeckillCloseException e1){
            logger.error(e1.getMessage(),e1);
            throw e1;
        }catch(RepeatKillException e2){
            logger.error(e2.getMessage(),e2);
            throw e2;
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            //所有编译器异常转化为运行期异常
            throw new SeckillException("seckill inner Error"+e.getMessage());
        }
    }
}
