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
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;车辆信息变更申请
<form name="fm" id="fm">
	<table class="table_edit" id="vehicleInfo">
		<th colspan="6"><img class="nav" src="../../../img/subNav.gif"/> 车辆信息</th>
		<input type="hidden" id="id" value="${map.ID}"/>
		<tr>
			<td align="right">VIN：</td>
			<td><c:out value="${map.VIN}"/></td>
			<td align="right">发动机号：</td>
			<td><c:out value="${map.ENGINE_NO}"/></td>
			<td align="right">牌照号：</td>
			<td><c:out value="${map.VEHICLE_NO}"/></td>
		</tr>
		<tr>
			<td align="right">产地：</td>
			<td><c:out value="${map.YIELDLY}"/></td>
			<td align="right">车系：</td>
			<td><c:out value="${map.SERIES_NAME}"/></td>
			<td align="right">车型：</td>
			<td><c:out value="${map.MODEL_NAME}"/></td>
		</tr>
		<tr>
			<td align="right">购车日期：</td>
			<td><c:out value="${map.PURCHASED_DATE}"/></td>
			<td align="right">行驶里程：</td>
			<td><c:out value="${map.MILEAGE}"/></td>
			<td align="right">保养次数：</td>
			<td><c:out value="${map.FREE_TIMES}"/></td>
		</tr>
		<tr>
			<td align="right">车主姓名：</td>
			<td><c:out value="${map.CTM_NAME}"/></td>
			<td align="right">车主电话：</td>
			<td><c:out value="${map.MAIN_PHONE}"/></td>
			<td align="right">三包策略代码：</td>
			<td><c:out value="${map.GAME_CODE}"/></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">三包规则代码：</td>
			<td><c:out value="${map.RULE_CODE}"/></td>
		</tr>
		<tr>
			<td align="right">车主地址：</td>
			<td><c:out value="${map.ADDRESS}"/></td>
		</tr>
		<th colspan="6"><img class="nav" src="../../../img/subNav.gif" />申请内容</th>
		<c:if test="${map.APPLY_ID == 13141001}"><!-- 如果申请类型是行驶里程变更 -->
		<tbody>
        <tr>
			<td align="right">申请类型：</td>
		  	<td align="left"><c:out value="${map.APPLY_TYPE}"/></td>
		  	<td align="right">申请时间：</td>
		  	<td align="left">${map.CREATE_DATE}</td>
		</tr>
        <tr>
        	<td align="right">提报错误单据：</td>
       		<td align="left"><c:out value="${map.ERROR_RO_CODE}"/></td>
       		<td align="right">变更后数据：</td>
       		<td align="left"><c:out value="${map.APPLY_DATA}"/></td>
       		<td></td>
       		<td></td>
       	</tr>
        <tr>
          <td align="right">经销商名称：</td>
          <td align="left" id="roDealerCode"><c:out value="${map.DN}"/></td>
          <td align="right">单据里程：</td>
          <td align="left" id="roMileage"><c:out value="${map.RO_MILEAGE}"/></td>
          <td align="right">单据保养次数：</td>
          <td align="left" id="roFreeTimes"><c:out value="${map.RO_FREE_TIMES}"/></td>
        </tr>
        <tr>
          <td align="right" nowrap="nowrap">可变更最小里程：</td>
          <td align="left" id="cInMileage"><c:out value="${map.C_MILEAGE}"/></td>
          <!-- 
          <td align="right">可变更保养次数：</td>
          <td align="left" id="cFreeTimes"><c:out value="${map.C_FREE_TIMES}"/></td>
           -->
          <td></td>
          <td></td>
        </tr>
        </tbody>
        </c:if><!-- 行驶里程变更结束 -->
        
        <c:if test="${map.APPLY_ID == 13141002}"><!-- 如果申请类型是保养次数变更 -->
		<tbody>
        <tr>
			<td align="right">申请类型：</td>
		  	<td align="left"><c:out value="${map.APPLY_TYPE}"/></td>
		  	<td align="right">申请时间：</td>
		  	<td align="left">${map.CREATE_DATE}</td>
		</tr>
        <tr>
        	<td align="right">提报错误单据：</td>
       		<td align="left"><c:out value="${map.ERROR_RO_CODE}"/></td>
       		<td align="right">变更后数据：</td>
       		<td align="left"><c:out value="${map.APPLY_DATA}"/></td>
       		<td></td>
       		<td></td>
       	</tr>
        <tr>
          <td align="right">经销商名称：</td>
          <td align="left" id="roDealerCode"><c:out value="${map.DN}"/></td>
          <td align="right">单据里程：</td>
          <td align="left" id="roMileage"><c:out value="${map.RO_MILEAGE}"/></td>
          <td align="right">单据保养次数：</td>
          <td align="left" id="roFreeTimes"><c:out value="${map.RO_FREE_TIMES}"/></td>
        </tr>
        <tr>
        <!-- 
          <td align="right" nowrap="nowrap">可变更最小里程：</td>
          <td align="left" id="cInMileage"><c:out value="${map.C_MILEAGE}"/></td>
        -->
          <td align="right">可变更保养次数：</td>
          <td align="left" id="cFreeTimes"><c:out value="${map.C_FREE_TIMES}"/></td>
          <td></td>
          <td></td>
        </tr>
        </tbody>
        </c:if><!-- 行驶里程变更结束 -->	
        <!-- 购车日期变更 开始 -->
        <c:if test="${map.APPLY_ID == 13141003}">
		<tbody id="purchase_date_chg">
		 	<tr>
				<td align="right">申请类型：</td>
		  		<td align="left"><c:out value="${map.APPLY_TYPE}"/></td>
		  		<td align="right">申请时间：</td>
		  		<td align="left">${map.CREATE_DATE}</td>
			</tr>
			<tr>
				<td align="right">变更后的日期：</td>
				<td>
					<fmt:formatDate value='${map.C_PURCHASE_DATE}' pattern='yyyy-MM-dd'/>
				</td>
			</tr>
		</tbody> 
		</c:if>  
		<!-- 购车日期变更 结束 -->
		<!-- 车主信息变更 开始 -->
		<c:if test="${map.APPLY_ID == 13141005}">
		<tbody id="ctm_info_chg">
			<tr>
				<td align="right">申请类型：</td>
		  		<td align="left"><c:out value="${map.APPLY_TYPE}"/></td>
		  		<td align="right">申请时间：</td>
		  	<td align="left">${map.CREATE_DATE}</td>
			</tr>
			<tr>
				<td align="right">用户姓名：</td>
				<td>${map.C_CTM_NAME }</td>
				<td align="right">用户电话：</td>
				<td>${map.C_CTM_PHONE }</td>
			</tr>
			<tr>
				<td align="right">用户地址：</td>
				<td colspan="3">${map.C_CTM_ADDRESS }</td>
			</tr>
		</tbody>  
		</c:if> 
		<!-- 车主信息变更 结束 -->  	
        <tbody>
        <tr>
			<td align="right">备注：</td>
			<td colspan="5" align="left"><textarea id="remark" name="remark" style="width: 95%" rows="3" readonly><c:out value="${map.APPLY_REMARK}"/></textarea></td>
		</tr>
		</tbody>
	</table>
	<c:if test="${map.APPLY_ID != 13141001 && map.APPLY_ID != 13141002}"> <!-- 行驶里程变更和保养次数变更没有审核 -->
		<table class="table_edit" id="">
			<th colspan="9" align="left"><img class="nav" src="../../../img/subNav.gif" />审核内容</th>
			<tr>
			  <td align="right" nowrap="nowrap">审批备注：</td>
			  <td colspan="6" align="left"><textarea name="checkRemark" id="checkRemark" cols="120" rows="3" readonly><c:out value="${map.CHECK_REMARK}"/></textarea></td>
		  	</tr>
		</table>
	</c:if>
	<!-- 添加附件 开始  -->
        <table id="add_file" style="display:none" width="100%" class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
	    		<tr>
	        		<th>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
					</th>
				</tr>
				<tr>
    				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  					<%if(fileList!=null){for(int i=0;i<fileList.size();i++) { %>
	 					 <script type="text/javascript">
	 					showUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>');
	 	 				</script>
					<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
		</br>
		<table width=100% border="0" cellspacing="0" cellpadding="0">
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
