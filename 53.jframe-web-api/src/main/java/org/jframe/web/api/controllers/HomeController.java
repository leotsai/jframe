package org.jframe.web.api.controllers;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jframe.infrastructure.web.StandardJsonResult;
import org.jframe.web.controllers._ControllerBase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by leo on 2017/5/7.
 */
@RestController("api-home")
@RequestMapping("/api/home")
@Api(value = "/group", description = "home的相关操作")
public class HomeController extends _ApiControllerBase {
    @RequestMapping("/index")
    public StandardJsonResult index() throws Exception{
        return super.tryJson(()-> "home-ind阿达看看卡斯柯达斯柯达斯柯达很快就卡萨丁卡卡ex");
    }

    @RequestMapping("/str")
    @ApiOperation(notes = "getAccessibleGroups", httpMethod = "GET", value = "str")
    public String str(@ApiParam(required = true, value = "p") String p){
        return "按时大大刷卡机按计划时间啊可视电话";
    }

}
