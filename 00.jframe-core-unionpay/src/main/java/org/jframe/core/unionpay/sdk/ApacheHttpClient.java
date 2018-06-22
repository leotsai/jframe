package org.jframe.core.unionpay.sdk;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 基于apache httpclient 组件实现的通信类
 * 
 * @author cm.he
 * 
 */
public class ApacheHttpClient {

	/**
	 * 目标地址
	 */
	private String url;

	/**
	 * 通信连接超时时间
	 */
	private int connectionTimeout = 15000;

	/**
	 * 通信读超时时间
	 */
	private int readTimeOut = 30000;

	/**
	 * 通信结果
	 */
	private String result;

	public ApacheHttpClient(String url, int connectionTimeout,
			int readTimeOut) {
		this.url = url;
		this.connectionTimeout = connectionTimeout;
		this.readTimeOut = readTimeOut;
	}

	public int send(Map<String, String> data, String encoding) {
		PostMethod post = new PostMethod(url);
		org.apache.commons.httpclient.HttpClient httpclient = null;
		try {
			post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset="+encoding);
			post.addRequestHeader("cache-control", "no-cache");
			post.addRequestHeader("pragma", "no-cache");
			post.addRequestHeader("connection", "keep-alive");
			if (null != data && 0 != data.size()) {
				for (Entry<String, String> en : data.entrySet()) {
					post.addParameter(en.getKey(), en.getValue());
				}
			}
			URL uRL = new URL(url);
			//测试环境配置不验证SSL证书（如果接银联生产环境需要验证SSL证书，可以注释以下两行代码）
			Protocol myhttps = new Protocol(uRL.getProtocol(), new MySSLSocketFactory(),-1 == uRL.getPort()?443:uRL.getPort());
			Protocol.registerProtocol("https", myhttps);
			
			httpclient = new org.apache.commons.httpclient.HttpClient();
			httpclient.getHostConfiguration().setHost(uRL.getHost(),uRL.getPort(), uRL.getProtocol());
			httpclient.setTimeout(readTimeOut);
			httpclient.setConnectionTimeout(connectionTimeout);
			
			int result = httpclient.executeMethod(post);
			LogUtil.writeLog("HTTP Return Status-Code:[" + result + "]");
			/**
			 * 获取返回值
			 */
			if (result == HttpStatus.SC_OK) {
				// 读取内容
				byte[] responseBody = post.getResponseBody();
				setResult(new String(responseBody, encoding));
			}
			LogUtil.writeLog("返回报文:[" + getResult() + "]");
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			post.releaseConnection();
			if (httpclient != null) {
				((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
			}
		}
	}
	
	
	public int sendGet(String encoding) {
		GetMethod get = new GetMethod(url);
		org.apache.commons.httpclient.HttpClient httpclient = null;
		try {
			get.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset="+encoding);
			get.addRequestHeader("cache-control", "no-cache");
			get.addRequestHeader("pragma", "no-cache");
			get.addRequestHeader("connection", "keep-alive");
			
			URL uRL = new URL(url);
			//测试环境配置不验证SSL证书（如果接银联生产环境需要验证SSL证书，可以注释以下两行代码）
			Protocol myhttps = new Protocol(uRL.getProtocol(), new MySSLSocketFactory(),-1 == uRL.getPort()?443:uRL.getPort());
			Protocol.registerProtocol("https", myhttps);
			
			httpclient = new org.apache.commons.httpclient.HttpClient();
			httpclient.getHostConfiguration().setHost(uRL.getHost(),uRL.getPort(), uRL.getProtocol());
			httpclient.setTimeout(readTimeOut);
			httpclient.setConnectionTimeout(connectionTimeout);

			int result = httpclient.executeMethod(get);
			LogUtil.writeLog("HTTP Return Status-Code:[" + result + "]");
			/**
			 * 获取返回值
			 */
			if (result == HttpStatus.SC_OK) {
				// 读取内容
				byte[] responseBody = get.getResponseBody();
				setResult(new String(responseBody, encoding));
			}
			LogUtil.writeLog("返回报文:[" + getResult() + "]");
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			get.releaseConnection();
			if (httpclient != null) {
				((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
			}
		}
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public static void main(String[] args) throws Exception {
		//String url ="https://gateway.95516.com/jiaofei/config/s/biz/S0_9800_0000";
		String url ="https://gateway.test.95516.com/gateway/api/backTransReq.do";
		
		String url1 = "http://localhost:8080/ACPTest/filterServer.do";
		ApacheHttpClient ac = new ApacheHttpClient(url1, 30000, 30000);
		ac.send(null, "UTF-8");
		
		//com.unionpay.acp.sdk.HttpClient hc = new com.unionpay.acp.sdk.HttpClient(url1,30000,30000);
		
		//hc.send(null, "UTF-8");
		
	}
}
