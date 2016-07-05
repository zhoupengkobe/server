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
 * ������������������
 * 1.����
 * 2.��Ӧ
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
	 * ָ���˿ڵ���������
	 */
	public void start(){
		start(8888);
	}
	
	/**
	 * ��������
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
	 * ���տͻ���
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
	 * ֹͣ������
	 */
	public void stop(){
		isShutDown = true;
		CloseUtil.closeAll();
	}
	
	
	

}
