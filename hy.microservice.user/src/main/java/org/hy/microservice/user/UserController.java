package org.hy.microservice.user;

import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.user.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;





/**
 * 用户中心的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-06
 * @version     v1.0
 */
@Controller
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
    
    
    
    
}
