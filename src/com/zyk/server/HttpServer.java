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
	//�������������������û�������д���
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
		//����������ΪSHUTDOWNʱ��ѭ����������
		while(!shutdown){
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			
			try{
				//����socket����������
				socket = serverSocket.accept();
				input = socket.getInputStream();
				output = socket.getOutputStream();
				
				//��������
				Request request = new Request(input);
				request.parser();
				
				//�������󲢷��ؽ��
				Response response = new Response(output);
				response.setRequest(request);
				response.sendStaticResource();
				
				//�ر�socket
				socket.close();
				//����������Ϊ�رգ���رշ�����
				System.out.println("request.uri---------=" +request.getUri());
				shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
			
			} catch (IOException e){
				e.printStackTrace();
				continue;
			}
		}
	}
}
