<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map" %>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.*"%>
<%
	String contextPath = request.getContextPath();
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商维护</title>
<script type="text/javascript">
function doInit()
{  
	genLocSel('txt1','txt2','txt3','<c:out value="${map.PROVINCE_ID}"/>','<c:out value="${map.CITY_ID}"/>','<c:out value="${map.AREA_ID}"/>'); // 加载省份城市和县
}

</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;修改经销商地址</div>
 <form method="post" name = "fm" id="fm" >
 <input id="ID" name="ID" type="hidden" value="${map.ID}"/>
  <input id="YILIE" name=YILIE type="hidden" value="${map.YIELDLY}"/>
    <input id="DEALER_ID" name=DEALER_ID type="hidden" value="${map.DEALER_ID}"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		  <tr>
		    <td class="table_query_2Col_label_6Letter">经销商代码：</td>
		    <td>${map.DEALER_CODE}</td>
		    <td class="table_query_2Col_label_6Letter">经销商名称：</td>
		    <td>${map.DEALER_NAME}</td>
	      </tr>
	     <tr>
		    <td class="table_query_2Col_label_6Letter">状态：
		    </td>
		    <td>
		   <label>
				<script type="text/javascript">
					genSelBoxExp("STATUS",<%=Constant.IF_TYPE%>,"<c:out value="${map.STATUS}"/>",'',"short_sel",'',"false",'');
				</script>
		  </label>
		    </td>
		    
		     <td class="table_query_2Col_label_6Letter">产地：
		    </td>
		    <td>
		   <label>
				<script type="text/javascript">
				document.write(getItemValue("<c:out value="${map.YIELDLY}"/>"));
				</script>
		  </label>
		    </td>
		    </tr>
		    
		    <tr>
		    <td class="table_query_2Col_label_6Letter">备注：</td>
		    <td><textarea id="remark" name="remark" rows="5" cols="40"></textarea></td>
		    <td></td>
		    <td></td>
	      </tr>
     </table> 
     
     <br/>
     <table class="table_info" border="0" id="file">
			<input type="hidden" name="fjids"/>
		<tr colspan="8">
			<th>
				<img class="nav" src="../../../img/subNav.gif" />&nbsp;附件列表：
					&nbsp;&nbsp;&nbsp;&nbsp;
				<span align="left"><input type="button" class="normal_btn"  onclick="showUpload2('<%=contextPath%>')" value ='添加附件'/></span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2">
				<jsp:include page="${contextPath}/uploadDiv.jsp" />
			</td>
		</tr>
		<%for(int i=0;i<attachLs.size();i++) { %>
    			<script type="text/javascript">
    			addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    			</script>
    	<%} %>
	</table>
	<br/>
     
     <table class=table_query>
	 <tr>
	 <td align="center">
	<input type="button" value="修改" name="saveBtn" class="normal_btn" onclick="modifyStatus()"/>	
	<input type="button" value="返回" name="cancelBtn"  class="normal_btn" onclick="history.go(-1);" /></td>
	</tr>
   </table>
   
  	<table class="table_edit">
			<tr>
				<th colspan="10">
					<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
					&nbsp;<a href="#" onclick="tabDisplayControl('showId')">变更记录</a>
				</th>
			</tr>
 	 </table>
	<table class="table_edit" id="showId" style="display: none">
			<tr>
				<td>序号</td>
				<td>变更人</td>
				<td>变更时间</td>
				<td>是否暂停结算</td>
				<td>备注</td>
			</tr>
		<c:forEach var="dtl" items="${mylist}" varStatus="vs">
			
			
			<tr class="table_list_row${vs.index%2+1}">
				<td>${vs.index+1}</td>
				<td>${dtl.NAME}</td>
				<td>${dtl.CREATE_DATE}</td>
				<td>
				<script type="text/javascript">
				document.write(getItemValue("<c:out value="${dtl.UPDATE_TYPE}"/>"));
				</script>
				</td>
				<td>${dtl.REMARK}</td>
			</tr>
		</c:forEach>
	
	</table>	
</form>

<script type="text/javascript" >
 function modifyStatus(){
	fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/dealerBSUpdate.do' ;
	fm.submit();
 }
	function tabDisplayControl(tableId){
		var tab = document.getElementById(tableId);
		if(tab.style.display=='none'){
			tab.style.display = '';
		}else{
			tab.style.display = 'none';
		}
	}
</script>

</body>
</html>
