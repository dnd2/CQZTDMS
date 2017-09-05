package com.infodms.dms.common.component.acl;

import com.infodms.dms.common.Constant;
import com.infoservice.mvc.component.acl.AclResource;

public class CommonAclResource extends AclResource {
	private static final long serialVersionUID = 6010188874482618941L;
	
	private String aclUrl = null;
	
	public CommonAclResource(String aclUrl){
		this.aclUrl = aclUrl;
	}
	
	public boolean check(Object res) {
		String resUrl = res.toString();
		if(resUrl.startsWith(Constant.COMMON_URI)){
			return true;
		}
		return aclUrl!=null&&aclUrl.equals(resUrl.substring(0,resUrl.lastIndexOf("/")));
	}

}
