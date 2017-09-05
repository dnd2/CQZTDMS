package com.infodms.dms.actions.partsmanage.common;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.infodms.dms.bean.PartClaimItemBean;

public class PartClaimItemSet {
//	private static ThreadLocal<Set<PartClaimItemBean>> partInfoSet = new ThreadLocal<Set<PartClaimItemBean>>();
//	
//	public static Set<PartClaimItemBean> get() {
//		Set<PartClaimItemBean> partInfo = partInfoSet.get();
//		if (null != partInfo) {
//			return partInfo;
//		}
//		partInfo = new LinkedHashSet<PartClaimItemBean>();
//		partInfoSet.set(partInfo);
//		return partInfo;
//	}
//	
//	public static void set(Set<PartClaimItemBean> partInfo) {
//		partInfoSet.set(partInfo);
//	}
//	
//	public static void remove() {
//		partInfoSet.remove();
//	}
	
	private static Map<Long, Set<PartClaimItemBean>> partInfoSet = new HashMap<Long, Set<PartClaimItemBean>>();
	
	public static Set<PartClaimItemBean> get(Long user) {
		Set<PartClaimItemBean> partInfo = partInfoSet.get(user);
		if (null != partInfo) {
			return partInfo;
		}
		partInfo = new LinkedHashSet<PartClaimItemBean>();
		partInfoSet.put(user, partInfo);
		return partInfo;
	}
	
	public static void set(Long user, Set<PartClaimItemBean> partInfo) {
		partInfoSet.put(user, partInfo);
	}
	
	public static void remove(Long user) {
		partInfoSet.remove(user);
	}
	
}
