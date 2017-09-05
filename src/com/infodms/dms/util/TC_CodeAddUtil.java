package com.infodms.dms.util;

import java.util.List;
import java.util.Map;

import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.KnowLedgeDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : TC_CodeAddUtil 
 * @Description   : TODO 
 * @author        : guozg
 * CreateDate     : 2013-4-6
 */
public class TC_CodeAddUtil {
	private static KnowLedgeDao dao = KnowLedgeDao.getInstance();
	private static TcCodeDao tcdao=TcCodeDao.getInstance();
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  获取下一个ID
	 * @param      : @param tc_type
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
//	public static Map<String,Object> getNextIdAndNum(String tc_type)
//	{
//		Map<String,Object> nextIdAndNum=null;
//		nextIdAndNum=dao.getNextIdAndNum(tc_type);
//		return nextIdAndNum;
//	}
	public static String genSelBoxExp(Integer type,String SelName)
	{
		StringBuilder selectBox=new StringBuilder();
		selectBox.append("<select name=\""+SelName+"\" id=\""+SelName+"\">");
		selectBox.append("<option value=0 selected>--请选择--");
		try{
			PageResult<Map<String,Object>> result=tcdao.genSelBoxExp(type.toString(),100,1) ;
			List<Map<String,Object>>r=result.getRecords();
			for(Map<String,Object> m:r)
			{
				selectBox.append("<option value=\""+m.get("CODE_ID")+"\">"+m.get("CODE_DESC")+"");
			}
		selectBox.append("</select>");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return selectBox.toString();
	}
	public static String genSelBoxExp(Integer type,String SelName,String id)
	{
		StringBuilder selectBox=new StringBuilder();
		selectBox.append("<select name=\""+SelName+"\" id=\""+SelName+"\">");
		selectBox.append("<option value=0 >--请选择--");
		try{
			PageResult<Map<String,Object>> result=tcdao.genSelBoxExp(type.toString(),id,100,1) ;
			List<Map<String,Object>>r=result.getRecords();
			for(Map<String,Object> m:r)
			{
				if(m.get("SELECTED").equals("true"))
				{
					selectBox.append("<option value=\""+m.get("CODE_ID")+"\" "+m.get("SELECTED")+">"+m.get("CODE_DESC")+"");
				}else{
					selectBox.append("<option value=\""+m.get("CODE_ID")+"\">"+m.get("CODE_DESC")+"");
				}
			}
		selectBox.append("</select>");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return selectBox.toString();
	}
	
	
}
