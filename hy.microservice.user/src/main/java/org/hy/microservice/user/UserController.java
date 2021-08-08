package org.hy.microservice.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.user.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;





/**
 * 用户中心的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-06
 * @version     v1.0
 */
@Controller
@RestController
@RequestMapping("user")
public class UserController
{
    
    private static final Logger $Logger = new Logger(UserController.class);
    
    @Autowired
    @Qualifier("UserService")
    private UserService userService;
    
    @Autowired
    @Qualifier("MS_User_IsCheckToken")
    private Param isCheckToken;
    
    
    
    /**
     * 用户登录接口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-08
     * @version     v1.0
     * 
     * @param i_UserName   用户名称
     * @param i_Password   用户密码
     * @param i_CheckSMS   验证码
     * @param i_OpenID     微信OpenID
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="qq" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<String> login(@RequestParam(value="loginName" ,required=false) String i_UserName
                                     ,@RequestParam(value="password"  ,required=false) String i_Password
                                     ,@RequestParam(value="checkSMS"  ,required=false) String i_CheckSMS
                                     ,@RequestParam(value="openID"    ,required=false) String i_OpenID
                                     ,HttpServletRequest  i_Request
                                     ,HttpServletResponse i_Response)
    {
        
        $Logger.info("qq");
        
        return new BaseResponse<String>();
    }
    
}
