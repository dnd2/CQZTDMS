/**********************************************************************
 * <pre>
 * FILE : Picture.java
 * CLASS : Picture
 *
 * AUTHOR : SuMMeR
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		  |2009-8-18| SuMMeR| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
/**
 * $Id: File.java,v 1.1 2010/08/16 01:42:29 yuch Exp $
 */

package com.infodms.dms.bean;

/**
 * 文件类
 * 
 * @author SuMMeR
 */
public class File
{

	// 文件ID
	private String fileId;

	// 文件URL
	private String fileUrl;

	// 文件类型
	private String picType;

	public String getFileId()
	{
		return fileId;
	}

	public void setFileId(String fileId)
	{
		this.fileId = fileId;
	}

	public String getFileUrl()
	{
		return fileUrl;
	}

	public void setFileUrl(String fileUrl)
	{
		this.fileUrl = fileUrl;
	}

	public String getPicType()
	{
		return picType;
	}

	public void setPicType(String picType)
	{
		this.picType = picType;
	}
}
