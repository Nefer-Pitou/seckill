package com.liyingyu.seckill.exception;

/**
 * 秒杀关闭异常
 * Created by YingyuLi on 2018/2/5.
 */
public class SeckillCloseException extends SeckillException{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
