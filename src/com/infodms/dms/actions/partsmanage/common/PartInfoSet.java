package com.infodms.dms.actions.partsmanage.common;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PartinfoBean;
import com.infodms.dms.common.Constant;
import com.infoservice.mvc.context.ActionContext;

public class PartInfoSet {
//	private static ThreadLocal<Set<PartinfoBean>> partInfoSet = new ThreadLocal<Set<PartinfoBean>>();
//	
//	public static Set<PartinfoBean> getPartInfoSet() {
//		Set<PartinfoBean> partInfo = partInfoSet.get();
//		if (null != partInfo) {
//			return partInfo;
//		}
//		partInfo = new LinkedHashSet<PartinfoBean>();
//		partInfoSet.set(partInfo);
//		return partInfo;
//	}
//	
//	public static void setPartInfoSet(Set<PartinfoBean> partInfo) {
//		partInfoSet.set(partInfo);
//	}
//	
//	public static void removePartInfoSet() {
//		partInfoSet.remove();
//	}
	
	private static Map<String, Set<PartinfoBean>> partInfoSet = new HashMap<String, Set<PartinfoBean>>();
	
	public static Set<PartinfoBean> getPartInfoSet(String user) {
		Set<PartinfoBean> partInfo = partInfoSet.get(user);
		if (null != partInfo) {
			return partInfo;
		}
		partInfo = new LinkedHashSet<PartinfoBean>();
		partInfoSet.put(user, partInfo);
		return partInfo;
	}
	
	public static void setPartInfoSet(String user, Set<PartinfoBean> partInfo) {
		partInfoSet.put(user, partInfo);
	}
	
	public static void removePartInfoSet(String user) {
		partInfoSet.remove(user);
	}
	
}
