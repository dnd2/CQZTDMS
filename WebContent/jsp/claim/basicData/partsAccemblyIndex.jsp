<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
	String reFlag = (String)request.getAttribute("REFLAG");
%>
<%@page import="java.util.List"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>
<title>总成维护</title>
</head>
<body>
<div class="wbox">
<form name='fm' id='fm'>
<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基本数据&gt;总成维护</div>
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
   <table  class="table_query">
       
          <tr>
          	<td style="text-align:right">总成代码：</td>
          	<td>
          		<input name="parts_accembly_code" type="text" id="parts_accembly_code"  class="middle_txt" />
          		<input name="coefficient " type="hidden" id="coefficient"  />
          		<input name="quantity" type="hidden" id="quantity"  />
          	</td>
          	<td style="text-align:right">总成名称：</td>
          	<td><input name="parts_accembly_name" type="text" id="parts_accembly_name"  class="middle_txt"/></td>
          	<td style="text-align:right">状态：</td>
            <td>
				 <script type="text/javascript">
	              genSelBoxExp("TYPE_CODE",<%=Constant.STATUS%>,"<%=Constant.STATUS_ENABLE%>",true,"","","",'');
	            </script>
            </td>  
          </tr>           
		   <tr>    
		   <td colspan="6" style="text-align:center">
            <input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onClick="__extQuery__(1);"/>
            <input class="normal_btn" type="button" value="失效" name="del" onclick="del2();"/>
            <input class="normal_btn" type="button" value="新增" name="add" onclick="addFoward();"/>
            
			
           </td>
      
           </tr>
       
  </table>
  </div>
  </div>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
</form>  
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/PartsAssembly/PartsAssemblyView.json?";
	var title = null;
	
	var columns = [
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\" onclick='selectAll(this,\"recesel\")' />全选", align:'center',sortable:false, dataIndex:'partsAssemblyId',width:'2%',renderer:checkBoxShow},
				{header: "操作",sortable: false,dataIndex: 'partsAssemblyId' ,align:'center' ,renderer:myLink1},
				{header: '总成代码',align:'center',dataIndex:'partsAssemblyCode'},
				{header: "总成名称",sortable: false,dataIndex: 'partsAssemblyName',align:'center'},				
				{header: "状态",sortable: false,dataIndex: 'status',align:'center',renderer:getItemValue}
		      ];

    
	function checkBoxShow(value,meta,record){
		return String.format("<input type='checkbox' id='recesel' name='recesel' value='" + record.data.partsAssemblyId + "' />");
	}

	function myLink1(value,meta,record){
		

			var rec =record.data.partsAssemblyId;
			return String.format("<a href='#' onclick='midify(" + rec + ")'>[修改]</a>");
		    /* var htmlStr="<input type=\"button\" value=\"\修改\" onclick=\"midify('"+rec+"');\" class=\"normal_btn\"/>&nbsp;";
			return String.format(htmlStr);	 */
}
	function del2() {
		var allChecks = document.getElementsByName("recesel");
		var allFlag = false;
		for(var i = 0;i<allChecks.length;i++){
			if(allChecks[i].checked){
				allFlag = true;
			}
		}
		if(allFlag){
			MyConfirm("确认失效吗?",changeSubmit);
		}else{
			MyAlert("请选择后再点击操作按钮！");
		}
	}

	function changeSubmit() {
		var url="<%=request.getContextPath()%>/claim/basicData/PartsAssembly/delPartsAssembly.json";
		makeNomalFormCall(url,delParts,'fm');
	}


	function midify(rec){
		OpenHtmlWindow("<%=contextPath%>/claim/basicData/PartsAssembly/modifyPartsAssembly.do?ID="+rec,800,400);
	}


	function delParts(json){
		var msg=json.falg;
		if(msg=='success'){
			MyAlert('失效成功！');
			__extQuery__(1);
		}else{
			MyAlert('操作失败！');
		}
	}
	function addFoward(){
		OpenHtmlWindow("<%=contextPath%>/claim/basicData/PartsAssembly/addPartsAssembly.do?",800,400);
	}

</script>
</div>
</body>
</html>