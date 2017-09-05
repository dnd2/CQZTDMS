package com.infodms.dms.common.component.dict;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.CodeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.FileConstant;
import com.infodms.dms.po.TcCodePO;
import com.infoservice.filestore.FileStore;
import com.infoservice.mvc.convert.JsonConverter;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.exceptions.DAOException;

/**
 * @author ZhaoLi 字典表操作类
 */
public class CodeDict{
	private static CodeDict instance = null;
	public static Date dt = null;
	
	public static Map<String,List<TcCodePO>> dictMap =  null;
	private static Logger logger = Logger.getLogger(CodeDict.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	public static final String tc_code_sql = "select code_id,code_desc,type,type_name from tc_code where status="
		+Constant.STATUS_ENABLE+" order by type, num";
	
	private CodeDict() {
	}

	public static CodeDict getInstance() {
		if (instance == null) {
			synchronized (CodeDict.class) {
				if (instance == null) {
					instance = new CodeDict();
				}
			}
		}
		return instance;
	}
	public String init() {
		try{
			List<CodeBean> codeList = null;
			if(dt==null || hasCodeUpdate()){
				//将数据字典写入内在中
				setDictMap();
				logger.info("数据字典写入了内在中,大小:"+dictMap.size());
				
				codeList = selCodeList();
			}
			byte[] codeJson = null;
			if(codeList != null){
				codeJson = toJsonFormat("var codeData = ", codeList);
			}
			String fid = null;
			if(codeJson != null){
				fid = FileStore.getInstance().write("codeData.js",
						codeJson);
			}
			String codeJsUrl = null;
			if(fid != null){
				codeJsUrl = FileStore.getInstance().getDomainURL(fid);
				FileConstant.codeJsUrl = codeJsUrl;
				logger.info("codeJsUrl"+FileConstant.codeJsUrl);
				dt = new Date();
			}
			return fid;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean hasCodeUpdate(){
		//if(true)return true;
		String sql = "select count(code_id) result from tc_code where create_date>? or update_date > ?";
		List<Object> params = Arrays.asList(new Object[]{dt});
		params.add(dt);
		List<Integer> rsList = null;
		boolean rs = false;
		try {
		//	POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
			rsList = factory.select(sql, params, new DAOCallback<Integer>(){
				public Integer wrapper(ResultSet rs, int idx) {
					Integer result;
					try {
						result = rs.getInt("result");
					} catch (SQLException e) {
						logger.error("检查数据字典表是否更新,发生错误");
						throw new DAOException(e);
					}
					return result;
				}});
			if(rsList.get(0) > 0){
				logger.info("TC_CODE表有更新!");
			}else{
				logger.info("TC_CODE表没更新!");
			}
	//		POContext.endTxn(true);
			rs = rsList.get(0) > 0;
		} catch (Exception e) {
		//	POContext.endTxn(false);
		}finally{
			//POContext.cleanTxn();
		}
		return rs;
	}
	
	public static List<CodeBean> selCodeList(){
		List<CodeBean> codeList = null;
		try {
		//	POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
			codeList = factory.select(tc_code_sql, null, new DAOCallback<CodeBean>(){
				public CodeBean wrapper(ResultSet rs, int idx) {
					CodeBean bean = new CodeBean();
					try {
						bean.setCodeDesc(rs.getString("code_desc"));
						bean.setCodeId(rs.getString("code_id"));
						bean.setType(rs.getString("type"));
					} catch (SQLException e) {
						throw new DAOException(e);
					}
					return bean;
				}});
		//	POContext.endTxn(true);
		} catch (Exception e) {
			POContext.endTxn(false);
		} finally{
			logger.info("执行了查询字典表的SQL: "+tc_code_sql);
			//POContext.cleanTxn();
		}
		return codeList;
	}
	
	@SuppressWarnings("unchecked")
	public static byte[] toJsonFormat(String pre, List list){
		HashMap<String,Object> pa = new HashMap<String,Object>();
		pa.put("data", list);
		JsonConverter jc = new JsonConverter();
		byte[] bytes = null;
		try {
//			logger.info(new String(jc.sourceToDest(pa),"UTF-8"));
			bytes =  (pre+new String(jc.sourceToDest(pa),"UTF-8")).getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
	/**
	 * 获得缓存中数据字典表
	 * add by zhangxianchao
	 * @return
	 */
	public static Map<String,List<TcCodePO>> getDictMap(){
		return dictMap;
	}
	/**
	 * 将字典信息写入Map中
	 * add by zhangxianchao
	 */
	public  static  void setDictMap(){
		List<TcCodePO> codeList = factory.select(new TcCodePO()); // 字典表
		Map<String,List<TcCodePO>> dictMapTemp = new TreeMap<String,List<TcCodePO>>();
		String codeType = null;
		List<TcCodePO> codeValueList = null;
		for(TcCodePO code:codeList){
			codeType = code.getType();
			if(dictMapTemp.containsKey(codeType)){
				codeValueList = dictMapTemp.get(codeType);
				codeValueList.add(code);
			}else{
				codeValueList = new LinkedList<TcCodePO>();
				codeValueList.add(code);
				dictMapTemp.put(codeType, codeValueList);
			}
		}
		dictMap = dictMapTemp;
	}
	/**
	 * 根据数据字典的类型，返回这个类型对应的数据字典对象的集合
	 * @param type
	 * @return
	 */
	public static List<TcCodePO> getDictListByType(String type){
		List<TcCodePO> codeList = dictMap.get(type);
		return codeList;
	}
	/**
	 * 根据数据字典的类型，返回这个类型对应的数据字典对象的数组
	 * @param type
	 * @return
	 */
	public static TcCodePO[] getDictArrayByType(String type){
		List<TcCodePO> codeList = dictMap.get(type);
		TcCodePO[] tcArray = new TcCodePO[codeList.size()];
		return (TcCodePO[]) codeList.toArray(tcArray);
	}
	/**
	 * 根据数据字典的类型返回这个类型对应的数据字典对象的CodeName的集合
	 * @param type
	 * @return
	 */
	public static List<String> getDictNameListByType(String type){
		List<TcCodePO> codeList = dictMap.get(type);
		List<String> codeNameList = new LinkedList<String>();
		for(TcCodePO code:codeList){
			codeNameList.add(code.getCodeDesc());
		}
		return codeNameList;
	}
	/**
	 * 根据数据字典的类型返回这个类型对应的数据字典对象的CodeName的数组
	 * @param type
	 * @return
	 */
	public static String[] getDictNameArrayByType(String type){
		List<TcCodePO> codeList = dictMap.get(type);
		List<String> codeNameList = new LinkedList<String>();
		for(TcCodePO code:codeList){
			codeNameList.add(code.getCodeDesc());
		}
		String[] arrays = new String[codeNameList.size()];
		return (String[])(codeNameList.toArray(arrays));
	}
	/**
	 * 根据数据字典类型和数据描述，返回数据字典的codeID
	 * @param type
	 * @param codeDesc
	 * @return
	 */
	public static String getDictCodeByName(String type,String codeDesc){
		List<TcCodePO> codeList = dictMap.get(type);
		String codeId = null;
		for(TcCodePO code:codeList){
			if(code.getCodeDesc().equals(codeDesc)){
				codeId =  code.getCodeId();
			}
		}
		return codeId;
	}
	
	/**
	 * 根据数据字典类型和数据描述，返回数据字典的codeID
	 * @param type
	 * @param codeDesc
	 * @return
	 */
	public static String getDictDescById(String type,String id){
		List<TcCodePO> codeList = dictMap.get(type);
		String codeDesc = "";
		for(TcCodePO code:codeList){
			if(code.getCodeId().equals(id)){
				codeDesc =  code.getCodeDesc();
			}
		}
		return codeDesc;
	}
	
}
