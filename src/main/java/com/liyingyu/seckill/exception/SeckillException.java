package com.liyingyu.seckill.exception;

/**
 * Created by YingyuLi on 2018/2/5.
 */
public class SeckillException extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
