package org.hy.microservice.user;

import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.user.role.RoleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;





/**
 * 用户中心：角色的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-18
 * @version     v1.0
 */
@Controller
@RestController
@RequestMapping("role")
public class RoleController
{
    
    private static final Logger $Logger = new Logger(RoleController.class);
    
    @Autowired
    @Qualifier("MS_User_CheckIllegalChar")
    private Param roleIllegalChar;
    
    @Autowired
    @Qualifier("RoleInfoService")
    private RoleInfoService roleService;
    
}
