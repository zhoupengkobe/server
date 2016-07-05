package com.kobe.servlet;

import com.kobe.server.Request;
import com.kobe.server.Response;

/**
 * �����һ������
 * @author ko
 *
 */
public abstract class Servlet {
	
	public void service(Request req,Response rep) throws Exception{
		this.doGet(req,rep);
		this.doPost(req,rep);
		
	}
	
	public abstract void doGet(Request req,Response rep) throws Exception;
	public abstract void doPost(Request req,Response rep) throws Exception;

}
