package com.liyingyu.seckill.exception;

/**
 * 重复秒杀异常（运行时异常）
 * Created by YingyuLi on 2018/2/5.
 */
public class RepeatKillException extends SeckillException{
    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatKillException(String message) {

        super(message);
    }
}
