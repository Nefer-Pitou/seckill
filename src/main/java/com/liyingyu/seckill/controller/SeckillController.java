package com.liyingyu.seckill.controller;

import com.liyingyu.seckill.dto.Exposer;
import com.liyingyu.seckill.dto.SeckillExecution;
import com.liyingyu.seckill.dto.SeckillResult;
import com.liyingyu.seckill.entity.Seckill;
import com.liyingyu.seckill.enums.SeckillStateEnum;
import com.liyingyu.seckill.exception.RepeatKillException;
import com.liyingyu.seckill.exception.SeckillCloseException;
import com.liyingyu.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by YingyuLi on 2018/2/8.
 */
@Controller
@RequestMapping("/seckill") //url:/模块/资源/{id}/细分
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;
    @RequestMapping(value = "/list", method= RequestMethod.GET)
    public String list(Model model){
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list",list);
        ///list.jsp+model = ModelAndView
        return "list";//WEB-INF/jsps/list.jsp
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId == null){
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if(seckill == null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";//WEB-INF/jsps/detail.jsp
    }

    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.GET,produces = {"application/json;charset=utf-8"})
    @ResponseBody //将返回的数据类型封装成json
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result = new SeckillResult<Exposer>(false,e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/{seckillId}/{md5}/execution", method = RequestMethod.POST,produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "userPhone",required = false) Long userPhone){
        if(null == userPhone){
            logger.info("用户号码："+userPhone);
            return new SeckillResult<SeckillExecution>(false,"用户未验证");
        }
        SeckillResult<SeckillExecution> result;
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId,userPhone,md5);
            result = new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (RepeatKillException e1){
            logger.error(e1.getMessage());
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            result = new SeckillResult<SeckillExecution>(true,execution);
        }catch (SeckillCloseException e2) {
            logger.error(e2.getMessage());
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            result = new SeckillResult<SeckillExecution>(true, execution);
        }catch (Exception e){
            logger.error(e.getMessage());
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            result = new SeckillResult<SeckillExecution>(true,execution);
        }
        return result;
    }

    //获取系统当前时间
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult<Long>(true,now.getTime());
    }

}
