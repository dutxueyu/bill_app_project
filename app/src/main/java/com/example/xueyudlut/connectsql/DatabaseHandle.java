package com.example.xueyudlut.connectsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xueyudlut on 2017/7/21.
 */

public class DatabaseHandle {
    private static DatabaseHandle databaseHandle = new DatabaseHandle();
    public  static   DatabaseHandle getDatabaseHandle(){
        return  databaseHandle;
    }
    //jtds驱动路径
    private  String drive = "net.sourceforge.jtds.jdbc.Driver";
    // SQL连接字符串，格式是 jdbc:jtds:sqlserver://服务器IP:端口号/数据库名称
    // 端口号默认为1433，如果不是，可以打开SQL Server配置管理器设定，
    // 如果你的SQL Server不是默认实例，需添加连接字符串为
    private  String connStr = "jdbc:jtds:sqlserver://202.199.161.253:50042;instance = SQLEXPRESS;DatabaseName =bill_data;charset=utf8";
    private  String uid = "sa";
    private  String pwd = "da2017lian?";
    private Connection con;
    private PreparedStatement pstm = null;


    //构造函数
    public DatabaseHandle(){

    }
    public  int excuteSQL(String sql){
        int count = 0;
        try {
            //加载驱动
            Class.forName(drive);
            //建立数据库连接
            con = DriverManager.getConnection(connStr,uid,pwd);
            pstm = con.prepareStatement(sql);
            count = pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            count = -1;
        }finally {
            try {
                if(pstm!=null)
                    pstm.close();
                if(con!=null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return  count;
    }
    public List<String> SelectSQLname(String sql){
        try {
            //加载驱动
            Class.forName(drive);
            //建立数据库连接
            con = DriverManager.getConnection(connStr,uid,pwd);
            pstm = con.prepareStatement(sql);
            ResultSet rs  =pstm.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            List<String> list = new ArrayList<String>();
            int count = metaData.getColumnCount();
            while(rs.next()){
                for (int i=0;i<count;i++){
                    list.add(rs.getString(i+1));
                }

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if(pstm!=null)
                    pstm.close();
                if(con!=null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean SelectSQL(String sql){
        try {
            //加载驱动
            Class.forName(drive);
            //建立数据库连接
            con = DriverManager.getConnection(connStr,uid,pwd);
            pstm = con.prepareStatement(sql);
            ResultSet rs  =pstm.executeQuery();
       //     ResultSetMetaData metaData = rs.getMetaData();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
                if(pstm!=null)
                    pstm.close();
                if(con!=null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public int[] LoginSQL(String sql){
        try {
            //加载驱动
            Class.forName(drive);
            //建立数据库连接
            con = DriverManager.getConnection(connStr,uid,pwd);
            pstm = con.prepareStatement(sql);
            ResultSet rs  =pstm.executeQuery();

            //     ResultSetMetaData metaData = rs.getMetaData();
             if(rs.next()){
                 int []result = {rs.getInt(3),rs.getInt(1)};
                 return result;
             }else{
                 int []result ={0,-1};
                 return  result;
             }
        } catch (Exception e) {
            e.printStackTrace();
            int [] result = {-1,-1};
            return result;
        }finally {
            try {
                if(pstm!=null)
                    pstm.close();
                if(con!=null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
