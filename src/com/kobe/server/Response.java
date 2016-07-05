package com.kobe.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

import com.kobe.Until.CloseUtil;

/**
 * 封装响应消息
 * @author ko
 *
 */
public class Response {
	//；两个常量
	private static final String CRLF="\r\n";
	private static final String BLANK=" ";
	//流
	private BufferedWriter bw;
	//正文
	private StringBuilder content;
	
	//存储头信息
	private StringBuilder headinfo;
	//存储正文长度
	private int len = 0;
	
	public Response(){
		headinfo = new StringBuilder();
		content = new StringBuilder();
		len = 0;
	}
	
	public Response(Socket client){
		this();
		try {
			bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			headinfo = null;
		}
	}
	
	public Response(OutputStream os){
		this();
		bw = new BufferedWriter(new OutputStreamWriter(os));
	}
	
	
	/**
	 * 构建正文
	 * @return 
	 */
	
	public Response print(String info){
		content.append(info);
		len+=info.getBytes().length;
		return this;
	}
	/**
	 * 正文加回车
	 * @return 
	 */
	
	public Response println(String info){
		content.append(info).append(CRLF);
		len+=(info+CRLF).getBytes().length;
		return this;
	}
	
	/**
	 * 构建响应头
	 */
	private void createHeadInfo(int code){
		//1)HTTP协议版本，状态码，描述
		headinfo.append("HTTP:/1.1").append(BLANK).append(code).append(BLANK);
		switch (code) {
		case 200:
			headinfo.append("OK");
			break;
		case 404:
			headinfo.append("NOT FOUND");
			break;
		case 505:
			headinfo.append("SEVER ERROR");
			break;
		}
		headinfo.append(CRLF);
		//2)响应头（Response Head）
		headinfo.append("Server:kobe Server/0.0.1").append(CRLF);
		headinfo.append("Date:").append(new Date()).append(CRLF);
		headinfo.append("Content-type:text/html;charset=GBK").append(CRLF);
		//正文长度：字节长度
		headinfo.append("Content-Length:").append(len).append(CRLF);
		headinfo.append(CRLF);//分隔符
	}
	//推送到客户端
	void pushToClient(int code) throws IOException{
		if (null==headinfo) {
			code=500;
		}
		createHeadInfo(code);
		//头信息+分隔符
		bw.append(headinfo.toString());
		//正文
		bw.append(content.toString());
		bw.flush();
	}
	
	public void close(){
		CloseUtil.closeAll(bw);
	}
	
}
