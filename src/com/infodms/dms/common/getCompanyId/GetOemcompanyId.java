package com.infodms.dms.common.getCompanyId;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;

/**
 * @Title: CHANADMS
 *
 * @Description:
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company:  www.infoservice.com.cn
 * @Date: 2010-7-13
 *
 * @author zjy 
 * @mail   zhaojinyu@infoservice.com.cn
 * @version 1.0
 * @remark 
 */
//车厂端与经销商端公用功能查询companyId的公用方法
public class GetOemcompanyId {

	public static Long getOemCompanyId(AclUserBean logonUser)
	{
		Long companyId=null;
		if(logonUser==null)
		{
			return null;	
		}
		companyId=logonUser.getCompanyId();
		String dutyType=logonUser.getDutyType();
		if(!"".equals(dutyType)&&String.valueOf(Constant.DUTY_TYPE_DEALER).equals(dutyType))
		{
			companyId=Long.valueOf(logonUser.getOemCompanyId());
		}
		return companyId;
	}
}
