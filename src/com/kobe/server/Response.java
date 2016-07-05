package com.kobe.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

import com.kobe.Until.CloseUtil;

/**
 * ��װ��Ӧ��Ϣ
 * @author ko
 *
 */
public class Response {
	//����������
	private static final String CRLF="\r\n";
	private static final String BLANK=" ";
	//��
	private BufferedWriter bw;
	//����
	private StringBuilder content;
	
	//�洢ͷ��Ϣ
	private StringBuilder headinfo;
	//�洢���ĳ���
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
	 * ��������
	 * @return 
	 */
	
	public Response print(String info){
		content.append(info);
		len+=info.getBytes().length;
		return this;
	}
	/**
	 * ���ļӻس�
	 * @return 
	 */
	
	public Response println(String info){
		content.append(info).append(CRLF);
		len+=(info+CRLF).getBytes().length;
		return this;
	}
	
	/**
	 * ������Ӧͷ
	 */
	private void createHeadInfo(int code){
		//1)HTTPЭ��汾��״̬�룬����
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
		//2)��Ӧͷ��Response Head��
		headinfo.append("Server:kobe Server/0.0.1").append(CRLF);
		headinfo.append("Date:").append(new Date()).append(CRLF);
		headinfo.append("Content-type:text/html;charset=GBK").append(CRLF);
		//���ĳ��ȣ��ֽڳ���
		headinfo.append("Content-Length:").append(len).append(CRLF);
		headinfo.append(CRLF);//�ָ���
	}
	//���͵��ͻ���
	void pushToClient(int code) throws IOException{
		if (null==headinfo) {
			code=500;
		}
		createHeadInfo(code);
		//ͷ��Ϣ+�ָ���
		bw.append(headinfo.toString());
		//����
		bw.append(content.toString());
		bw.flush();
	}
	
	public void close(){
		CloseUtil.closeAll(bw);
	}
	
}
