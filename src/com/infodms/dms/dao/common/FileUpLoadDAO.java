package com.infodms.dms.dao.common;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.dms.dao.common.JCDynaBeanCallBack;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.DynaBean;


/**
 * @Title: CHANADMS
 *
 * @Description:
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-17
 *
 * @author andy 
 * @mail   andy.ten@tom.com
 * @version 1.0
 * @remark 
 */
public class FileUpLoadDAO 
{
	private static POFactory factory = POFactoryBuilder.getInstance();
	/**
	 * @param args
	 * void
	 * 2010-6-17
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}
	
	/**
	 * 生成无效的文件信息
	 * 
	 * @param file
	 * @throws SQLException
	 */
	public void addDisableFile(FsFileuploadPO po ,AclUserBean logonUser) throws SQLException
	{
		po.setStatus(Constant.STATUS_DISABLE);
		po.setCreateBy(logonUser.getUserId());
		po.setCreateDate(new Date());
		po.setFjid(Long.valueOf(SequenceManager.getSequence(null)));
		factory.insert(po);
	}
	
	/**
	 * 生成有效的文件信息
	 * 
	 * @param file
	 * @throws SQLException
	 */
	public void addenableFile(String ywzj ,String fjids ,AclUserBean logonUser) throws SQLException
	{
		String sql = "UPDATE FS_FILEUPLOAD SET YWZJ = " + ywzj +",UPDATE_DATE = SYSDATE,UPDATE_BY = " +logonUser.getUserId()+ " WHERE FJID IN (" + fjids +") "; 
		factory.update(sql, null);
	}
	
	/**
	 * 删除上传的文件
	 * @param fjid
	 * @param logonUser
	 * @throws SQLException
	 */
	public void delUploadFile(String fjid ,AclUserBean logonUser) throws SQLException
	{
		String sql = "DELETE FROM FS_FILEUPLOAD WHERE FJID = "+fjid; 
		factory.delete(sql, null);
	}
	/**
	 * 批量删除上传的文件
	 * @param fjid
	 * @param logonUser
	 * @author add by subo
	 * @throws SQLException
	 */
	public void delAllUploadFiles(String ywzj,String ids) throws SQLException
	{
		if(ywzj==null||"".equals(ywzj)) return;
		StringBuffer sql = new StringBuffer("");
		sql.append("DELETE FROM  FS_FILEUPLOAD A");
		sql.append(" WHERE A.YWZJ = "+ywzj);
		if(ids!=null&&!"".equals(ids)){
			sql.append(" AND A.FJID not in ("+ids+")");
		}
		factory.delete(sql.toString(), null);
	}
	
	/**
	 * 批量删除上传的文件
	 * @param fjid 待删除文件的主键
	 * @param logonUser 
	 * @author add by subo
	 * @throws SQLException
	 */
	public void delAllUploadFiles1(String ywzj,String ids) throws SQLException
	{
		if(ywzj==null||"".equals(ywzj)) return;
		StringBuffer sql = new StringBuffer("");
		sql.append("DELETE FROM  FS_FILEUPLOAD A");
		sql.append(" WHERE A.YWZJ = "+ywzj);
		if(ids!=null&&!"".equals(ids)){
			sql.append(" AND A.FJID in ("+ids+")");
		}
		factory.delete(sql.toString(), null);
	}
	
	/**
	 * 查询文件是否实销更改新上传的文件信息
	 * 
	 * @param file
	 * @throws SQLException
	 */
	public List<DynaBean> getIfUpdateFile(String[] fjids) throws SQLException
	{
		String str="";
		if(fjids!=null&&fjids.length>0){
			for(int i=0;i<fjids.length;i++){
				if(fjids[i]!=null||!"".equals(fjids[i])){
					str += fjids[i]+",";
				}
			}
			if(str.length()>0){
				str = str.substring(0,str.length()-1);
			}
		}
		String sql = "SELECT count(1) tflag  FROM Fs_Fileupload WHERE 1=1  AND Fjid IN (" + str +") and ywzj is not null "; 
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());

	}
	
	/**
	 * 查询文件是否建立业务关系
	 * 
	 * @param file
	 * @throws SQLException
	 */
	public List<DynaBean> relationCheckFile(String fjid) throws SQLException
	{
		String sql = "SELECT count(1) flag  FROM Fs_Fileupload WHERE 1=1  AND Fjid=" + fjid +" and ywzj is not null "; 
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/**
	 * 生成有效的文件信息
	 * 
	 * @param file一次更新一个文件;
	 * @throws SQLException
	 */
	public void updateRelationFile(String ywzj ,String fjid,AclUserBean logonUser) throws SQLException
	{
		String sql = "UPDATE FS_FILEUPLOAD SET YWZJ = " + ywzj +",UPDATE_DATE = SYSDATE,UPDATE_BY = " +logonUser.getUserId()+ " WHERE FJID IN (" + fjid +") "; 
		factory.update(sql, null);
	}
	
	/**
	 * 生成无效的文件信息 审核成功调用文件创建文件
	 * 
	 * @param file
	 * @throws SQLException
	 */
	public void  reviewedFile(FsFileuploadPO po ,AclUserBean logonUser) throws SQLException
	{
		po.setStatus(Constant.STATUS_DISABLE);
		po.setCreateBy(logonUser.getUserId());
		po.setCreateDate(new Date());
		
		factory.insert(po);
	}
	/**
	 * 查询网络日志关联关系对应数据条数
	 * 
	 * @param file
	 * @throws SQLException
	 */
	public List<DynaBean> getNetCheckFile(String speciaId) throws SQLException
	{
		String sql = "SELECT count(1) cNum  FROM Fs_Fileupload WHERE 1=1  AND ywzj="+speciaId+"  "; 
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	/**
	 * 修改网络日志上传附件信息
	 * 
	 * @param file;
	 * @throws SQLException
	 */
	public void updateNetFile(String speciaId,AclUserBean logonUser) throws SQLException
	{
		String sql = "UPDATE FS_FILEUPLOAD SET YWZJ = " + null +",UPDATE_DATE = SYSDATE,UPDATE_BY = " +logonUser.getUserId()+ " WHERE YWZJ IN (" + speciaId +") "; 
		factory.update(sql, null);
	}
}
