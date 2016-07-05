package com.kobe.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import com.kobe.Until.CloseUtil;

/**
 * 创建服务器，并启动
 * 1.请求
 * 2.相应
 * @author ko
 *
 */
public class Server {
	private ServerSocket server;
	private static final String CRLF="\r\n";
	private static final String BLANK=" ";
	private boolean isShutDown = false;
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
		
	}
	/**
	 * 指定端口的启动方法
	 */
	public void start(){
		start(8888);
	}
	
	/**
	 * 启动方法
	 */
	public void start(int port){
		
		try {
			server = new ServerSocket(port);		
			this.receive();
		} catch (IOException e) {
			
			//e.printStackTrace();
			stop();
		}
		
	}
	/**
	 * 接收客户端
	 */
	private void receive(){
		try {
				while (!isShutDown) {
					new Thread(new Dispatcher(server.accept())).start();
				}
			
		} catch (IOException e) {
			stop();
		}
	}
	
	/**
	 * 停止服务器
	 */
	public void stop(){
		isShutDown = true;
		CloseUtil.closeAll();
	}
	
	
	

}
