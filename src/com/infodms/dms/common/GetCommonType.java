package com.infodms.dms.common;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.AreaProvinceDealerAction;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infoservice.po3.bean.PO;

public class GetCommonType extends BaseDao {
	
	private Logger logger = Logger.getLogger(AreaProvinceDealerAction.class);
	
	public static String getMyCommonType(String typeId){
		GetCommonType dao=new GetCommonType();
		TcCodePO po= new TcCodePO();
		po.setCodeId(typeId);
		List list=dao.select(po);
		Map map=new HashMap();
		TcCodePO mypo=(TcCodePO) list.get(0);
		return mypo.getCodeDesc();
	}
	public static String getMyCommonTypeName(String typeName){
		GetCommonType dao=new GetCommonType();
		TcCodePO po= new TcCodePO();
		po.setCodeDesc(typeName);
		List list=dao.select(po);
		Map map=new HashMap();
		TcCodePO mypo=(TcCodePO) list.get(0);
		return mypo.getCodeId();
	}
	;
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	} 
	

}
