package org.hy.microservice.user.user;

import org.hy.common.xml.SerializableDef;





/**
 * 票据信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-02-05
 * @version     v1.0
 */
public class TokenInfo extends SerializableDef
{
    private static final long serialVersionUID = 2029220892157842421L;

    /** 过期时长（单位：秒） */
    private Integer expire;
    
    /** 访问Token */
    private String  accessToken;
    
    /** 临时登录Code */
    private String  code;
    
    /** 会话Token */
    private String  sessionToken;

    
    
    /**
     * 获取：会话Token
     */
    public String getSessionToken()
    {
        return sessionToken;
    }

    
    /**
     * 设置：会话Token
     * 
     * @param sessionToken
     */
    public void setSessionToken(String sessionToken)
    {
        this.sessionToken = sessionToken;
    }
    
    
    /**
     * 获取：过期时长（单位：秒）
     */
    public Integer getExpire()
    {
        return expire;
    }

    
    /**
     * 获取：访问Token
     */
    public String getAccessToken()
    {
        return accessToken;
    }

    
    /**
     * 设置：过期时长（单位：秒）
     * 
     * @param expire
     */
    public void setExpire(Integer expire)
    {
        this.expire = expire;
    }

    
    /**
     * 设置：访问Token
     * 
     * @param accessToken
     */
    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    
    /**
     * 获取：临时登录Code
     */
    public String getCode()
    {
        return code;
    }

    
    /**
     * 设置：临时登录Code
     * 
     * @param code
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
}
