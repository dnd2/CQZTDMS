package com.infodms.yxdms.dao;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.dao.common.JCDynaBeanCallBack;
import com.infodms.dms.po.TcCodePO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.callbackimpl.POCallBack;
import com.infoservice.po3.core.callback.DAOCallback;

public abstract class IBaseDao<T extends PO> {
	
	protected POFactory factory = POFactoryBuilder.getInstance();
	protected abstract T wrapperPO(ResultSet rs, int idx);
	
	protected Object wrapperObject(ResultSet rs, int idx) {
		return null;
	}

	public Integer getIntegerPK(T t) {
		return factory.getIntegerPK(t);
	}
	
	public Long getLongPK(T t) {
		return factory.getLongPK(t);
	}
	
	public String getStringPK(T t) {
		return factory.getStringPK(t);
	}
	
	public List<T> select(T t) {
		return factory.select(t);
	}

	public List<T> select(T t, int i) {
		return factory.select(t, new DAOCallback<T>() {
			public T wrapper(ResultSet rs, int idx) {
				return wrapperPO(rs, idx);
			}
		});
	}
	
	public List<T> select(Class<T> t,String sql, List<Object> param) {
		return factory.select(sql, param, new POCallBack<T>(factory, t));
	}
	
	public void insert(T t) {
		factory.insert(t);
	}
	
	public void insert(List<T> t) {
		factory.insert(t);
	}
	
	
	public PageResult<T> pageQuery(String sql, List<Object> params, int pageSize, int curPage) {
		PageResult<T> tcs = factory.pageQuery(sql, params, new DAOCallback<T>() {
			public T wrapper(ResultSet rs, int idx) {
				return wrapperPO(rs, idx);
			}
		}, pageSize, curPage);
		return tcs;
	}
	
	public PageResult<T> pageQuery(Class<T> t, String sql, List<Object> params, int pageSize, int curPage) {
		PageResult<T> tcs = factory.pageQuery(sql, params,
				new POCallBack<T>(factory, t), pageSize, curPage);
		return tcs;
	}
	
	
	public PageResult<Object> pageQueryObject(String sql, List<Object> params, int pageSize, int curPage) {
		PageResult<Object> tcs = factory.pageQuery(sql, params, new DAOCallback<Object>() {
			public Object wrapper(ResultSet rs, int idx) {
				return wrapperObject(rs, idx);
			}
		}, pageSize, curPage);
		return tcs;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> pageQuery(String sql, List<Object> params, final String funName) {
		List<DynaBean> tmp = factory.select(sql, params, new JCDynaBeanCallBack());
		LinkedList<Map<String,Object>> ret = new LinkedList<Map<String,Object>>();
		for( DynaBean bean : tmp ){
			ret.addLast((Map<String,Object>)bean);
		}
		return ret;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> pageQuery01(String sql, List<Object> params, final String funName) {
		List<DynaBean> tmp = factory.select(sql, params, new JCDynaBeanCallBack());
		LinkedList<Map<String,String>> ret = new LinkedList<Map<String,String>>();
		for( DynaBean bean : tmp ){
			ret.addLast((Map<String,String>)bean);
		}
		return ret;
	}
	
	public Map<String, Object> pageQueryMap(String sql, List<Object> params, final String funName) {
		List<Map<String, Object>> list = pageQuery(sql, params, funName);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> pageQuery(String sql, List<Object> params, final String funName, int pageSize, int curPage) {
		PageResult<DynaBean> tmps = factory.pageQuery(sql, params, new JCDynaBeanCallBack(),pageSize,curPage );
		PageResult<Map<String,Object>> ret = new PageResult<Map<String,Object>>();
		ret.setCurPage(tmps.getCurPage());
		ret.setPageSize(tmps.getPageSize());
		ret.setTotalPages(tmps.getTotalPages());
		ret.setTotalRecords(tmps.getTotalRecords());
		List<DynaBean> tmp = tmps.getRecords();
		LinkedList<Map<String,Object>> t1 = new LinkedList<Map<String,Object>>();
		if( tmp!=null ){
			for( DynaBean bean : tmp ){
				t1.addLast((Map<String,Object>)bean);
			}		
			ret.setRecords(t1);	
		}
		return ret;
	}
	
	public int update(T t1, T t2) {
		return factory.update(t1, t2);
	}
	
	public int update(String sql, List<?> params) {
		return factory.update(sql, params);
	}
	
	public int delete(T t) {
		return factory.delete(t);
	}
	
	public int delete(String sql, List<?> params) {
		return factory.delete(sql, params);
	}
	
	public Object callFunction(String arg0, int arg1, List<Object> arg2) {
		return factory.callFunction(arg0, arg1, arg2);
	}
	
	public List<Object> callProcedure(String arg0, List<Object> arg1, List<Integer> arg2) {
		return factory.callProcedure(arg0, arg1, arg2);
	}
	
	public String getFunName() {
		StackTraceElement stack[] = new Throwable().getStackTrace(); 
		StackTraceElement ste = stack[1];   
		StringBuilder strBuilder = new StringBuilder();
		return strBuilder.append(ste.getClassName()).
			append(".").append(ste.getMethodName()).
			append(ste.getLineNumber()).
			toString();
	}
	
	@SuppressWarnings("rawtypes")
	public List<Object> select(String sql, List<Object> params, Class clz) {
		return factory.select(sql, params, new DAOCallback<Object>() {
			public Object wrapper(ResultSet rs, int idx) {
				return wrapperObject(rs, idx);
			}
		});
	}
	
	public void insert(String sql){
		factory.insert(sql, null);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void toExcel(String excelName,ActionContext act,String[] head,List params){
		String systemDateStr = BaseUtils.getSystemDateStr();
		try {
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, excelName+systemDateStr+".xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public String getTypeDesc(String codeId){
		TcCodePO t=new TcCodePO();
		t.setCodeId(codeId);
		List<TcCodePO> list= factory.select(t);
		if(list!=null && list.size()>0){
			t=list.get(0);
			return t.getCodeDesc();
		}else{
			return "无";
		}
	}
}
