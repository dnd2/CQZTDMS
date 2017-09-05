package com.infodms.dms.util.enums;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommonEnum {

	// 对象类型
	public enum TarTypeEnum {
		经销商服务站("dealer"),
		用户("owner"),
		职位("position"),
		指定用户("target");

		private String value;

		public String getValue() {
			return this.value;
		}

		TarTypeEnum(String value) {
			this.value = value;
		}	

		public static TarTypeEnum getTarTypeEnum(String value) {
			TarTypeEnum tt = null;

			for (TarTypeEnum enum1 : TarTypeEnum.values()) {
				if (enum1.getValue().equals(value)) {
					tt = enum1;
					break;
				}
			}
			return tt;
		}
		
		public static List<Map<String, String>> getTarTypeEnumList(){
			List<Map<String, String>> list = new LinkedList<Map<String,String>>();
			TarTypeEnum[] enumList = TarTypeEnum.values();
			for (TarTypeEnum tt : enumList) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", tt.value);
				map.put("name", tt.name());
				list.add(map);
			}
			return list;
		}
	}
	// 提醒处理状态
	public enum NoticeHandleStateEnum {
		已处理("1"),
		未处理("0");

		private String value;

		public String getValue() {
			return this.value;
		}

		NoticeHandleStateEnum(String value) {
			this.value = value;
		}	

		public static NoticeHandleStateEnum getNoticeHandleStateEnum(String value) {
			NoticeHandleStateEnum tt = null;

			for (NoticeHandleStateEnum enum1 : NoticeHandleStateEnum.values()) {
				if (enum1.getValue().equals(value)) {
					tt = enum1;
					break;
				}
			}
			return tt;
		}
		
		public static List<Map<String, String>> getNoticeHandleStateEnumList(){
			List<Map<String, String>> list = new LinkedList<Map<String,String>>();
			NoticeHandleStateEnum[] enumList = NoticeHandleStateEnum.values();
			for (NoticeHandleStateEnum tt : enumList) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", tt.value);
				map.put("name", tt.name());
				list.add(map);
			}
			return list;
		}
	}
	// 提醒处理状态
	public enum NoticeTypeEnum {
		通知类("0"),
		处理类("1");

		private String value;

		public String getValue() {
			return this.value;
		}

		NoticeTypeEnum(String value) {
			this.value = value;
		}	

		public static NoticeTypeEnum getNoticeTypeEnum(String value) {
			NoticeTypeEnum tt = null;

			for (NoticeTypeEnum enum1 : NoticeTypeEnum.values()) {
				if (enum1.getValue().equals(value)) {
					tt = enum1;
					break;
				}
			}
			return tt;
		}
		
		public static List<Map<String, String>> getNoticeTypeEnumList(){
			List<Map<String, String>> list = new LinkedList<Map<String,String>>();
			NoticeTypeEnum[] enumList = NoticeTypeEnum.values();
			for (NoticeTypeEnum tt : enumList) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", tt.value);
				map.put("name", tt.name());
				list.add(map);
			}
			return list;
		}
	}
	// 关系运算符
	public enum RelationsEnum {
		等于("="),
		大于(">"),
		大于等于(">="),
		小于("<"),
		小于等于("<="),
		不等于("<>"),
		非空("is not");

		private String value;

		public String getValue() {
			return this.value;
		}

		RelationsEnum(String value) {
			this.value = value;
		}	

		public static RelationsEnum getRelationsEnum(String value) {
			RelationsEnum tt = null;

			for (RelationsEnum enum1 : RelationsEnum.values()) {
				if (enum1.getValue().equals(value)) {
					tt = enum1;
					break;
				}
			}
			return tt;
		}
		
		public static List<Map<String, String>> getRelationsEnumList(){
			List<Map<String, String>> list = new LinkedList<Map<String,String>>();
			RelationsEnum[] enumList = RelationsEnum.values();
			for (RelationsEnum tt : enumList) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", tt.value);
				map.put("name", tt.name());
				list.add(map);
			}
			return list;
		}
	}
}
