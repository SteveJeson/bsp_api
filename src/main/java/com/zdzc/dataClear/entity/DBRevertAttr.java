package com.zdzc.dataClear.entity;

/**
 *  * @Description: 数据库还原相关属性
 *  * @author chengwengao
 *  * @date 2017/12/7 0007 15:56
 *  
 */
public class DBRevertAttr {
    private String hostName; //数据库主机名
    private String username; //数据库用户名
    private String passwd; //数据库密码
    private String databaseName; //数据库名

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

}
