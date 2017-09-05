<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>免费保养模板</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<!--页面列表 begin -->

<script type="text/javascript" >

var myPage;
//查询路径
var url = "<%=contextPath%>/MainTainAction/keepFitTemplate.json?query=true";
			
var title = null;

var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "模板编号", width:'15%', dataIndex: 'KEEP_FIT_NO'},
				{header: "模板名称", width:'15%', dataIndex: 'KEEP_FIT_NAME'},
				{header: "车型", width:'15%', dataIndex: 'MODEL_NAME'},
				{header: "配置代码", width:'15%', dataIndex: 'PACKAGE_CODE'},
				{header: "总费用", width:'15%', dataIndex: 'KEEP_FIT_AMOUNT'},
				{header: "模版类型", width:'15%', dataIndex: 'CHOOSE_TYPE',renderer:getItemValue},
				{header: "创建时间", width:'15%', dataIndex: 'CREATE_DATE'},
				{header: "状态", width:'15%', dataIndex: 'STATUS',renderer:getItemValue},
				{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink,align:'center'}
	      ];
	      function myLink(value,meta,record){
		    var status=record.data.STATUS;
		  	var url="";
		    if(status==18041001){//未发布
		    	var urlUpdate="<%=contextPath%>/MainTainAction/updateInit.do?id="+value;
		    	url+="<a href='"+urlUpdate+"'>[修改]</a>";
		    	url+="<a href='#' onclick='pulish(this,\""+ value +"\")';'>[下发]</a>";
		    	url+="<a href='#' onclick='del(this,\""+ value +"\")';'>[作废]</a>";
		    }
		    if(status==18041002){//未发布
		    	url+="<a href='#' onclick='del(this,\""+ value +"\")';'>[作废]</a>";
		    }
	    	var urlView="<%=contextPath%>/MainTainAction/view.do?id="+value;
		    url+="<a href='"+urlView+"'>[明细]</a>";
	    	
	        return String.format(url);
	      }
	      var id_del="";
	      var vin_del="";
	      function pulish(obj,id){
	    	  id_del=id;
	    	  MyConfirm("是否确认下发？",pulishsubmit,"");
	      }
	      function del(obj,id){
	    	  id_del=id;
	    	  MyConfirm("是否确认作废？",delsubmit,"");
	      }
	      function pulishsubmit(){
	    	  sendAjax('<%=contextPath%>/MainTainAction/publish.json?id='+id_del,backpulish,'fm');
	      }
	      function backpulish(json){
	    	  if(json.succ=="1"){
	    		  MyAlert("提示：发布成功！");
	    		  __extQuery__(1);
	    	  }else{
	    		  MyAlert("提示："+json.msg);
	    	  }
	      }
	      function delsubmit(){
	    	  sendAjax('<%=contextPath%>/MainTainAction/deleteMainTain.json?id='+id_del,backDel,'fm');
	      }
	      function backDel(json){
	    	  if(json.succ=="1"){
	    		  MyAlert("提示：作废成功！");
	    		  __extQuery__(1);
	    	  }else{
	    		  MyAlert("提示："+json.msg);
	    	  }
	      }
	      function add(){
	    	  window.location.href='<%=contextPath%>/MainTainAction/addMainTain.do';
	      }
</script>
<!--页面列表 end -->
</head>
<body >
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔基础数据&gt;免费保养模板查询
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">模板编号：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="keep_fit_no"  name="keep_fit_no" maxlength="30" type="text"/>
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">模板名称：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="keep_fit_name"  name="keep_fit_name" maxlength="30" type="text"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter">模板状态：</td>
		<td width="15%" nowrap="true">
			  <script type="text/javascript">
		       genSelBoxExp("status",<%=Constant.OLD_PART_BORROW%>,"",true,"short_sel","","false",'18041003');
		    </script>
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">配置：</td>
      	<td width="15%" nowrap="true">
        <select name="package_code" id="package_code">
      	    <option value="">-请选择-</option>
      	<c:forEach items="${listpo}" var="li">
      	    <option value="${li.PACKAGE_CODE}">${li.PACKAGE_NAME }</option>
      	</c:forEach>
        </select>
<!--    	    <change:select name="package_code"  noTop="true" value="${t.PACKAGE_CODE }"  fieldCode="package_code" fieldName="package_name"  style="short_sel" sql="select g.package_code,g.package_name from vw_material_group g group by g.package_code,g.package_name"/>-->
      	</td>
        <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">车型：</td>
      	<td width="15%" nowrap="true">
      		<input class="middle_txt" id="model_name"  name="model_name" maxlength="30" type="text"/>
      	</td>
		<td width="10%" nowrap="true" class="table_query_2Col_label_5Letter"></td>
		<td width="15%" nowrap="true">
		</td>
		<td width="12.5%"></td>
	</tr>
	<tr>
    	<td align="center" colspan="8">
    		<input type="button"  name="bntAdd"  id="bntAdd"  value="新增" onclick="add();" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="queryBtn" value="查询" class="normal_btn" onClick="__extQuery__(1);"/>
    		&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->

<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
</html>