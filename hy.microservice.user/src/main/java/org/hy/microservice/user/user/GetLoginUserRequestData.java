package org.hy.microservice.user.user;

import org.hy.common.xml.SerializableDef;





/**
 * 获取在线登录的用户
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-02-04
 * @version     v1.0
 */
public class GetLoginUserRequestData extends SerializableDef
{
    
    private static final long serialVersionUID = -6415452953242078304L;
    
    /** 登录用户 */
    private UserSSO data;

    
    
    /**
     * 获取：登录用户
     */
    public UserSSO getData()
    {
        return data;
    }

    
    /**
     * 设置：登录用户
     * 
     * @param data
     */
    public void setData(UserSSO data)
    {
        this.data = data;
    }
    
}
