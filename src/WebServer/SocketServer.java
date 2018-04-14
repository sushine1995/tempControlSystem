package WebServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.org.apache.bcel.internal.generic.NEW;

import Util.HexToString;
import Util.UpToDB;
import servlet.DBUtil;
public class SocketServer {
	public static void main(String[] args) throws Exception {
		//java.util.Date day = new java.util.Date();

		//创建线程池
		ExecutorService pool = Executors.newFixedThreadPool(3);
		//pool.submit(new MyRunnable())
		int port = 220;
		ServerSocket serverSocket = new ServerSocket(port);
/*		System.out.println("server将一直等待连接");
		Socket socket = serverSocket.accept();
		try {
			getMessage(socket);
		}catch (Exception e) {
			// TODO: handle exception
		}*/
		//getMessage(socket);
		try {
			while(true) {
				Socket socket = serverSocket.accept();
				pool.submit(new MyRunnable(socket));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		serverSocket.close();
		/*InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();
		byte[] bytes = new byte[64];
		int byteToInt = 0;
		int len;
		StringBuffer buf = new StringBuffer();
		
		while ((len = inputStream.read(bytes)) != -1) {
			//stringBuffer.append(new String(bytes, 0, len));
            for (int i = 0; i < len; i++) {
                //如果直接使用bytes，当高位为1时，会通过下面的if条件，导致显示错误
            	bytes[i] = (byte) (bytes[i] & 0XFF);
            	byteToInt = bytes[i];
            	
            	//该判断条件用于修正只有一位数字的显示，自动补充0    
                if(byteToInt<16)
                    buf.append(0);
                buf.append( Integer.toHexString(bytes[i]));
                //buf.append( (bytes[i]&0XFF));
                buf.append(' ');
            }
			System.out.println("get message from client: " + buf);					
			outputStream.write(buf.toString().getBytes("UTF-8"));
			//stringBuffer.delete(0, len);
		    buf.delete(0, buf.length());
		}
		inputStream.close();
		outputStream.close();
		socket.close();
		serverSocket.close();*/
	}
	public static void getMessage(Socket socket) throws Exception, Exception {
		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();
		byte[] bytes = new byte[64];
		//int byteToInt = 0;
		int len;
		StringBuffer buf = new StringBuffer();
		
		while ((len = inputStream.read(bytes)) != -1) {
			
            for (int i = 0; i < len; i++) {
                //如果直接使用bytes，当高位为1时，会通过下面的if条件，导致显示错误
            	//byteToInt = 0;
            	bytes[i] = (byte) (bytes[i] & 0XFF);
            	//System.out.println(Integer.toHexString(bytes[i] & 0XFF));
            	System.out.println((bytes[i] & 0XFF));
            }
            UpToDB.upTemp(bytes,len);
            
            for (int i = 0; i < len; i++) {
            	if(bytes[i]<16 &&bytes[i]>=0)
                    buf.append(0);
                buf.append( Integer.toHexString(bytes[i] & 0XFF));
                //buf.append(' ');
            }
    		SimpleDateFormat df= new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    		System.out.println(df.format(new java.util.Date())+" From Port: " + socket.getPort()+ ",Message : " + buf);
			//System.out.println("From Port: " + socket.getPort()+ ",Message : " + buf);					
			outputStream.write(buf.toString().getBytes("UTF-8"));
			//stringBuffer.delete(0, len);
		    buf.delete(0, buf.length());
		}
		inputStream.close();
		outputStream.close();
		socket.close();
		
	}
}
class MyRunnable implements Runnable{
	private Socket socket = null;
	public MyRunnable(Socket socket) {
		// TODO Auto-generated constructor stub
		super();
		this.socket = socket;
	}
	@Override
		public void run() {
		try {
			SocketServer.getMessage(socket);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
