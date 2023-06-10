package org.hy.microservice.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.user.UserSSO;
import org.hy.microservice.common.user.UserService;
import org.hy.microservice.user.role.RoleInfo;
import org.hy.microservice.user.role.RoleInfoService;
import org.hy.microservice.user.role.RoleRelation;
import org.hy.microservice.user.role.RoleRelationService;
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
    @Qualifier("MS_User_IsCheckToken")
    private Param isCheckToken;
    
    @Autowired
    @Qualifier("MS_User_CheckIllegalChar")
    private Param roleIllegalChar;
    
    @Autowired
    @Qualifier("UserService")
    private UserService userService;
    
    @Autowired
    @Qualifier("UserInfoService")
    private UserInfoService userInfoService;
    
    @Autowired
    @Qualifier("RoleInfoService")
    private RoleInfoService roleService;
    
    @Autowired
    @Qualifier("RoleRelationService")
    private RoleRelationService roleRelationService;
    
    
    
    /**
     * 添加角色接口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-19
     * @version     v1.0
     * 
     * @param i_Token        认证票据号
     * @param i_CreateRole   创建的角色信息
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="addRole" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<RoleInfo> addRole(@RequestParam(value="token" ,required=false) String i_Token
                                         ,@RequestBody RoleInfo i_CreateRole
                                         ,HttpServletRequest    i_Request
                                         ,HttpServletResponse   i_Response)
    {
        BaseResponse<RoleInfo> v_RetResp = new BaseResponse<RoleInfo>();
        
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
            
            if ( !v_User.getAppKey().equals(i_CreateRole.getAppKey()) )
            {
                return v_RetResp.setCode("-901").setMessage("无权访问");
            }
            
            // 当验证用户登录会话时，谁登录创建者即是谁
            i_CreateRole.setCreateUserID(v_User.getId());
        }
        
        if ( i_CreateRole == null || Help.isNull(i_CreateRole.getRoleName()) )
        {
            $Logger.info("创建角色：角色名称为空");
            return v_RetResp.setCode("902").setMessage("创建角色：角色名称为空");
        }
        
        if ( Help.isNull(i_CreateRole.getAppKey()) )
        {
            $Logger.info("创建角色：所属系统编号为空");
            return v_RetResp.setCode("903").setMessage("创建角色：所属系统编号为空");
        }
        
        String [] v_RoleIllegalChar = this.roleIllegalChar.getValue().split("-");
        if ( StringHelp.isContains(i_CreateRole.getRoleName() ,v_RoleIllegalChar) )
        {
            $Logger.info("创建角色：角色名称禁止非法字符" + i_CreateRole.getAppKey() + "：" + i_CreateRole.getRoleName());
            return v_RetResp.setCode("904").setMessage("创建角色：角色名称禁止非法字符");
        }
        
        if ( !Help.isNull(i_CreateRole.getRemarks()) )
        {
            if ( StringHelp.isContains(i_CreateRole.getRemarks() ,v_RoleIllegalChar) )
            {
                $Logger.info("创建角色：角色说明禁止非法字符" + i_CreateRole.getAppKey() + "：" + i_CreateRole.getRoleName());
                return v_RetResp.setCode("905").setMessage("创建角色：角色说明禁止非法字符");
            }
        }
        
        if ( Help.isNull(i_CreateRole.getCreateUserID()) )
        {
            $Logger.info("创建角色：创建者编号为空或不存在" + i_CreateRole.getAppKey() + "：" + i_CreateRole.getRoleName());
            return v_RetResp.setCode("906").setMessage("创建角色：创建者编号为空或不存在");
        }
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            if ( !i_CreateRole.getAppKey().equals(v_User.getAppKey()) )
            {
                $Logger.info("创建角色：创建者不能跨系统操作" + i_CreateRole.getAppKey() + "：" + i_CreateRole.getRoleName());
                return v_RetResp.setCode("907").setMessage("创建角色：创建者不能跨系统操作");
            }
        }
        else
        {
            UserInfo v_Creater = this.userInfoService.queryUserGID(i_CreateRole.getAppKey() ,i_CreateRole.getCreateUserID());
            if ( v_Creater == null )
            {
                $Logger.info("创建角色：创建者编号为空或不存在" + i_CreateRole.getAppKey() + "：" + i_CreateRole.getRoleName());
                return v_RetResp.setCode("911").setMessage("创建角色：创建者编号为空或不存在");
            }
            
            if ( !i_CreateRole.getAppKey().equals(v_Creater.getAppKey()) )
            {
                $Logger.info("创建角色：创建者不能跨系统操作" + i_CreateRole.getAppKey() + "：" + i_CreateRole.getRoleName());
                return v_RetResp.setCode("912").setMessage("创建角色：创建者不能跨系统操作");
            }
        }
        
        
        RoleInfo v_Role = this.roleService.addRole(i_CreateRole);
        if ( v_Role == null )
        {
            $Logger.info("创建角色失败：" + i_CreateRole.getAppKey() + "：" + i_CreateRole.getRoleName());
            return v_RetResp.setCode("913").setMessage("创建角色失败");
        }
        else
        {
            $Logger.info("创建角色成功" + i_CreateRole.getAppKey() + "：" + i_CreateRole.getRoleName());
            return v_RetResp.setData(v_Role);
        }
    }
    
    
    
    /**
     * 修改角色接口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-19
     * @version     v1.0
     * 
     * @param i_Token     认证票据号
     * @param i_Role      角色信息
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="editRole" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<String> editRole(@RequestParam(value="token" ,required=false) String i_Token
                                        ,@RequestBody RoleInfo i_Role
                                        ,HttpServletRequest    i_Request
                                        ,HttpServletResponse   i_Response)
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
            
            if ( !v_User.getAppKey().equals(i_Role.getAppKey()) )
            {
                return v_RetResp.setCode("-901").setMessage("无权访问");
            }
            
            // 当验证用户登录会话时，谁登录创建者即是谁
            i_Role.setUpdateUserID(v_User.getId());
        }
        
        if ( i_Role == null || Help.isNull(i_Role.getRoleID()) )
        {
            $Logger.info("编辑角色：角色ID为空");
            return v_RetResp.setCode("902").setMessage("编辑角色：角色ID为空");
        }
        
        if ( Help.isNull(i_Role.getRoleName()) )
        {
            $Logger.info("编辑角色：角色名称为空");
            return v_RetResp.setCode("903").setMessage("编辑角色：角色名称为空");
        }
        
        if ( Help.isNull(i_Role.getAppKey()) )
        {
            $Logger.info("编辑角色：所属系统编号为空");
            return v_RetResp.setCode("904").setMessage("编辑角色：所属系统编号为空");
        }
        
        String [] v_RoleIllegalChar = this.roleIllegalChar.getValue().split("-");
        if ( StringHelp.isContains(i_Role.getRoleName() ,v_RoleIllegalChar) )
        {
            $Logger.info("创建角色：角色名称禁止非法字符" + i_Role.getAppKey() + "：" + i_Role.getRoleID() + "：" + i_Role.getRoleName());
            return v_RetResp.setCode("905").setMessage("编辑角色：角色名称禁止非法字符");
        }
        
        if ( !Help.isNull(i_Role.getRemarks()) )
        {
            if ( StringHelp.isContains(i_Role.getRemarks() ,v_RoleIllegalChar) )
            {
                $Logger.info("编辑角色：角色说明禁止非法字符" + i_Role.getAppKey() + "：" + i_Role.getRoleID() + "：" + i_Role.getRoleName());
                return v_RetResp.setCode("906").setMessage("编辑角色：角色说明禁止非法字符");
            }
        }
        
        if ( Help.isNull(i_Role.getUpdateUserID()) )
        {
            $Logger.info("编辑角色：编辑者编号为空或不存在" + i_Role.getAppKey() + "：" + i_Role.getRoleID() + "：" + i_Role.getRoleName());
            return v_RetResp.setCode("907").setMessage("编辑角色：编辑者编号为空或不存在");
        }
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            if ( !i_Role.getAppKey().equals(v_User.getAppKey()) )
            {
                $Logger.info("编辑角色：编辑者不能跨系统操作" + i_Role.getAppKey() + "：" + i_Role.getRoleID() + "：" + i_Role.getRoleName());
                return v_RetResp.setCode("908").setMessage("编辑角色：编辑者不能跨系统操作");
            }
        }
        else
        {
            UserInfo v_Updater = this.userInfoService.queryUserGID(i_Role.getAppKey() ,i_Role.getUpdateUserID());
            if ( v_Updater == null )
            {
                $Logger.info("编辑角色：编辑者编号为空或不存在" + i_Role.getAppKey() + "：" + i_Role.getRoleID() + "：" + i_Role.getRoleName());
                return v_RetResp.setCode("911").setMessage("编辑角色：编辑者编号为空或不存在");
            }
            
            if ( !i_Role.getAppKey().equals(v_Updater.getAppKey()) )
            {
                $Logger.info("编辑角色：编辑者不能跨系统操作" + i_Role.getAppKey() + "：" + i_Role.getRoleID() + "：" + i_Role.getRoleName());
                return v_RetResp.setCode("912").setMessage("编辑角色：编辑者不能跨系统操作");
            }
        }
        
        
        boolean v_EditRet = this.roleService.updateRole(i_Role);
        if ( !v_EditRet )
        {
            $Logger.info("编辑角色异常：" + i_Role.getAppKey() + "：" + i_Role.getRoleID() + "：" + i_Role.getRoleName());
            return v_RetResp.setCode("913").setMessage("编辑角色失败");
        }
        else
        {
            $Logger.info("编辑角色成功" + i_Role.getAppKey() + "：" + i_Role.getRoleID() + "：" + i_Role.getRoleName());
            return v_RetResp;
        }
    }
    
    
    
    /**
     * 删除角色接口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-19
     * @version     v1.0
     * 
     * @param i_Token    认证票据号
     * @param i_Role     角色信息
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="delRole" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<String> delRole(@RequestParam(value="token" ,required=false) String i_Token
                                       ,@RequestBody RoleInfo i_Role
                                       ,HttpServletRequest    i_Request
                                       ,HttpServletResponse   i_Response)
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
            
            if ( !v_User.getAppKey().equals(i_Role.getAppKey()) )
            {
                return v_RetResp.setCode("-901").setMessage("无权访问");
            }
            
            // 当验证用户登录会话时，谁登录创建者即是谁
            i_Role.setUpdateUserID(v_User.getId());
        }
        
        if ( i_Role == null || Help.isNull(i_Role.getRoleID()) )
        {
            $Logger.info("删除角色：角色ID为空");
            return v_RetResp.setCode("902").setMessage("删除角色：角色ID为空");
        }
        
        if ( Help.isNull(i_Role.getAppKey()) )
        {
            $Logger.info("删除角色：所属系统编号为空");
            return v_RetResp.setCode("903").setMessage("删除角色：所属系统编号为空");
        }
        
        if ( Help.isNull(i_Role.getUpdateUserID()) )
        {
            $Logger.info("删除角色：编辑者编号为空或不存在" + i_Role.getAppKey() + "：" + i_Role.getRoleID());
            return v_RetResp.setCode("904").setMessage("删除角色：编辑者编号为空或不存在");
        }
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            if ( !i_Role.getAppKey().equals(v_User.getAppKey()) )
            {
                $Logger.info("删除角色：编辑者不能跨系统操作" + i_Role.getAppKey() + "：" + i_Role.getRoleID());
                return v_RetResp.setCode("905").setMessage("删除角色：编辑者不能跨系统操作");
            }
        }
        else
        {
            UserInfo v_Updater = this.userInfoService.queryUserGID(i_Role.getAppKey() ,i_Role.getUpdateUserID());
            if ( v_Updater == null )
            {
                $Logger.info("删除角色：编辑者编号为空或不存在" + i_Role.getAppKey() + "：" + i_Role.getRoleID());
                return v_RetResp.setCode("911").setMessage("删除角色：编辑者编号为空或不存在");
            }
            
            if ( !i_Role.getAppKey().equals(v_Updater.getAppKey()) )
            {
                $Logger.info("删除角色：编辑者不能跨系统操作" + i_Role.getAppKey() + "：" + i_Role.getRoleID());
                return v_RetResp.setCode("912").setMessage("删除角色：编辑者不能跨系统操作");
            }
        }
        
        
        boolean v_DelRet = this.roleService.delRole(i_Role);
        if ( !v_DelRet )
        {
            $Logger.info("删除角色失败：" + i_Role.getAppKey() + "：" + i_Role.getRoleID());
            return v_RetResp.setCode("913").setMessage("删除角色失败");
        }
        else
        {
            $Logger.info("删除角色成功" + i_Role.getAppKey() + "：" + i_Role.getRoleID());
            return v_RetResp;
        }
    }
    
    
    
    /**
     * 查询角色接口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-19
     * @version     v1.0
     * 
     * @param i_Token    认证票据号
     * @param i_Role     角色信息
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="list" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<List<RoleInfo>> query(@RequestParam(value="token" ,required=false) String i_Token
                                             ,@RequestBody RoleInfo i_Role
                                             ,HttpServletRequest    i_Request
                                             ,HttpServletResponse   i_Response)
    {
        BaseResponse<List<RoleInfo>> v_RetResp = new BaseResponse<List<RoleInfo>>();
        
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
            
            if ( !v_User.getAppKey().equals(i_Role.getAppKey()) )
            {
                return v_RetResp.setCode("-901").setMessage("无权访问");
            }
        }
        
        if ( i_Role == null )
        {
            $Logger.info("查询角色：角色为空");
            return v_RetResp.setCode("902").setMessage("查询角色：角色为空");
        }
        
        if ( Help.isNull(i_Role.getAppKey()) )
        {
            $Logger.info("查询角色：所属系统编号为空");
            return v_RetResp.setCode("903").setMessage("查询角色：所属系统编号为空");
        }
        
        List<RoleInfo> v_Roles = this.roleService.query(i_Role);
        if ( Help.isNull(v_Roles) )
        {
            $Logger.info("查询角色失败：" + i_Role.getAppKey());
            return v_RetResp.setCode("911").setMessage("删除角色失败");
        }
        else
        {
            $Logger.info("查询角色成功" + i_Role.getAppKey());
            return v_RetResp.setData(v_Roles);
        }
    }
    
    
    
    /**
     * 授权用户角色的接口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     * 
     * @param i_Token          认证票据号
     * @param i_RoleRelation  用户与角色关系
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="grantRole" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<RoleRelation> grantRole(@RequestParam(value="token" ,required=false) String i_Token
                                                ,@RequestBody RoleRelation i_RoleRelation
                                                ,HttpServletRequest  i_Request
                                                ,HttpServletResponse i_Response)
    {
        BaseResponse<RoleRelation> v_RetResp = new BaseResponse<RoleRelation>();
        
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
            
            if ( !v_User.getAppKey().equals(i_RoleRelation.getAppKey()) )
            {
                return v_RetResp.setCode("-901").setMessage("无权访问");
            }
            
            // 当验证用户登录会话时，谁登录创建者即是谁
            i_RoleRelation.setCreateUserID(v_User.getId());
        }
        
        if ( i_RoleRelation == null || Help.isNull(i_RoleRelation.getRoleID()) )
        {
            $Logger.info("授权用户角色：角色ID为空");
            return v_RetResp.setCode("902").setMessage("授权用户角色：角色ID为空");
        }
        
        if ( Help.isNull(i_RoleRelation.getAppKey()) )
        {
            $Logger.info("授权用户角色：所属系统编号为空");
            return v_RetResp.setCode("903").setMessage("授权用户角色：所属系统编号为空");
        }
        
        
        if ( Help.isNull(i_RoleRelation.getUserGID()) )
        {
            $Logger.info("授权用户角色：赋权用户编号为空");
            return v_RetResp.setCode("904").setMessage("授权用户角色：赋权用户编号为空");
        }
        
        String [] v_RoleIllegalChar = this.roleIllegalChar.getValue().split("-");
        if ( !Help.isNull(i_RoleRelation.getRemarks()) )
        {
            if ( StringHelp.isContains(i_RoleRelation.getRemarks() ,v_RoleIllegalChar) )
            {
                $Logger.info("授权用户角色：说明禁止非法字符" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID() + "：" + i_RoleRelation.getRemarks());
                return v_RetResp.setCode("905").setMessage("授权用户角色：说明禁止非法字符");
            }
        }
        
        
        if ( Help.isNull(i_RoleRelation.getCreateUserID()) )
        {
            $Logger.info("授权用户角色：编辑者编号为空或不存在" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setCode("906").setMessage("授权用户角色：编辑者编号为空或不存在");
        }
        
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            if ( !i_RoleRelation.getAppKey().equals(v_User.getAppKey()) )
            {
                $Logger.info("授权用户角色：编辑者不能跨系统操作" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
                return v_RetResp.setCode("907").setMessage("授权用户角色：编辑者不能跨系统操作");
            }
        }
        else
        {
            UserInfo v_Creater = this.userInfoService.queryUserGID(i_RoleRelation.getAppKey() ,i_RoleRelation.getCreateUserID());
            if ( v_Creater == null )
            {
                $Logger.info("授权用户角色：编辑者编号为空或不存在" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
                return v_RetResp.setCode("911").setMessage("授权用户角色：编辑者编号为空或不存在");
            }
            
            if ( !i_RoleRelation.getAppKey().equals(v_Creater.getAppKey()) )
            {
                $Logger.info("授权用户角色：编辑者不能跨系统操作" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
                return v_RetResp.setCode("912").setMessage("授权用户角色：编辑者不能跨系统操作");
            }
        }
        
        
        UserInfo v_GrantUser = this.userInfoService.queryUserGID(i_RoleRelation.getAppKey() ,i_RoleRelation.getUserGID());
        if ( v_GrantUser == null )
        {
            $Logger.info("授权用户角色：赋权用户不存在" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setCode("913").setMessage("授权用户角色：赋权用户不存在");
        }
        if ( !i_RoleRelation.getAppKey().equals(v_GrantUser.getAppKey()) )
        {
            $Logger.info("授权用户角色：赋权用户非本系统用户" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setCode("914").setMessage("授权用户角色：赋权用户非本系统用户");
        }
        
        
        RoleInfo v_Role = new RoleInfo();
        v_Role.setRoleID(i_RoleRelation.getRoleID());
        v_Role = this.roleService.queryByID(v_Role);
        if ( v_Role == null )
        {
            $Logger.info("授权用户角色：授权角色不存在" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setCode("915").setMessage("授权用户角色：赋权用户不存在");
        }
        if ( !i_RoleRelation.getAppKey().equals(v_Role.getAppKey()) )
        {
            $Logger.info("授权用户角色：授权角色非本系统角色" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setCode("916").setMessage("授权用户角色：授权角色非本系统角色");
        }
        
        
        RoleRelation v_IsExistsObj = new RoleRelation();
        v_IsExistsObj.init(i_RoleRelation);
        v_IsExistsObj.setUrrID(null);
        v_IsExistsObj.setRemarks(null);
        List<RoleRelation> v_GrantRelations = this.roleRelationService.query(v_IsExistsObj);
        if ( !Help.isNull(v_GrantRelations) )
        {
            $Logger.info("授权用户角色：用户已有授权，请勿重复授权" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setCode("916").setMessage("授权用户角色：用户已有授权，请勿重复授权");
        }
        
        
        RoleRelation v_AddRet = this.roleRelationService.addRelation(i_RoleRelation);
        if ( v_AddRet == null )
        {
            $Logger.info("授权用户角色失败：" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setCode("918").setMessage("授权用户角色失败");
        }
        else
        {
            $Logger.info("授权用户角色成功" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setData(v_AddRet);
        }
    }
    
    
    
    /**
     * 撤销用户角色的接口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-08-22
     * @version     v1.0
     * 
     * @param i_Token          认证票据号
     * @param i_RoleRelation  用户与角色关系
     * @param i_Request
     * @param i_Response
     * @return
     */
    @RequestMapping(value="revokeRole" ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<RoleRelation> revokeRole(@RequestParam(value="token" ,required=false) String i_Token
                                                 ,@RequestBody RoleRelation i_RoleRelation
                                                 ,HttpServletRequest  i_Request
                                                 ,HttpServletResponse i_Response)
    {
        BaseResponse<RoleRelation> v_RetResp = new BaseResponse<RoleRelation>();
        
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
            
            if ( !v_User.getAppKey().equals(i_RoleRelation.getAppKey()) )
            {
                return v_RetResp.setCode("-901").setMessage("无权访问");
            }
            
            // 当验证用户登录会话时，谁登录创建者即是谁
            i_RoleRelation.setUpdateUserID(v_User.getId());
        }
        
        if ( i_RoleRelation == null || Help.isNull(i_RoleRelation.getRoleID()) )
        {
            $Logger.info("撤销用户角色：角色ID为空");
            return v_RetResp.setCode("902").setMessage("撤销用户角色：角色ID为空");
        }
        
        if ( Help.isNull(i_RoleRelation.getAppKey()) )
        {
            $Logger.info("撤销用户角色：所属系统编号为空");
            return v_RetResp.setCode("903").setMessage("撤销用户角色：所属系统编号为空");
        }
        
        
        if ( Help.isNull(i_RoleRelation.getUserGID()) )
        {
            $Logger.info("撤销用户角色：撤权用户编号为空");
            return v_RetResp.setCode("904").setMessage("撤销用户角色：撤权用户编号为空");
        }
        
        
        if ( Help.isNull(i_RoleRelation.getUpdateUserID()) )
        {
            $Logger.info("撤销用户角色：编辑者编号为空或不存在" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setCode("905").setMessage("撤销用户角色：编辑者编号为空或不存在");
        }
        
        if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
        {
            if ( !i_RoleRelation.getAppKey().equals(v_User.getAppKey()) )
            {
                $Logger.info("撤销用户角色：编辑者不能跨系统操作" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
                return v_RetResp.setCode("906").setMessage("撤销用户角色：编辑者不能跨系统操作");
            }
        }
        else
        {
            UserInfo v_Updater = this.userInfoService.queryUserGID(i_RoleRelation.getAppKey() ,i_RoleRelation.getUpdateUserID());
            if ( v_Updater == null )
            {
                $Logger.info("撤销用户角色：编辑者编号为空或不存在" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
                return v_RetResp.setCode("911").setMessage("撤销用户角色：编辑者编号为空或不存在");
            }
            
            if ( !i_RoleRelation.getAppKey().equals(v_Updater.getAppKey()) )
            {
                $Logger.info("撤销用户角色：编辑者不能跨系统操作" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
                return v_RetResp.setCode("912").setMessage("撤销用户角色：编辑者不能跨系统操作");
            }
        }
        
        
        UserInfo v_GrantUser = this.userInfoService.queryUserGID(i_RoleRelation.getAppKey() ,i_RoleRelation.getUserGID());
        if ( v_GrantUser == null )
        {
            $Logger.info("撤销用户角色：赋权用户不存在" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setCode("913").setMessage("撤销用户角色：赋权用户不存在");
        }
        if ( !i_RoleRelation.getAppKey().equals(v_GrantUser.getAppKey()) )
        {
            $Logger.info("撤销用户角色：赋权用户非本系统用户" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setCode("914").setMessage("撤销用户角色：赋权用户非本系统用户");
        }
        
        
        RoleInfo v_Role = new RoleInfo();
        v_Role.setRoleID(i_RoleRelation.getRoleID());
        v_Role = this.roleService.queryByID(v_Role);
        if ( v_Role == null )
        {
            $Logger.info("撤销用户角色：撤销角色不存在" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setCode("915").setMessage("撤销用户角色：赋权用户不存在");
        }
        if ( !i_RoleRelation.getAppKey().equals(v_Role.getAppKey()) )
        {
            $Logger.info("撤销用户角色：撤销角色非本系统角色" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setCode("916").setMessage("撤销用户角色：撤销角色非本系统角色");
        }
        
        
        RoleRelation v_IsExistsObj = new RoleRelation();
        v_IsExistsObj.init(i_RoleRelation);
        v_IsExistsObj.setUrrID(null);
        v_IsExistsObj.setRemarks(null);
        List<RoleRelation> v_DelRelations = this.roleRelationService.query(v_IsExistsObj);
        if ( Help.isNull(v_DelRelations) )
        {
            $Logger.info("撤销用户角色：角色关系未查到" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
            return v_RetResp.setCode("918").setMessage("撤销用户角色：角色关系未查到");
        }
        
        for (RoleRelation v_Relation : v_DelRelations)
        {
            if ( !this.roleRelationService.delRelation(v_Relation) )
            {
                $Logger.info("撤销用户角色失败：" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
                return v_RetResp.setCode("919").setMessage("撤销用户角色失败");
            }
        }
        
        $Logger.info("撤销用户角色成功" + i_RoleRelation.getAppKey() + "：" + i_RoleRelation.getRoleID() + "：" + i_RoleRelation.getUserGID());
        return v_RetResp;
    }
    
}
