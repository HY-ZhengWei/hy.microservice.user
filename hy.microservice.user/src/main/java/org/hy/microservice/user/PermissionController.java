package org.hy.microservice.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.user.permission.Permission;
import org.hy.microservice.user.permission.PermissionService;
import org.hy.microservice.user.role.RoleRelactionService;
import org.hy.microservice.user.user.UserSSO;
import org.hy.microservice.user.user.UserService;
import org.hy.microservice.user.userInfo.UserInfo;
import org.hy.microservice.user.userInfo.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;





/**
 * 用户中心：权限项的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-23
 * @version     v1.0
 */
@Controller
@RestController
@RequestMapping("permission")
public class PermissionController
{
    
    private static final Logger $Logger = new Logger(PermissionController.class);
    
    @Autowired
    @Qualifier("MS_User_IsCheckToken")
    private Param isCheckToken;
    
    @Autowired
    @Qualifier("MS_User_CheckIllegalChar")
    private Param permissionIllegalChar;
    
    @Autowired
    @Qualifier("UserService")
    private UserService userService;
    
    @Autowired
    @Qualifier("UserInfoService")
    private UserInfoService userInfoService;
    
    @Autowired
    @Qualifier("PermissionService")
    private PermissionService permissionService;
    
    @Autowired
    @Qualifier("RoleRelactionService")
    private RoleRelactionService roleRelactionService;
    
    
    
    /**
     * 添加权限项(应用级)。
     * 
     * 注意：不能添加系统级的权限项
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-23
     * @version     v1.0
     * 
     * @param i_Token        认证票据号
     * @param i_Permission   创建的权限项信息
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="addPermission" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<Permission> addPermission(@RequestParam(value="token" ,required=false) String i_Token
                                                 ,@RequestBody Permission i_Permission
                                                 ,HttpServletRequest      i_Request
                                                 ,HttpServletResponse     i_Response)
    {
        BaseResponse<Permission> v_RetResp = new BaseResponse<Permission>();
        
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
            
            if ( !v_User.getAppKey().equals(i_Permission.getAppKey()) )
            {
                return v_RetResp.setCode("-901").setMessage("无权访问");
            }
            
            // 当验证用户登录会话时，谁登录创建者即是谁
            i_Permission.setCreaterID(v_User.getId());
        }
        
        if ( i_Permission == null || Help.isNull(i_Permission.getPermissionName()) )
        {
            $Logger.info("创建权限项：权限项名称为空");
            return v_RetResp.setCode("902").setMessage("创建权限项：权限项名称为空");
        }
        
        if ( Help.isNull(i_Permission.getAppKey()) )
        {
            $Logger.info("创建权限项：所属系统编号为空");
            return v_RetResp.setCode("903").setMessage("创建权限项：所属系统编号为空");
        }
        
        if ( Help.isNull(i_Permission.getPermissionCode()) )
        {
            $Logger.info("创建权限项：权限编号为空");
            return v_RetResp.setCode("904").setMessage("创建权限项：权限编号为空");
        }
        
        String [] v_RoleIllegalChar = this.permissionIllegalChar.getValue().split("-");
        if ( StringHelp.isContains(i_Permission.getPermissionCode() ,v_RoleIllegalChar) )
        {
            $Logger.info("创建权限项：权限项编号禁止非法字符" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionCode() + "：" + i_Permission.getPermissionName());
            return v_RetResp.setCode("905").setMessage("创建权限项：权限项编号禁止非法字符");
        }
        
        if ( StringHelp.isContains(i_Permission.getPermissionName() ,v_RoleIllegalChar) )
        {
            $Logger.info("创建权限项：权限项名称禁止非法字符" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionCode() + "：" + i_Permission.getPermissionName());
            return v_RetResp.setCode("905").setMessage("创建权限项：权限项名称禁止非法字符");
        }
        
        if ( !Help.isNull(i_Permission.getPermissionType()) )
        {
            if ( StringHelp.isContains(i_Permission.getPermissionType() ,v_RoleIllegalChar) )
            {
                $Logger.info("创建权限项：权限项类型禁止非法字符" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionCode() + "：" + i_Permission.getPermissionName());
                return v_RetResp.setCode("905").setMessage("创建权限项：权限项类型禁止非法字符");
            }
        }
        
        if ( !Help.isNull(i_Permission.getPermissionLevel()) )
        {
            if ( StringHelp.isContains(i_Permission.getPermissionLevel() ,v_RoleIllegalChar) )
            {
                $Logger.info("创建权限项：权限项级别禁止非法字符" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionCode() + "：" + i_Permission.getPermissionName());
                return v_RetResp.setCode("905").setMessage("创建权限项：权限项级别禁止非法字符");
            }
        }
        
        if ( !Help.isNull(i_Permission.getRemarks()) )
        {
            if ( StringHelp.isContains(i_Permission.getRemarks() ,v_RoleIllegalChar) )
            {
                $Logger.info("创建权限项：权限项说明禁止非法字符" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionCode() + "：" + i_Permission.getPermissionName());
                return v_RetResp.setCode("905").setMessage("创建权限项：权限项说明禁止非法字符");
            }
        }
        
        
        if ( Help.isNull(i_Permission.getCreaterID()) )
        {
            $Logger.info("创建权限项：创建者编号为空或不存在" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionCode() + "：" + i_Permission.getPermissionName());
            return v_RetResp.setCode("906").setMessage("创建权限项：创建者编号为空或不存在");
        }
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            if ( !i_Permission.getAppKey().equals(v_User.getAppKey()) )
            {
                $Logger.info("创建权限项：创建者不能跨系统操作" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionCode() + "：" + i_Permission.getPermissionName());
                return v_RetResp.setCode("907").setMessage("创建权限项：创建者不能跨系统操作");
            }
        }
        else
        {
            UserInfo v_Creater = this.userInfoService.queryUserGID(i_Permission.getAppKey() ,i_Permission.getCreaterID());
            if ( v_Creater == null )
            {
                $Logger.info("创建权限项：创建者编号为空或不存在" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionCode() + "：" + i_Permission.getPermissionName());
                return v_RetResp.setCode("911").setMessage("创建权限项：创建者编号为空或不存在");
            }
            
            if ( !i_Permission.getAppKey().equals(v_Creater.getAppKey()) )
            {
                $Logger.info("创建权限项：创建者不能跨系统操作" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionCode() + "：" + i_Permission.getPermissionName());
                return v_RetResp.setCode("912").setMessage("创建权限项：创建者不能跨系统操作");
            }
        }
        
        
        Permission v_Permission = this.permissionService.queryByCode(i_Permission);
        if ( v_Permission != null )
        {
            $Logger.info("创建权限项：权限项已存，请勿重复创建" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionCode() + "：" + i_Permission.getPermissionName());
            return v_RetResp.setCode("913").setMessage("创建权限项：权限项已存，请勿重复创建");
        }
        
        v_Permission = this.permissionService.addPermission(i_Permission);
        if ( v_Permission == null )
        {
            $Logger.info("创建权限项失败：" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionCode() + "：" + i_Permission.getPermissionName());
            return v_RetResp.setCode("914").setMessage("创建权限项失败");
        }
        else
        {
            $Logger.info("创建权限项成功" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionCode() + "：" + i_Permission.getPermissionName());
            return v_RetResp.setData(v_Permission);
        }
    }
    
    
    
    /**
     * 更新权限项(应用级)。
     * 
     * 注意：不能更新系统级的权限项
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-23
     * @version     v1.0
     * 
     * @param i_Token       认证票据号
     * @param i_Permission  权限项信息
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="editPermission" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<String> editPermission(@RequestParam(value="token" ,required=false) String i_Token
                                              ,@RequestBody Permission i_Permission
                                              ,HttpServletRequest      i_Request
                                              ,HttpServletResponse     i_Response)
    {
        BaseResponse<String> v_RetResp = new BaseResponse<String>();
        
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
            
            if ( !v_User.getAppKey().equals(i_Permission.getAppKey()) )
            {
                return v_RetResp.setCode("-901").setMessage("无权访问");
            }
            
            // 当验证用户登录会话时，谁登录创建者即是谁
            i_Permission.setUpdaterID(v_User.getId());
        }
        
        if ( i_Permission == null || Help.isNull(i_Permission.getPermissionID()) )
        {
            $Logger.info("编辑权限项：权限项ID为空");
            return v_RetResp.setCode("902").setMessage("编辑权限项：权限项ID为空");
        }
        
        if ( Help.isNull(i_Permission.getPermissionName()) )
        {
            $Logger.info("编辑权限项：权限项名称为空");
            return v_RetResp.setCode("903").setMessage("编辑权限项：权限项名称为空");
        }
        
        if ( Help.isNull(i_Permission.getAppKey()) )
        {
            $Logger.info("编辑权限项：所属系统编号为空");
            return v_RetResp.setCode("904").setMessage("编辑权限项：所属系统编号为空");
        }
        
        String [] v_RoleIllegalChar = this.permissionIllegalChar.getValue().split("-");
        if ( StringHelp.isContains(i_Permission.getPermissionName() ,v_RoleIllegalChar) )
        {
            $Logger.info("创建权限项：权限项名称禁止非法字符" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID() + "：" + i_Permission.getPermissionName());
            return v_RetResp.setCode("905").setMessage("创建权限项：权限项名称禁止非法字符");
        }
        
        if ( !Help.isNull(i_Permission.getPermissionType()) )
        {
            if ( StringHelp.isContains(i_Permission.getPermissionType() ,v_RoleIllegalChar) )
            {
                $Logger.info("创建权限项：权限项类型禁止非法字符" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID() + "：" + i_Permission.getPermissionName());
                return v_RetResp.setCode("905").setMessage("创建权限项：权限项类型禁止非法字符");
            }
        }
        
        if ( !Help.isNull(i_Permission.getPermissionLevel()) )
        {
            if ( StringHelp.isContains(i_Permission.getPermissionLevel() ,v_RoleIllegalChar) )
            {
                $Logger.info("创建权限项：权限项级别禁止非法字符" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID() + "：" + i_Permission.getPermissionName());
                return v_RetResp.setCode("905").setMessage("创建权限项：权限项级别禁止非法字符");
            }
        }
        
        if ( !Help.isNull(i_Permission.getRemarks()) )
        {
            if ( StringHelp.isContains(i_Permission.getRemarks() ,v_RoleIllegalChar) )
            {
                $Logger.info("创建权限项：权限项说明禁止非法字符" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID() + "：" + i_Permission.getPermissionName());
                return v_RetResp.setCode("905").setMessage("创建权限项：权限项说明禁止非法字符");
            }
        }
        
        
        if ( Help.isNull(i_Permission.getUpdaterID()) )
        {
            $Logger.info("编辑权限项：编辑者编号为空或不存在" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID() + "：" + i_Permission.getPermissionName());
            return v_RetResp.setCode("906").setMessage("编辑权限项：编辑者编号为空或不存在");
        }
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            if ( !i_Permission.getAppKey().equals(v_User.getAppKey()) )
            {
                $Logger.info("编辑权限项：编辑者不能跨系统操作" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID() + "：" + i_Permission.getPermissionName());
                return v_RetResp.setCode("907").setMessage("编辑权限项：编辑者不能跨系统操作");
            }
        }
        else
        {
            UserInfo v_Updater = this.userInfoService.queryUserGID(i_Permission.getAppKey() ,i_Permission.getUpdaterID());
            if ( v_Updater == null )
            {
                $Logger.info("编辑权限项：编辑者编号为空或不存在" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID() + "：" + i_Permission.getPermissionName());
                return v_RetResp.setCode("911").setMessage("编辑权限项：编辑者编号为空或不存在");
            }
            
            if ( !i_Permission.getAppKey().equals(v_Updater.getAppKey()) )
            {
                $Logger.info("编辑权限项：编辑者不能跨系统操作" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID() + "：" + i_Permission.getPermissionName());
                return v_RetResp.setCode("912").setMessage("编辑权限项：编辑者不能跨系统操作");
            }
        }
        
        
        boolean v_EditRet = this.permissionService.updatePermission(i_Permission);
        if ( !v_EditRet )
        {
            $Logger.info("编辑权限项异常：" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID() + "：" + i_Permission.getPermissionName());
            return v_RetResp.setCode("913").setMessage("编辑权限项失败");
        }
        else
        {
            $Logger.info("编辑权限项成功" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID() + "：" + i_Permission.getPermissionName());
            return v_RetResp;
        }
    }
    
    
    
    /**
     * 删除权限项(应用级)。
     * 
     * 注意：不能删除系统级的权限项
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-23
     * @version     v1.0
     * 
     * @param i_Token       认证票据号
     * @param i_Permission  权限项信息
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="delPermission" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<String> delPermission(@RequestParam(value="token" ,required=false) String i_Token
                                             ,@RequestBody Permission i_Permission
                                             ,HttpServletRequest      i_Request
                                             ,HttpServletResponse     i_Response)
    {
        BaseResponse<String> v_RetResp = new BaseResponse<String>();
        
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
            
            if ( !v_User.getAppKey().equals(i_Permission.getAppKey()) )
            {
                return v_RetResp.setCode("-901").setMessage("无权访问");
            }
            
            // 当验证用户登录会话时，谁登录创建者即是谁
            i_Permission.setUpdaterID(v_User.getId());
        }
        
        if ( i_Permission == null || Help.isNull(i_Permission.getPermissionID()) )
        {
            $Logger.info("删除权限项：权限项ID为空");
            return v_RetResp.setCode("902").setMessage("删除权限项：权限项ID为空");
        }
        
        if ( Help.isNull(i_Permission.getAppKey()) )
        {
            $Logger.info("删除权限项：所属系统编号为空");
            return v_RetResp.setCode("903").setMessage("删除权限项：所属系统编号为空");
        }
        
        if ( Help.isNull(i_Permission.getUpdaterID()) )
        {
            $Logger.info("删除权限项：编辑者编号为空或不存在" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID());
            return v_RetResp.setCode("904").setMessage("删除权限项：编辑者编号为空或不存在");
        }
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            if ( !i_Permission.getAppKey().equals(v_User.getAppKey()) )
            {
                $Logger.info("删除权限项：编辑者不能跨系统操作" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID());
                return v_RetResp.setCode("905").setMessage("删除权限项：编辑者不能跨系统操作");
            }
        }
        else
        {
            UserInfo v_Updater = this.userInfoService.queryUserGID(i_Permission.getAppKey() ,i_Permission.getUpdaterID());
            if ( v_Updater == null )
            {
                $Logger.info("删除权限项：编辑者编号为空或不存在" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID());
                return v_RetResp.setCode("911").setMessage("删除权限项：编辑者编号为空或不存在");
            }
            
            if ( !i_Permission.getAppKey().equals(v_Updater.getAppKey()) )
            {
                $Logger.info("删除权限项：编辑者不能跨系统操作" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID());
                return v_RetResp.setCode("912").setMessage("删除权限项：编辑者不能跨系统操作");
            }
        }
        
        
        boolean v_DelRet = this.permissionService.delPermission(i_Permission);
        if ( !v_DelRet )
        {
            $Logger.info("删除权限项失败：" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID());
            return v_RetResp.setCode("913").setMessage("删除权限项失败");
        }
        else
        {
            $Logger.info("删除权限项成功" + i_Permission.getAppKey() + "：" + i_Permission.getPermissionID());
            return v_RetResp;
        }
    }
    
    
    
    /**
     * 查询权限项接口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-23
     * @version     v1.0
     * 
     * @param i_Token       认证票据号
     * @param i_Permission  权限项信息
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="list" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<List<Permission>> query(@RequestParam(value="token" ,required=false) String i_Token
                                               ,@RequestBody Permission i_Permission
                                               ,HttpServletRequest      i_Request
                                               ,HttpServletResponse     i_Response)
    {
        BaseResponse<List<Permission>> v_RetResp = new BaseResponse<List<Permission>>();
        
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
            
            if ( !v_User.getAppKey().equals(i_Permission.getAppKey()) )
            {
                return v_RetResp.setCode("-901").setMessage("无权访问");
            }
        }
        
        if ( i_Permission == null )
        {
            $Logger.info("查询权限项：权限项为空");
            return v_RetResp.setCode("902").setMessage("查询权限项：权限项为空");
        }
        
        if ( Help.isNull(i_Permission.getAppKey()) )
        {
            $Logger.info("查询权限项：所属系统编号为空");
            return v_RetResp.setCode("903").setMessage("查询权限项：所属系统编号为空");
        }
        
        List<Permission> v_Permissions = this.permissionService.query(i_Permission);
        if ( Help.isNull(v_Permissions) )
        {
            $Logger.info("查询权限项失败：" + i_Permission.getAppKey());
            return v_RetResp.setCode("911").setMessage("查询权限项失败");
        }
        else
        {
            $Logger.info("查询权限项成功" + i_Permission.getAppKey());
            return v_RetResp;
        }
    }
    
}
