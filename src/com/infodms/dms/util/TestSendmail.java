package com.infodms.dms.util;


public class TestSendmail {
	public static void main(String[]args){
		String files[] = new String[2];
		files[0]="d:\\approval.log";
		files[1]="d:\\approval.log";
		System.out.println(MailSender.INSTANCE.sendMail("huangxiaodong2@infoservice.com.cn", "hello", "hello",files));
	}
} 
