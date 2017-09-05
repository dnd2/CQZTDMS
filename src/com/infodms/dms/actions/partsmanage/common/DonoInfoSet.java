package com.infodms.dms.actions.partsmanage.common;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.infodms.dms.bean.PartinfoBean;

/**
 * @Title: DonoInfoSet.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-24
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class DonoInfoSet {
	
	private static Map<Long, Set<PartinfoBean>> partSignSet = new HashMap<Long, Set<PartinfoBean>>();
	
	public static Set<PartinfoBean> getPartInfoSet(Long user) {
		Set<PartinfoBean> partInfo = partSignSet.get(user);
		if (null != partInfo) {
			return partInfo;
		}
		partInfo = new LinkedHashSet<PartinfoBean>();
		partSignSet.put(user, partInfo);
		return partInfo;
	}
	
	public static void setPartInfoSet(Long user, Set<PartinfoBean> partInfo) {
		partSignSet.put(user, partInfo);
	}
	
	public static void removePartInfoSet(Long user) {
		partSignSet.remove(user);
	}
}
