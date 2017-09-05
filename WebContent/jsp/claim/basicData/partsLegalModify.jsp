<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrOldPartSignDetailListBean"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();

%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>零件法定名称维护</title>
</head>
<body>
<div class="wbox">
<script type="text/javascript">
function save(){	
		if(submitForm('fm')){
		var url="<%=request.getContextPath()%>/claim/basicData/PartsLegal/savePartsLrgal.json";
		makeNomalFormCall(url,showResult22,'fm');
		}
		
}
function showResult22(json){
	if(json.flag='true'){
		MyAlert("修改成功");
		
		fm.action = "<%=contextPath%>/claim/basicData/PartsLegal/PartsLegalMain.do" ;
		fm.submit();
		/* parent.$('inIframe').contentWindow.__extQuery__(1);//xiongchuan添加,返回列表也同时刷新页面
		_hide(); */
	}else{
		MyAlert("修改失败");
	}
}
function delpartLegalDetail() {
	var allChecks = document.getElementsByName("recesel");
	var allFlag = false;
	for(var i = 0;i<allChecks.length;i++){
		if(allChecks[i].checked){
			allFlag = true;
		}
	}
	if(allFlag){
		MyConfirm("确认删除吗?", delDetail) ;
		/* if(confirm("确认删除吗?")){
			delDetail();
		} */
	}else{
		MyAlert("请选择后再点击操作按钮！");
	}
}

function delDetail(){
	var url="<%=request.getContextPath()%>/claim/basicData/PartsLegal/delPartsLegalDetail.json";
	makeNomalFormCall(url,delParts,'fm');
}	

function delParts(json){
	var msg=json.falg;
	if(msg=='success'){
		MyAlert('删除成功!');
		var faultId=document.getElementById("ID").value;
		fm.action = "<%=contextPath%>/claim/basicData/PartsLegal/modifyPartsLegal.do?fID=" + faultId;
		fm.submit();
	}else{
		MyAlert('操作失败!');
	}
}

function importParts(){
    var faultId=document.getElementById("ID").value;
	fm.action = "<%=contextPath%>/claim/basicData/PartsLegal/partImport.do?faultId="+faultId;
	fm.submit();
}
function addParts(){
    var faultId=document.getElementById("ID").value;
	fm.action = "<%=contextPath%>/claim/basicData/PartsLegal/addParts.do?faultId="+faultId;
	fm.submit();
}
function back(){
	 fm.action = "<%=contextPath%>/claim/basicData/PartsLegal/PartsLegalMain.do";
	 fm.method="post";
	   fm.submit();
	
}
</script>

<form name='fm' id='fm' method="post">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;零件法定名称修改</div>
  <div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
   <table  class="table_query">
       
          <tr>
          	<td style="text-align:right">零件法定代码：</td>
          	<td>
          		<input name = "ID" id="ID" type="hidden" value="${lit.PART_LEGAL_ID}" />
          		<input  name="parts_legal_code" type="text" id="parts_legal_code" value="${lit.PART_LEGAL_CODE}" datatype="0,is_null,20"  class="middle_txt" />
          	</td>
          	<td style="text-align:right">零件法定名称：</td>
          	<td><input name="parts_legal_name" type="text" id="parts_legal_name" value="${lit.PART_LEGAL_NAME}"  datatype="0,is_null,100"  class="middle_txt"/></td>
          </tr>           
          <tr>
  
          	<td style="text-align:right">所属总成：</td>
    		 <td align="left" nowrap="true">
				<select id='checkUserSel' name='checkUserSel' class='u-select'>
				<option value="${lit.PARTS_ASSEMBLY_ID}">${lit.PARTS_ASSEMBLY_NAME}</option>
				<c:if test="${!empty list}">
					<c:forEach items="${list}" var="user">
						<option value="${user.PARTS_ASSEMBLY_ID}">${user.PARTS_ASSEMBLY_NAME }</option>
					</c:forEach>
				</c:if>
				</select>
			</td> 
          	<td style="text-align:right">状态：</td>
            <td>
				 <script type="text/javascript">
	              genSelBoxExp("TYPE_CODE",<%=Constant.STATUS%>,"${lit.STATUS}",true,"","","true",'');
	            </script>
            </td>  
          </tr> 
		   <tr>    
      	<td colspan="4" style="text-align:center">
            
            <input class="normal_btn" type="button" value="保存" name="add" onclick="save();"/>
            <input class="normal_btn" type="button" value="新增配件" name="delDetail" onclick="addParts();"/>
            <input class="normal_btn" type="button" value="删除配件" name="delDetail" onclick="delpartLegalDetail();"/>
            <input class="normal_btn" type="button" value="导入配件" name="delDetail" onclick="importParts();"/>
            <input type="button"  onclick="back();" class="normal_btn" id="backId" style="" value="返回" />
           </td>
           </tr>
       
  </table>
  </div>
  </div>
  <div class="form-panel">
		<h2>配件明细</h2>
			<div class="form-body">
	<table  class="table_list" >
				<!-- <tr>
				<th align="left" colspan="3" >
					<img class="nav" src="../../../img/subNav.gif" />
					配件明细
				</th>
				</tr> -->
		<tr>
			<th style="width:7%"><input type="checkbox" id="checkBoxAll" name="checkBoxAll" onclick='selectAll(this,"recesel")' />全选</th>
			<th>配件编码</th>
			<th>配件名称</th>
		</tr>
		<c:forEach var="detailList" items="${detailList}" varStatus="num">
			<tr class="table_list_row1">
				
				<td><input type='checkbox' id='recesel' name='recesel' value="${detailList.ID}" /></td>
				<td><c:out value="${detailList.PART_CODE}"></c:out></td>
				<td><c:out value="${detailList.PART_NAME}"></c:out></td>
			</tr>
		
		 </c:forEach>
  </table>
  </div>
  </div>
  <%-- <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> --%>
</form>
</div>
</body></html>