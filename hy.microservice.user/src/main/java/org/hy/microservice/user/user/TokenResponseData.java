package org.hy.microservice.user.user;





/**
 * 访问Token的数据结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-05-06
 * @version     v1.0
 */
public class TokenResponseData
{
    
    /** 票据信息 */
    private TokenInfo data;
    
    
    
    public TokenInfo getData()
    {
        return data;
    }
    
    
    public void setData(TokenInfo data)
    {
        this.data = data;
    }
    
}
