package org.hy.microservice.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.user.user.UserSSO;
import org.hy.microservice.user.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @Qualifier("UserInfoService")
    private UserInfoService UserInfoService;
    
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
     * @param i_Token        认证票据号
     * @param i_CreateUser   创建的用户信息
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="createUser" ,produces=MediaType.APPLICATION_JSON_VALUE ,method= {RequestMethod.POST})
    @ResponseBody
    public BaseResponse<UserInfo> createUser(@RequestParam(value="token" ,required=false) String i_Token
                                            ,@RequestBody UserInfo i_CreateUser
                                            ,HttpServletRequest    i_Request
                                            ,HttpServletResponse   i_Response)
    {
        BaseResponse<UserInfo> v_RetResp = new BaseResponse<UserInfo>();
        
        UserSSO v_User = null;
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            // 验证票据及用户登录状态
            if ( Help.isNull(i_Token) )
            {
                return v_RetResp.setCode("-901").setMessage("非法访问");
            }
            
            v_User = this.userService.getUser(i_Token);
            if ( v_User == null )
            {
                return v_RetResp.setCode("-901").setMessage("非法访问");
            }
        }
        
        UserInfo v_CreateUser = this.UserInfoService.createUser(i_CreateUser);
        if ( v_CreateUser == null )
        {
            return v_RetResp.setCode("-911").setMessage("创建用户失败");
        }
        
        return v_RetResp.setData(v_CreateUser);
    }
    
}
