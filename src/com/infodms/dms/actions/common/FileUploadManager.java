package com.infodms.dms.actions.common;

import java.sql.SQLException;
import java.util.List;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.dao.common.FileUpLoadDAO;
import com.infoservice.infox.util.StringUtil;

/**
 * @Title: CHANADMS
 *
 * @Description:
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-18
 *
 * @author andy 
 * @mail   andy.ten@tom.com
 * @version 1.0
 * @remark 
 */
public class FileUploadManager
{

	/**
	 * @param args
	 * void
	 * 2010-6-18
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}
	
	/**
	 * 根据业务主键，生成有效的附件信息
	 * 
	 * void
	 * 2010-6-18
	 */
	public static void fileUploadByBusiness(String ywzj ,String[] fjids ,AclUserBean logonUser) throws SQLException
	{
		FileUpLoadDAO dao = new FileUpLoadDAO();
		String str="";
		if(fjids == null || "".equals(fjids)) return;
		if(ywzj == null || "".equals(ywzj)) return;
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
		dao.addenableFile(ywzj, str, logonUser);
	}
	
	/**
	 * 根据业务主键，生成有效的附件信息
	 * @param (String,List,AclUserBean)
	 * void
	 * 2010-8-31
	 */
	public static void fileUploadByBusiness2(String ywzj ,List fjids ,AclUserBean logonUser) throws SQLException
	{
		FileUpLoadDAO dao = new FileUpLoadDAO();
		String str="";
		if(fjids == null || "".equals(fjids)) return;
		if(ywzj == null || "".equals(ywzj)) return;
		if(fjids!=null&&fjids.size()>0){
			for(int i=0;i<fjids.size();i++){
				if(fjids.get(i)!=null||!"".equals(fjids.get(i))){
					str += fjids.get(i)+",";
				}
			}
			if(str.length()>0){
				str = str.substring(0,str.length()-1);
			}
		}
		dao.addenableFile(ywzj, str, logonUser);
	}
	
	
	/**
	 * 根据业务主键删除上传文件
	 * @param fjid
	 * @param logonUser
	 * @throws SQLException
	 */
	public static void delfileUploadByBusiness(String fjid,AclUserBean logonUser) throws SQLException
	{
		FileUpLoadDAO dao = new FileUpLoadDAO();
		if(fjid == null || "".equals(fjid)) return;
		dao.delUploadFile(fjid, logonUser);
	}
	/**
	 * 根据业务主键批量删除上传文件
	 * @param fjid
	 * @param logonUser
	 * @author add by subo
	 * @throws SQLException
	 */
	public static void delAllFilesUploadByBusiness(String ywzj,String[] uploadFjid)throws SQLException
	{
		String fileIds = "";
		FileUpLoadDAO dao = new FileUpLoadDAO();
		if(uploadFjid!=null&&uploadFjid.length>0){
			for(int i=0;i<uploadFjid.length;i++){
				fileIds += uploadFjid[i]+",";
			}
			if(fileIds!=null&&!"".equals(fileIds)){
				fileIds = fileIds.substring(0,fileIds.length()-1);
			}
			//modify by zhaolunda 2010-08-02
			dao.delAllUploadFiles(ywzj,fileIds);
			//modify end 
		}else{
			dao.delAllUploadFiles(ywzj,null);
		}
	}
	
	/**
	 * 根据业务主键批量删除上传文件
	 * @param fjid
	 * @param logonUser
	 * @author add by subo
	 * @throws SQLException
	 */
	public static void delAllFilesUploadByBusiness(String ywzj,String uploadFjid)throws SQLException
	{
		String fileIds = "";
		FileUpLoadDAO dao = new FileUpLoadDAO();
		if(!StringUtil.isEmpty(uploadFjid)){
			fileIds = uploadFjid.substring(0,uploadFjid.length()-1);
			dao.delAllUploadFiles1(ywzj,fileIds);
		}else{
			dao.delAllUploadFiles1(ywzj,null);
		}
	}
}
