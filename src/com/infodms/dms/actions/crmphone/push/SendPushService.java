package com.infodms.dms.actions.crmphone.push;

import java.util.HashMap;

import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

public class SendPushService {
	
	private static String appKey ="4b1b4dec0a59cbdeb511191a";	//必填，efb3861086bd48a43d8ed0c9例如466f7032ac604e02fb7bda89

	private static String masterSecret = "18aebf5ca01d5d734c0e38e4";//"13ac09b17715bd117163d8a1";//必填，45b4939ad55ed96413c29a80每个应用都对应一个masterSecret
	
	/**
	 * 保存离线的时长。秒为单位。最多支持10天（864000秒）。
	 * 0 表示该消息不保存离线。即：用户在线马上发出，当前不在线用户将不会收到此消息。
	 * 此参数不设置则表示默认，默认为保存1天的离线消息（86400秒)。
	 */
	private static int timeToLive =  60 * 60 * 24;  

	private static JPushClient jpush = null;
	public static final String ALERT = "Test from API Example - alert";
	public static final String TITLE = "Test from API example";
	
	//发送全网广播通知
	public static void sendNotification(String stralert) {
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, timeToLive);
		
//		PushPayload payload = buildPushObject_all_all_alert(stralert);
//		try {
//            PushResult result = jpushClient.sendPush(payload);
//            
//        } catch (APIConnectionException e) {
//            
//        } catch (APIRequestException e) {
//        }
	}
	
	//发送全网广播通知
	public static void sendNotification(String stralert,String stralias,HashMap mapextra) {
//			JPushClient jpushClient = new JPushClient(masterSecret, appKey, timeToLive);
			System.out.println("----------------------------");
			SendPushServicev2 jp = new SendPushServicev2();
			MessageResult msgResult = null;
			msgResult = jp.sendAlias(stralias, "", stralert, mapextra);
			System.out.println("----------------------------");
//			PushPayload payload = buildPushObject_all_alias_alert(stralert,stralias,mapextra);
//			try {
//	            PushResult result = jpushClient.sendPush(payload);
//	            
//	        } catch (APIConnectionException e) {
//	            
//	        } catch (APIRequestException e) {
//	        }
		}
	
//	
//	//所有平台，所有设备，全网通知，内容为 ALERT 的通知。
//	public static PushPayload buildPushObject_all_all_alert(String stralert) {
//	    return PushPayload.alertAll(stralert);
//	}
//	
//
//	/**
//	 * 所有平台
//	 * @param stralias 推送通知的别名，必须传
//	 * @param stralert 通知内容，必须传
//	 * @param mapextra扩展字段，比如通知id
//	 * @return
//	 */
//    public static PushPayload buildPushObject_all_alias_alert(String stralert,String stralias,HashMap mapextra) {
//        return PushPayload.newBuilder()
//                .setPlatform(Platform.all())
//                .setAudience(Audience.alias(stralias))
//                 .setNotification(Notification.newBuilder()
//                		.setAlert(stralert)
//                		.addPlatformNotification(AndroidNotification.newBuilder()
//                				.addExtras(mapextra).build())
//                		.addPlatformNotification(IosNotification.newBuilder()
//                				.incrBadge(1)
//                				.addExtras(mapextra)
//                				.build())
//                		.build())
//                .build();
//    }
//    
//   
//
//    /**
//     * 平台Android和ios
//     * @param strtag推送通知标签名称，必须传
//     * @param stralert通知内容，必须传
//     * @param strtitle通知标题
//     * @param strextra通知扩张字段，比如通知的id等
//     * @return
//     */
//    public static PushPayload buildPushObject_android_and_ios(String strtag,String stralert,String strtitle,HashMap mapextra) {
//        return PushPayload.newBuilder()
//                .setPlatform(Platform.android_ios())
//                .setAudience(Audience.tag(strtag))
//                .setNotification(Notification.newBuilder()
//                		.setAlert(stralert)
//                		.addPlatformNotification(AndroidNotification.newBuilder()
//                				.setTitle(strtitle)
//                				.addExtras(mapextra).build())
//                		.addPlatformNotification(IosNotification.newBuilder()
//                				.incrBadge(1)
//                				.addExtras(mapextra)
//                				.build())
//                		.build())
//                .build();
//    }
//    
    public static void main(String[] args) {
    	//sendNotification("看通知！");
    	HashMap hm = new HashMap();
    	hm.put("strpushtype", "100");
    	hm.put("strkey", "100");
    	sendNotification("看通知！","4000010067",hm);
	}

}
