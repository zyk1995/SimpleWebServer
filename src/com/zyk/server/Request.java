package com.zyk.server;

import java.io.IOException;
import java.io.InputStream;

/** 
 *接收到的请求串的具体格式如下： 
 * GET /aaa.htm HTTP/1.1 
 * Host: 127.0.0.1:8080 
 * Connection: keep-alive 
 * User-Agent: Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11 
 * Accept: text/html,application/xhtml+xml,application/xml;q=0.9,q=0.8 
 * Accept-Encoding: gzip,deflate,sdch 
 * Accept-Language: zh-CN,zh;q=0.8 
 * Accept-Charset: GBK,utf-8;q=0.7,*;q=0.3 
 *  
 * @author zyk
 * 
*/  
public class Request {
	private InputStream input;
	private String uri;
	
	public Request(InputStream input){
		this.input = input;
	}
	
	public void parser(){
		StringBuffer request = new StringBuffer();
		byte[] buffer = new byte[2048];
		int i = 0;
		
		try{
			 i = input.read(buffer);
		} catch(IOException e){
			e.printStackTrace();
			i = -1;
		}
		
		for(int k = 0; k < i; k++){
			request.append((char)buffer[k]);
		}
		
		uri = parserUri(request.toString());
	}
	
	private String parserUri(String requestData){
		System.out.println("requestData-----------="+requestData);
        int index1, index2;  
        index1 = requestData.indexOf(' '); 
        System.out.println("index1="+index1);
        if(index1 != -1) {  
            index2 = requestData.indexOf(' ', index1 + 1);  
            System.out.println("index2="+index2);
            if(index2 > index1) {  
                return requestData.substring(index1 + 1, index2);  
            }  
        }  
          
        return null;
	}
	
	public String getUri() {
		return uri;
	}
}
