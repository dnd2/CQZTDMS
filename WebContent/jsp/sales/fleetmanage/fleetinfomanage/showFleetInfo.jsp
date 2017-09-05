<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集团客户信息报备</title>
</head>
<body onload="__extQuery__(1)">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />集团客户信息</div>
 <form method="post" name = "fm" >
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		  <tr>
		    <td class="table_query_2Col_label_4Letter">客户名称：</td>
		    <td><input type='text'  class="middle_txt" name="fleetName"  id="fleetName" datatype="1,is_name,15" value=""/></td>
		    <td class="table_query_2Col_label_4Letter">客户类型：</td>
		    <td><script type="text/javascript">
            	genSelBoxExp("fleetType",<%=Constant.FLEET_TYPE%>,"",true,"short_sel","","true",'');
                </script>
		    </td>
	      </tr>
		  <tr>
		    <td><div align="right">
        	<input name="searchBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查询" />
      		</div></td>
	      </tr>
     </table> 
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetInfoApplication/showFleetList.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'FLEET_ID',renderer:mySelect,align:'center'},
				{header: "客户名称",dataIndex:'FLEET_NAME',align:'center'},
				{header: "区域", dataIndex: 'REGION', align:'center',renderer:getRegionName},
				{header: "客户类型", dataIndex: 'FLEET_TYPE', align:'center',renderer:getItemValue},
				{header: "主要联系人", dataIndex: 'MAIN_LINKMAN', align:'center'},
				{header: "主要联系人电话", dataIndex: 'MAIN_PHONE', align:'center'},
				{header: "主要联系人职务", dataIndex: 'MAIN_JOB', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "提报人员", dataIndex: 'SUBMIT_USER', align:'center'},
				{header: "报备时间", dataIndex: 'SUBMIT_DATE', align:'center'}
		      ];
		      
//设置超链接  begin      
	
	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setFleetInfo(\""+record.data.FLEET_NAME+"\",\""+record.data.FLEET_TYPE+"\",\""+record.data.REGION+"\",\""+record.data.MAIN_BUSINESS+"\",\""+record.data.FUND_SIZE+"\",\""+record.data.STAFF_SIZE+"\",\""+record.data.PURPOSE+"\",\""+record.data.ZIP_CODE+"\",\""+record.data.ADDRESS+"\",\""+record.data.MAIN_LINKMAN+"\",\""+record.data.MAIN_PHONE+"\",\""+record.data.MAIN_JOB+"\",\""+record.data.MAIN_EMAIL+"\",\""+record.data.OTHER_LINKMAN+"\",\""+record.data.OTHER_PHONE+"\",\""+record.data.OTHER_JOB+"\",\""+record.data.OTHER_EMAIL+"\",\""+record.data.STATUS+"\",\""+record.data.SUBMIT_USER+"\",\""+record.data.SUBMIT_DATE+"\")' />");
	}
	
	// 将查询的值传给父窗口
	function setFleetInfo(fleetName,fleetType,region,mainBusiness,fundSize,staffSize,purpose,zipCode,address,mainLinkman,mainPhone,mainJob,mainEmail,otherLinkman,otherPhone,otherJob,otherEmail,status,submitUser,submitDate){
	     
		 //调用父页面方法
		 if(fleetName==null||fleetName=="null"){
		 	fleetName = "";
		 }
		 if(fleetType==null||fleetType=="null"){
		 	fleetType = "";
		 }
		 if(region=="null"||region==null){
		 	region = "";
		 }
		 if(mainBusiness==null||mainBusiness=="null"){
		 	mainBusiness = "";
		 }
		 if(fundSize=="null"||fundSize==null){
		 	fundSize = "";
		 }
		 if(staffSize==null||staffSize=="null"){
		 	staffSize = "";
		 }
		 if(purpose==null||purpose=="null"){
		 	purpose = "";
		 }
		 if(zipCode==null||zipCode=="null"){
		 	zipCode = "";
		 }
		 if(address==null||address=="null"){
		 	address = "";
		 }
		 if(mainLinkman==null||mainLinkman=="null"){
		 	mainLinkman = "";
		 }
		 if(mainPhone==null||mainPhone=="null"){
		 	mainPhone = "";
		 }
		 if(mainJob==null||mainJob=="null"){
		 	mainJob = "";
		 }
		 if(mainEmail==null||mainEmail=="null"){
		 	mainEmail = "";
		 }
		 if(otherLinkman==null||otherLinkman=="null"){
		 	otherLinkman = "";
		 }
		 if(otherPhone==null||otherPhone=="null"){
		 	otherPhone = "";
		 }
		 if(otherJob==null||otherJob=="null"){
		 	otherJob = "";
		 }
		 if(otherEmail==null||otherEmail=="null"){
		 	otherEmail = "";
		 }
		 if(submitUser==null||submitUser=="null"){
		 	submitUser = "";
		 }
		 if(submitDate==null||submitDate=="null"){
		 	submitDate = "";
		 }
		 if(status==null||status=="null"){
		 	status = "";
		 }
 		parent.$('inIframe').contentWindow.fetchFleetInfo(fleetName,fleetType,region,mainBusiness,fundSize,staffSize,purpose,zipCode,address,mainLinkman,mainPhone,mainJob,mainEmail,otherLinkman,otherPhone,otherJob,otherEmail,status,submitUser,submitDate);
 		//关闭弹出页面
 		_hide();
	}
	
	
	
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>
