<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>   
<%@taglib uri="/jstl/cout" prefix="c" %>
<%String contextPath = request.getContextPath();%>
<%List<Map<String, Object>> claimSit =(List<Map<String, Object>>)request.getAttribute("claimSit"); %>
<%List<Map<String, Object>> detail =(List<Map<String, Object>>)request.getAttribute("detail"); %>
<%List<Map<String, Object>> detail1 =(List<Map<String, Object>>)request.getAttribute("detail1"); %>
<%List<Map<String, Object>> detail2 =(List<Map<String, Object>>)request.getAttribute("detail2"); %>
<%List<Map<String, Object>> detail3 =(List<Map<String, Object>>)request.getAttribute("detail3"); %>
<%List<Map<String, Object>> detail4 =(List<Map<String, Object>>)request.getAttribute("detail4"); %>
<%List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList"); %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>抽查单逐条审核</title>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 索赔结算管理 &gt; 经销商抽查审核 &gt; 逐条审核</div>
<form method="post" name="fm" id="fm" >
<!-- 根据查询条件逐条审核 -->
<input type="hidden" name="id" value="${id }" />
<input type="hidden" name="claimId" value="${claimId }" />
<input type="hidden" name="count" value="${count }" />
<input type="hidden" name="checkId" value="${id }" />
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <tr>
          <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
          </tr>
		  <tr>
		    <td class="table_info_3col_label_6Letter">vin：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${list.VIN}"/>
		    </td>
		    <td class="table_info_3col_label_6Letter">车型：</td>
		    <td class="table_info_3col_input">
		    	${list.GROUP_CODE }
		    </td>
		    <td class="table_info_3col_label_6Letter">索赔单类型：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${list.CLAIM_TYPE}"/>
		    </td>
	      </tr>
		  <tr>
		    <td class="table_info_3col_label_6Letter">索赔单号：</td>
		    <td class="table_info_3col_input">
		    	<c:out value="${list.CLAIM_NO}"/>
		    </td>
		    <td class="table_info_3col_label_6Letter">金额：</td>
		    <td class="table_info_3col_input">
		    	${list.BALANCE_AMOUNT }
		    </td>
		    <td class="table_info_3col_label_6Letter">是否上传附件：</td>
		    <c:if test="${list.UPFILE_STATUS==91051002 }">
		    <td class="table_info_3col_input">
		    	已上传
		    </td></c:if>
		    <c:if test="${list.UPFILE_STATUS==91051001 }">
		    <td class="table_info_3col_input">
		    	未上传
		    </td></c:if>
	      </tr>
     </table>   
	
	 <table class="table_info" border="0" id="file">
    <tr colspan="8">
        <th>
		<img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;附件列表：
		</th>
		<th><span align="left"></span>
		</th>
	</tr>
	<tr>
		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp"/></td>
	</tr>
	<%for(int i=0;i<fileList.size();i++) { %>
	  <script type="text/javascript">
	  showUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>');
	  </script>
	<%}%>
  </table>
	 
	 <table><tr><td align="left">委托书</td></tr></table>
	 <table class="table_list">
		<tr class="table_list_row2">
			<td><input type="checkBox" id="checkBoxAll" name="checkBoxAll" onclick="selectAll(this,'recesel')" />选择</td>
			<td>委托书名称</td>
		</tr>
		<%
			for(int i=0;i<detail.size();i++){
				Boolean flag=false;
				for (int j=0;j<claimSit.size();j++){
					if(detail.get(i).get("CODE_ID").toString().equals(claimSit.get(j).get("CHECK_SITUATION").toString())){
						flag = true ;
					}
					
				}
				if(flag){
				%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel" name="recesel" value="<%=detail.get(i).get("CODE_ID") %>" checked/></td>
						<td><%=detail.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel" name="recesel" value="<%=detail.get(i).get("CODE_ID") %>" 
						/></td>
						<td><%=detail.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}
			}
		%>
	</table>
	<table><tr><td align="left">配件领料单</td></tr></table>
	<table class="table_list">
		<tr class="table_list_row2">
			<td><input type="checkBox" id="checkBoxAll1" name="checkBoxAll1" onclick="selectAll(this,'recesel1')" />选择</td>
			<td>配件领料单名称</td>
		</tr>
		<%
			for(int i=0;i<detail1.size();i++){
				Boolean flag=false;
				for (int j=0;j<claimSit.size();j++){
					if(detail1.get(i).get("CODE_ID").toString().equals(claimSit.get(j).get("CHECK_SITUATION").toString())){
						flag = true ;
					}
				}
				if(flag){
				%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel1" name="recesel1" value="<%=detail1.get(i).get("CODE_ID") %>" checked/></td>
						<td><%=detail1.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel1" name="recesel1" value="<%=detail1.get(i).get("CODE_ID") %>" 
						/></td>
						<td><%=detail1.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}
			}
		%>
	</table>
	<table><tr><td align="left">三包单据</td></tr></table>
	<table class="table_list">
		<tr class="table_list_row2">
			<td><input type="checkBox" id="checkBoxAll2" name="checkBoxAll2" onclick="selectAll(this,'recesel2')" />选择</td>
			<td>三包单据名称</td>
		</tr>
			<%
			for(int i=0;i<detail2.size();i++){
				Boolean flag=false;
				for (int j=0;j<claimSit.size();j++){
					if(detail2.get(i).get("CODE_ID").toString().equals(claimSit.get(j).get("CHECK_SITUATION").toString())){
						flag = true ;
					}
				}
				if(flag){
				%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel2" name="recesel2" value="<%=detail2.get(i).get("CODE_ID") %>" checked/></td>
						<td><%=detail2.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel2" name="recesel2" value="<%=detail2.get(i).get("CODE_ID") %>" 
						/></td>
						<td><%=detail2.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}
			}
		%>	

	</table>
	<table><tr><td align="left">单据合并检查</td></tr></table>
	<table class="table_list">
		<tr class="table_list_row2">
			<td><input type="checkBox" id="checkBoxAll3" name="checkBoxAll3" onclick="selectAll(this,'recesel3')" />选择</td>
			<td>单据合并检查名称</td>
		<%
			
			for(int i=0;i<detail3.size();i++){
				Boolean flag=false;
				for (int j=0;j<claimSit.size();j++){
					if(detail3.get(i).get("CODE_ID").toString().equals(claimSit.get(j).get("CHECK_SITUATION").toString())){
						flag = true ;
					}	
				}
				if(flag){
				%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel3" name="recesel3" value="<%=detail3.get(i).get("CODE_ID") %>" 
						checked/></td>
						<td><%=detail3.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel3" name="recesel3" value="<%=detail3.get(i).get("CODE_ID") %>" 
						/></td>
						<td><%=detail3.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}
			}
		%>
	</table>
	<table><tr><td align="left">其他</td></tr></table>
	<table class="table_list">
		<tr class="table_list_row2">
			<td><input type="checkBox" id="checkBoxAll4" name="checkBoxAll4" onclick="selectAll(this,'recesel4')" />选择</td>
			<td>其他栏目名称</td>
		<%
			
			for(int i=0;i<detail4.size();i++){
				Boolean flag=false;
				for (int j=0;j<claimSit.size();j++){
					if(detail4.get(i).get("CODE_ID").toString().equals(claimSit.get(j).get("CHECK_SITUATION").toString())){
						flag = true ;
					}	
				}
				if(flag){
				%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel4" name="recesel4" value="<%=detail4.get(i).get("CODE_ID") %>" 
						checked/></td>
						<td><%=detail4.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel4" name="recesel4" value="<%=detail4.get(i).get("CODE_ID") %>" 
						/></td>
						<td><%=detail4.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}
			}
		%>
	</table>
	<table><tr><td align="left">备注</td></tr></table>
	<table>
		<tr>
			<td><textarea name="remark" id="remark" rows="5" cols="100">${rem }</textarea></td>
		</tr>
	</table>
	<br />
   
   
    <table class="table_list">
       <tr > 
         <th height="12" align=center>
           <div id="a1">
	           <input type="button" onclick="audit()" id="btn1" class="long_btn" value="同意并跳转下一条"/>
	           <input type="button" onclick="pass();" class="normal_btn" value="跳过" /> 
	           <input type="button" onclick="javascript:window.location.href='<%=contextPath%>/claim/application/DealerNewKp/checkApplicationAuthInit.do'" class="normal_btn" value="返回" /> 
           </div>
       </tr>
     </table>    
</form>
<script type="text/javascript">
	function audit(){
		MyConfirm("是否确认审核同意并跳转下条!",addSpecia);
	}
	
	function addSpecia(){
		makeNomalFormCall("<%=contextPath%>/claim/application/DealerNewKp/save.json",addSpeciaBack,'fm','queryBtn'); 
	}
	
	function addSpeciaBack(json){
		var flag=json.flag;
		var checkId=json.checkId
		if(flag=='01'){
			MyAlert("操作成功!");
			$('count').value = parseInt($('count').value) + 1;
			fm.action = "<%=contextPath%>/claim/application/DealerNewKp/ztAudit.do";
			fm.submit();
		}else{
			MyAlert('操作失败,请联系管理员');
		}
	}
	//跳过审核下一条
	function pass(){
		$('count').value = parseInt($('count').value) + 1;
		fm.action = "<%=contextPath%>/claim/application/DealerNewKp/ztAudit.do";
		fm.submit();
	}
</script>
</body>
</html>
