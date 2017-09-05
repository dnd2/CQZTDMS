<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@ page import="com.infodms.dms.dao.claim.specialExpenses.SpecialExpensesManageDao"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>特殊费用详细</title>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 特殊费用详细</div>
 <form method="post" name = "fm" >
 
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <tr>
          <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
          </tr>
		  <tr>
		    <td class="table_info_3col_label_6Letter">单据号码：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.FEE_NO}"/>
		    </td>
		    <td class="table_info_3col_label_6Letter">经销商代码：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.DEALER_CODE}"/>
		    </td>
		    <td class="table_info_3col_label_6Letter">经销商名称：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.DEALER_SHORTNAME}"/>
		    </td>
	      </tr>
		  <tr>
		    <td class="table_info_3col_label_6Letter">制单日期：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.CREATE_DATE}"/>
		    </td>
		    <td class="table_info_3col_label_6Letter">结算厂家：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${areaName}"/>
		    </td>
		    <td class="table_info_3col_label_6Letter">结算金额(元)：</td>
		    <td class="table_info_3col_input">
				<script type='text/javascript'>
					var name=amountFormat('<c:out value="${map.DECLARE_SUM}"/>');
					document.write(name) ;
				</script> 
			</td>
	      </tr>
		  <tr>
		    <td class="table_info_3col_label_6Letter">VIN：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.VIN}"/>
		    </td>
		    <td class="table_info_3col_label_6Letter">车型：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.V_MODEL}"/>
		    </td>
		    <td class="table_info_3col_label_6Letter">申报金额：</td>
		    <td class="table_info_3col_input">
		    				<script type='text/javascript'>
					var name=amountFormat('<c:out value="${map.DECLARE_SUM1}"/>');
					document.write(name) ;
				</script>
		    </td>
	      </tr>
		  <tr>
		    <td class="table_info_3col_label_6Letter">联系人：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${map.LINKMAN}"/>
		    </td>
            <td class="table_info_3col_label_6Letter">联系人电话：</td>
            <td class="table_info_3col_input">
				<c:out value="${map.LINKMAN_TEL}"/>
            </td>
            <td class="table_info_3col_label_6Letter">费用类型：</td>
            <td class="table_info_3col_input">
            	<script type='text/javascript'>
					var name=getItemValue('<c:out value="${map.FEE_TYPE}"/>');
					document.write(name);
				</script>
            </td>
          </tr>
          
          <tr>
		    <td class="table_info_3col_label_6Letter">结算费用类型：</td>
		    <td class="table_info_3col_input">
		    	<script type='text/javascript'>
					var name=getItemValue('<c:out value="${map.BALANCE_FEE_TYPE}"/>');
					document.write(name);
				</script>
		    </td>
            <td class="table_info_3col_label_6Letter">主因件编码：</td>
            <td class="table_info_3col_input">
				<c:out value="${map.PART_CODE}"/>
            </td>
            <td class="table_info_3col_label_6Letter">主因件名称：</td>
            <td class="table_info_3col_input">
				<c:out value="${map.PART_NAME}"/>
            </td>
           
          </tr>
	           <td class="table_info_3col_label_6Letter">制造商名称：</td>
	            <td class="table_info_3col_input">
	            	<c:out value="${map.SUPPLIER_NAME}"/>
	            </td>
	           <td class="table_info_3col_label_6Letter">索赔单号：</td>
	            <td class="table_info_3col_input">
	            	<c:out value="${map.CLAIM_NO}"/>
	            </td>
          <tr>
            
          </tr>
          <tr>
            <td class="table_info_3col_label_6Letter">备注：</td>
			<td colspan="5" align="left" valign="top" nowrap="nowrap" class="table_info_input_all">
				<c:out value="${map.APPLY_CONTENT}"/>
      		</td>
          </tr>
     </table>   
		<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	 		<tr>
		 		<th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 特殊费用审核明细</th>
	 		</tr>
	 		<tr>
		 		<td>审核人</td>
		 		<td>审核时间</td>
		 		<td>审核状态</td>
		 		<td colspan="3">审核内容</td>
	 		</tr>
	 		<c:forEach items="${list }" var="t">
	 			<tr>
	 				<td>${t.AUDITING_PERSON }</td>
	 				<td>${t.AUDITING_DATE }</td>
	 				<td>${t.STATUS }</td>
	 				<td>${t.AUDITING_OPINION }</td>
	 			</tr>
	 		</c:forEach>
	 </table>
<!-- 展示附件 开始-->
  <table class="table_info" border="0" id="file">
    <tr colspan="8">
        <th>
		<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
		&nbsp;附件列表：
		</th>
		<th><span align="left"></span>
		</th>
	</tr>
	<tr>
		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp"/></td>
	</tr>
	<%for(int i=0;i<fileList.size();i++) { %>
	  <script type="text/javascript">
    		addUploadRowByDbView('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
    	</script>
	<%}%>
  </table>
<!-- 展示附件 结束-->
    <table class="table_list">
       <tr > 
         <th height="12" align=center>
           <div>
	           <input type="button" onclick="_hide();" class="normal_btn" style="width=8%" value="关闭"/>
	           <input class="normal_btn" type="hidden" value="历史信息" name="historyBtn"
		 onclick="openWindowDialog('<%=contextPath%>/claim/specialExpenses/SpecialExpensesManage/maintaimHistory.do?VIN=${map.VIN}');"/>
           </div>
       </tr>
     </table>    
</form>
<script type="text/javascript">
function openWindowDialog(targetUrl){
	  var height = 500;
	  var width = 800;
	  var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
	  var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
	  var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
	  window.open(targetUrl,null,params);
}
</script>
</body>
</html>
