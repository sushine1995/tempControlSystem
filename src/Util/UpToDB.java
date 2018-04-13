package Util;

import java.sql.ResultSet;

import com.mysql.fabric.xmlrpc.base.Array;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.sun.org.apache.bcel.internal.util.ByteSequence;

import servlet.DBUtil;
import Basic.UserDao; 

public class UpToDB {
	static boolean UpOk = false;
	
	public static boolean upTemp(byte[] data,int dataLen) {
		
		byte[] bDeviceId = new byte[UserDao.lenOfDeviceId];	
		byte[] temperature = new byte[UserDao.lenOfTemp];
		String sDeviceId = new String();
		
		System.arraycopy(data,UserDao.posOfDeviceId,bDeviceId,0,UserDao.lenOfDeviceId);
		System.arraycopy(data,UserDao.posOfTemp,temperature,0,UserDao.lenOfTemp);
		sDeviceId = BytesToString.byteArrayToHexStr(bDeviceId);
		//System.arraycopy(data,0,deviceId,0,2);
		//System.arraycopy(data,2,temperature,0,1);
		try {
			Connection connect = DBUtil.getConnect();
			Statement statement = (Statement) connect.createStatement();
			ResultSet result;
			
			String sqlInsertTemp2 = "insert into "  + DBUtil.TABLE_TEMPERATURE+ "(deviceId,temp) values(?,?)";
			PreparedStatement statement2 = (PreparedStatement) connect.prepareStatement(sqlInsertTemp2);
			//statement2.setBytes(1, deviceId);
			statement2.setString(1, sDeviceId);
			statement2.setBytes(2, temperature);
			int row2= statement2.executeUpdate();
			String sqlInsertTemp = "insert into "  + DBUtil.TABLE_TEMPERATURE+ "(deviceId,temp) values('"+data[0]+data[1]+"','"+data[2]+"')";
			//String sqlInsertPass = "insert into " + DBUtil.TABLE_PASSWORD + "(userAccount,userPassword) values('"+account+"','"+password+"')";
			System.out.println("SQL:"+sqlInsertTemp);
			
			//int row2 = statement.executeUpdate(sqlInsertTemp);
			if(row2!=0) {
				System.out.println("温度数据插入成功，设备号："+ sDeviceId + "   ，温度值(十六进制)：" + BytesToString.byteArrayToHexStr(temperature));
				//System.out.println("温度数据插入成功，设备号："+ sDeviceId + "温度值()：" + HexToString.hexStringToString(temperature));
			}
			else System.out.println("温度数据插入失败，设备号："+ sDeviceId + "   ，温度值(十六进制)：" + BytesToString.byteArrayToHexStr(temperature));
			statement.close();
			connect.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return UpOk;
		
		
		
	}
}
