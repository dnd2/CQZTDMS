package com.infodms.dms.dao.sales.paraConfigDao;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infoservice.po3.bean.PO;

public class DateSetImportDao extends BaseDao {
	
	public static Logger logger = Logger.getLogger(YearPlanDao.class);
	private static final DateSetImportDao dao = new DateSetImportDao ();
	public static final DateSetImportDao getInstance() {
		return dao;
	}
	private DateSetImportDao() {}

	/*
	 * 查询所有模板文件的服务器配置
	 */
	public List<Map<String, Object>> selectTmpDateSet(String userId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select s.SET_YEAR, s.SET_MONTH, s.SET_WEEK, s.SET_DATE\n");
		sql.append("  from TMP_DATE_SET s\n");  
		sql.append(" where s.USER_ID = ?");
		params.add(userId);

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 查询所有模板文件的服务器配置
	 */
	public Map<String, Object> selectTemplateParaConfigMap(String fileId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select p.PARA_ID, p.TYPE_CODE, p.TYPE_NAME, p.PARA_NAME,p.PARA_VALUE\n");
		sql.append("  from TM_BUSINESS_PARA p\n");  
		sql.append(" where p.PARA_ID = ?");
		params.add(fileId);

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
