<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); 
List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);%>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		//doCusChange();
	}
</script>
<body>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;质改跟踪详细信息

<form name="fm" id="fm">
	<table class="table_edit" id="vehicleInfo" width="100%;">
		<br/>
		<th colspan="8"><img class="nav" src="../../../img/subNav.gif"/> 车辆详细信息</th>
		<input type="hidden" id="id" value="${map.ID}"/>
		<tr>
			<td width="10%;" nowrap="true">车系：</td>
			<td width="15%;" nowrap="true"><c:out value="${map.CAR_TIE_ID}"/></td>
			<td width="10%;"nowrap="true">车型：</td>
			<td width="15%;"nowrap="true"><c:out value="${map.CAR_TYPE_ID}"/></td>
			<td width="10%;"nowrap="true">底盘号：</td>
			<td width="15%;"nowrap="true"><c:out value="${map.VIN}"/></td>
			<td width="10%;"nowrap="true"></td>
			<td width="15%;"nowrap="true"></td>
		</tr>
		<tr>
			<td nowrap="true">生产日期：</td>
			<td nowrap="true"><c:out value="${map.RO_CREATE_DATE}"/></td>
			<td nowrap="true">起始维修日期：</td>
			<td><c:out value="${map.RO_REPAIR_DATE_ONE}"/></td>
			<td nowrap="true">终止维修日期：</td>
			<td><c:out value="${map.RO_REPAIR_DATE_TWO}"/></td>
		</tr>
		<tr>
			<td nowrap="true">故障零件数：</td>
			<td><c:out value="${map.PART_NUM}"/></td>
			<td nowrap="true">故障类别代码：</td>
			<td><c:out value="${map.MAL_CODE}"/></td>
			<td nowrap="true">故障现象：</td>
			<td><c:out value="${map.MAL_NAME}"/></td>
		</tr>
		<tr>
			<td nowrap="true">零件号：</td>
			<td><c:out value="${map.PART_CODE}"/></td>
			<td nowrap="true">零件号名称：</td>
			<td><c:out value="${map.PART_NAME}"/></td>
			
		</tr>
		<tr>
			<td nowrap="true">部件厂代码：</td>
			<td><c:out value="${map.MAKER_CODE}"/></td>
			<td nowrap="true">部件厂名称：</td>
			<td><c:out value="${map.MAKER_NAME}"/></td>
		</tr>
		<tr>
			<td  nowrap="true">创建人：</td>
			<td><c:out value="${map.NAME}"/></td>
			<td >创建时间：</td>
			<td><c:out value="${map.CREATE_DATE}"/></td>
		</tr>
		<tr>
			<td  nowrap="true">备注信息：</td>
			<td colspan="6">
			 <textarea disabled="disabled" style="width: 95%;height: 80px;">${map.REMARK}</textarea>
			 </td>
		</tr>
		<table width=100% border="0"  cellspacing="0" cellpadding="0">
			<tr>
				<td height="12" align=left width="33%">&nbsp;</td>
             	<td height="12" align=center width="33%">
					<input type="button" onClick="javascript:history.go(-1);" class="normal_btn"  style="width=8%" value="返回"/>
    			</td>
            	<td height="12" align=center width="33%">
      			</td>
			</tr>
		</table>
	</form>
</body>

<script type="text/javascript">
function myBodySet(){
	var type = ${map.APPLY_ID} ;
	if(type==13141003 || type==13141005){
		$('add_file').style.display = 'block' ;
	}
}
myBodySet();
</script>
</html>
