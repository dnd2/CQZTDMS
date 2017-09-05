package com.infodms.yxdms.action;

import com.infodms.yxdms.utils.BaseAction;

public class ReturnAndclaimAction extends BaseAction{
	public void  returnAndclaim(){
		sendMsgByUrl("special", "select_returncar_goodwill_claim", "退换车善意索赔查询");
	}
}
