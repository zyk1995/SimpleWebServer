package com.zyk.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {
	
	private OutputStream output;
	private Request request;
	
	private static final int BUFFER_SIZE = 1024;
	
	public Response(OutputStream output){
		this.output = output;
	}
	
	public void setRequest(Request request){
		this.request = request;
	}
	
	//发送一个静态资源给客户端 ，若本地服务器有对应的文件则返回，否则返回404页面
	public void sendStaticResource(){
		byte[] buffer = new byte[BUFFER_SIZE];
		int ch;
		FileInputStream fis = null;
		try{
			System.out.println("webroot=" + HttpServer.WEB_ROOT);
			File file = new File(HttpServer.WEB_ROOT, request.getUri().substring(1));
			System.out.println("file="+file.getAbsolutePath());
			if(file.exists()){
				fis = new FileInputStream(file);
				ch = fis.read(buffer);
				while(ch != -1){
					output.write(buffer, 0, ch);
					ch = fis.read(buffer, 0, BUFFER_SIZE);
				}
			} else{
				String errorMessage = "HTTP/1.1 404 File Not Found \r\n"+
							"Content-Type: text/html\r\n" +
							"Content-Length: 24\r\n" +
							"\r\n" +
							"<h1>File Not Found!</h1>";
				output.write(errorMessage.getBytes());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(fis != null){
				try {
					fis.close();
				} catch(IOException e){
					e.printStackTrace();
				}
		}
		}
	}
}
