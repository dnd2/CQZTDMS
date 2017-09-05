package com.infodms.dms.dao.sales.paraConfigDao;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infoservice.po3.bean.PO;

public class TemplateDownLoadDao extends BaseDao {
	
	public static Logger logger = Logger.getLogger(YearPlanDao.class);
	private static final TemplateDownLoadDao dao = new TemplateDownLoadDao ();
	public static final TemplateDownLoadDao getInstance() {
		return dao;
	}
	private TemplateDownLoadDao() {}

	/*
	 * 查询所有模板文件的服务器配置
	 */
	public List<Map<String, Object>> selectTemplateParaConfig(String typeCode,String companyId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select p.PARA_ID, p.TYPE_CODE, p.TYPE_NAME, p.PARA_NAME,p.PARA_VALUE\n");
		sql.append("  from TM_BUSINESS_PARA p\n");  
		sql.append(" where p.TYPE_CODE = ?");
		params.add(typeCode);
		sql.append("  and p.OEM_COMPANY_ID=?");
		params.add(companyId);

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 查询所有模板文件的服务器配置
	 */
	public Map<String, Object> selectTemplateParaConfigMap(String fileId,String companyId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select p.PARA_ID, p.TYPE_CODE, p.TYPE_NAME, p.PARA_NAME,p.PARA_VALUE\n");
		sql.append("  from TM_BUSINESS_PARA p\n");  
		sql.append(" where p.PARA_ID = ?");
		params.add(fileId);
		sql.append("  and p.OEM_COMPANY_ID=?");
		params.add(companyId);
		return dao.pageQueryMap(sql.toString(), params, getFunName());
	}
	
	/*
	 * 查询所有模板文件FS_FILEUPLOAD,这个方法对应FileDownloadAction.download()
	 */
	public List<Map<String, Object>> selectTemplateFsFile(){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select d.FILEID, d.FILENAME from FS_FILEUPLOAD d;");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
