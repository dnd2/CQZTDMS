package com.infodms.dms.actions.crmphone.push;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

public class SendPushServicev2  {
	
//	private static SendPushServicev2 instance = null;
//	/**
//	 * 私有构造初始化一次
//	 * */
//	private SendPushServicev2(){
//		
//	}
//	
//	public static SendPushServicev2 getInstance() {
//		if (instance == null) {
//			instance = new SendPushServicev2();
//		}
//		return instance;
//	}
	
	private static String appKey ="4b1b4dec0a59cbdeb511191a";	//必填，efb3861086bd48a43d8ed0c9例如466f7032ac604e02fb7bda89

	private static String masterSecret = "18aebf5ca01d5d734c0e38e4";//"13ac09b17715bd117163d8a1";//必填，45b4939ad55ed96413c29a80每个应用都对应一个masterSecret
	
	private static long timeToLive =  60 * 60 * 24;  

	private static JPushClient jpush = null;
	
	public MessageResult sendTag(String tag,String msgTitle,String msgContent,Map<String, Object> extra) {
		int sendNo = getRandomSendNo();
		jpush = new JPushClient(masterSecret, appKey, timeToLive);
		MessageResult msgResult = jpush.sendNotificationWithTag(sendNo, tag, msgTitle, msgContent, 0, extra);
		return msgResult;
	}
	
	public MessageResult sendAlias(String alias,String msgTitle,String msgContent,Map<String, Object> extra) {
		int sendNo = getRandomSendNo();
		jpush = new JPushClient(masterSecret, appKey, timeToLive);
		MessageResult msgResult = jpush.sendNotificationWithAlias(sendNo, alias, msgTitle, msgContent, 0, extra);
		return msgResult;
	}
	
	public MessageResult sendAppKey(String msgTitle,String msgContent) {
		int sendNo = getRandomSendNo();
		jpush = new JPushClient(masterSecret, appKey, timeToLive);
		MessageResult msgResult = jpush.sendNotificationWithAppKey(sendNo, msgTitle, msgContent);
		return msgResult;
	}
	
	public static final int MAX = Integer.MAX_VALUE;
	public static final int MIN = (int) MAX/2;

	/**
	 * 保持 sendNo 的唯一性是有必要的
	 * It is very important to keep sendNo unique.
	 * @return sendNo
	 */
	public static int getRandomSendNo() {
		return (int) (MIN + Math.random() * (MAX - MIN));
	}
	
	public static void main(String[] args) {
		SendPushServicev2 jp = new SendPushServicev2();
		Map<String, Object> extra = new HashMap<String, Object>();
		extra.put("strxxlx", "120");
		extra.put("strkey", "1053");
		extra.put("strname", "刘振华");
		MessageResult msgResult = null;
		//msgResult = jp.sendTag("4", "测试1", "张三", extra);
		//System.out.println(msgResult);
		msgResult = jp.sendAlias("4000010065", "", "张三11111111", extra);
		System.out.println(msgResult);
		//msgResult = jp.sendAppKey("广播", "广播广播广播广播");
		//System.out.println(msgResult);
	}
}
