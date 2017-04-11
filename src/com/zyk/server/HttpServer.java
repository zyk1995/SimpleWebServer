package com.zyk.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class HttpServer {
	public static final String WEB_ROOT = System.getProperty("user.dir")+ File.separator + "webroot";
	private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
	private boolean shutdown = false;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpServer server = new HttpServer();
		server.start();
	}
	//启动服务器，并接收用户请求进行处理
	public void start(){
		System.out.println("Simple Server start up!");
		ServerSocket serverSocket = null;
		int PORT = 8080;
		
		try{
			serverSocket = new ServerSocket(PORT, 1, InetAddress.getByName("127.0.0.1"));
			
		} catch(UnknownHostException e){
			e.printStackTrace();
			System.exit(-1);
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		//若请求的命令不为SHUTDOWN时，循环处理请求
		while(!shutdown){
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			
			try{
				//创建socket进行请求处理
				socket = serverSocket.accept();
				input = socket.getInputStream();
				output = socket.getOutputStream();
				
				//接收请求
				Request request = new Request(input);
				request.parser();
				
				//处理请求并返回结果
				Response response = new Response(output);
				response.setRequest(request);
				response.sendStaticResource();
				
				//关闭socket
				socket.close();
				//若请求命令为关闭，则关闭服务器
				System.out.println("request.uri---------=" +request.getUri());
				shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
			
			} catch (IOException e){
				e.printStackTrace();
				continue;
			}
		}
	}
}
