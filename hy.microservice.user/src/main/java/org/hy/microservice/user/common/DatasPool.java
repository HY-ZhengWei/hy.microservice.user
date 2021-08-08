package org.hy.microservice.user.common;

import java.util.List;

import org.hy.common.Help;
import org.hy.common.net.ClientSocket;
import org.hy.common.net.ClientSocketCluster;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;





/**
 * 内存数据池的操作类（今后可为对接Redis提供便利条件）
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-08-08
 * @version     v1.0
 */
@Xjava
public class DatasPool
{
    
    private List<ClientSocket> servers;
    
    
    
    /**
     * 同步数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-05-18
     * @version     v1.0
     *
     * @return
     */
    public void put(String i_DataID ,Object i_Data ,int i_DataExpireTime)
    {
        XJava.putObject(i_DataID ,i_Data ,i_DataExpireTime);
        
        if ( !Help.isNull(servers) )
        {
            ClientSocketCluster.sendObjects(servers ,this.getClusterTimeout() ,i_DataID ,i_Data ,i_DataExpireTime ,true);
        }
    }
    
    
    
    /**
     * 同步数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-05-18
     * @version     v1.0
     *
     * @return
     */
    public void remove(String i_DataID)
    {
        if ( !Help.isNull(servers) )
        {
            ClientSocketCluster.removeObjects(servers ,this.getClusterTimeout() ,i_DataID ,true);
        }
    }
    
    
    
    /**
     * 获取数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-05-18
     * @version     v1.0
     *
     * @return
     */
    public Object get(String i_DataID)
    {
        return XJava.getObject(i_DataID);
    }
    
    
    
    /**
     * 获取数据后，立即删除数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-05-24
     * @version     v1.0
     *
     * @return
     */
    public Object getDel(String i_DataID)
    {
        Object v_Ret = XJava.getObject(i_DataID);
        
        XJava.remove(i_DataID);
        
        return v_Ret;
    }
    
    
    
    /**
     * 集群并发通讯的超时时长(单位：毫秒)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2017-01-23
     * @version     v1.0
     *
     * @return
     */
    private long getClusterTimeout()
    {
        return Long.parseLong(XJava.getParam("ClusterTimeout").getValue());
    }
    
}
