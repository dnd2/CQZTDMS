package com.infodms.yxdms.action;

import java.util.Map;

import com.infodms.yxdms.service.CommonService;
import com.infodms.yxdms.service.OldReturnService;
import com.infodms.yxdms.service.impl.CommonServiceImpl;
import com.infodms.yxdms.service.impl.OldReturnServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
/**
 * 开票action
 * @author yuewei
 *
 */
public class CommonAction extends BaseAction{

	private CommonService commonservice = new CommonServiceImpl();
	private OldReturnService oldreturnservice = new OldReturnServiceImpl();
	
	public void del(){
		String tableName = getParam("tableName");
		String idName = getParam("idName");
		String id = getParam("id");
		System.out.println(id+"***********");
		int res = commonservice.del(tableName,idName,id);
		setJsonSuccByres(res);
	}
	public void del1(){
		String tableName = getParam("tableName");
		String idName = getParam("idName");
		String id = getParam("id");
		int re = oldreturnservice.updateOldreturnstatus(request);//修改主表状态
		int res = commonservice.del(tableName,idName,id);
		setJsonSuccByres(res);
	}
}
