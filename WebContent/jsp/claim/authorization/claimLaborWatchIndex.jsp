<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	List levellist = (List)request.getAttribute("LEVELLIST");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>监控工时维护</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;监控工时维护</div>
<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>

  <form name='fm' id='fm'>
  <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
  <table class="table_query">
       <tr>            
        <td style="text-align: right">工时代码：</td>            
        <td>
			<input  class="middle_txt" id="LABOUR_OPERATION_NO"maxlength="25"    name="LABOUR_OPERATION_NO" type="text" datatype="1,is_null,20"/>
        </td>
        <td style="text-align: right">工时名称：</td>
        <td><input type="text" name="LABOUR_OPERATION_NAME" maxlength="31"  id="LABOUR_OPERATION_NAME" datatype="1,is_null,30" class="middle_txt" value=""/></td>
		<td style="text-align: right">索赔工时车型组：</td>            
        <td>
		   <script type="text/javascript">
		      var wrmodelgrouplist=document.getElementById("wrmodelgrouplist").value;
    	      var str="";
    	      str += "<select id='WRGROUP_ID' name='WRGROUP_ID'  class='u-select'>";
    	      str+=wrmodelgrouplist;
    		  str += "</select>";
    		  document.write(str);
	       </script>
        </td>
       </tr>
       <tr>            
        <td style="text-align: right">状态：</td>            
        <td>
			<select class="u-select" id="selDelFlag" name="selDelFlag">
				<option value="0" selected>有效</option>
				<option value="1">无效</option>
			</select>
        </td>
        <td></td>
        <td colspan="3"></td>
       </tr>
       <tr>
        <td colspan="6" style="text-align: center">
        	    <input  id="queryBtn" class="normal_btn" type="button" name="queryBtn" value="查询"  onclick="__extQuery__(1)"/>
				<input class="normal_btn" type="button" name="button1" value="新增"  onclick="add();"/>
        </td>
       </tr>
 	</table>
 	</div>
 	</div>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  </div>
  </body>
</html>
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/authorization/ClaimLaborWatchMain/claimLaborWatchQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "序号",align:'center', renderer:getIndex, width:'7%'},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
				{header: "车型组",sortable: false,dataIndex: 'WRGROUP_CODE',align:'center'},
				{header: "工时代码",sortable: false,dataIndex: 'LABOUR_OPERATION_NO',align:'center'},
				{header: "工时名称",sortable: false,dataIndex: 'LABOUR_OPERATION_NAME',align:'center'},		
       <% for(int i=0;i<levellist.size();i++){ 
    	 HashMap temp = (HashMap)levellist.get(i);
		  %>
		  	{header: "<%=temp.get("APPROVAL_LEVEL_NAME")%>",sortable: false,dataIndex: "<%=temp.get("APPROVAL_LEVEL_CODE")%>",align:'center',renderer:myCheckbox},
		  	
		  <%
   			 }	%>	
   			{header: "状态",sortable: false,dataIndex: 'IS_DEL',renderer:statusLink ,align:'center'}
		      ];
//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
		var labelDelFlag = record.data.IS_DEL ;
		if(labelDelFlag == 0)
		    return String.format(
	         "<a href='#' onclick='updateIt(" + value + ")'>[修改]</a><a  href=\"#\" onclick='sel(\""+value+"\",\"" + labelDelFlag + "\")'>[无效]</a>");
		else 
			return String.format(
					"<a href='#' onclick='updateIt(" + value + ")'>[修改]</a><a  href=\"#\" onclick='sel(\""+value+"\",\"" + labelDelFlag + "\")'>[有效]</a>");
	}
	
	function statusLink(value, meta, record) {
		var labelStatus = "" ;
		
		if(value == 0) {
			labelStatus = "有效" ;
		} else {
			labelStatus = "无效" ;
		} ;
		
		return labelStatus ;
	}
	
	function updateIt(value) {
		var url = "<%=contextPath%>/claim/authorization/ClaimLaborWatchMain/claimLaborWatchUpdateInit.do?ID=" + value;
		OpenHtmlWindow(url, 800, 420, '监控工时修改');
		
		/* fm.action = url ;
		fm.submit() ; */
	}
	//checkbox 格式化方法：
    function myCheckbox(value,meta,record){
        if(value != ''){
		    return String.format(
	         "<input type='checkbox' name='box1' id='box1' value='"+value+"' checked='true' disabled='disabled'/>");
        }else{
        	return String.format(
	         "<input type='checkbox' name='box1' id='box1' value='"+value+"' disabled='disabled'/>");
        }
	}
	//删除方法：
	function sel(str, labelDelFlag){
		MyConfirm("是否修改状态？",del,[str, labelDelFlag]);
	}  
	//删除
	function del(str, labelDelFlag){
		makeNomalFormCall('<%=contextPath%>/claim/authorization/ClaimLaborWatchMain/claimLaborWatchDel.json?ID='+str +'&labelDelFlag=' + labelDelFlag,delBack,'fm','');
	}
	//删除回调方法：
	function delBack(json) {
		if(json.success != null && json.success == "true") {
			MyAlert("状态修改成功！");
			__extQuery__(1);
		} else {
			MyAlert("操作失败！请联系管理员！");
		}
	}	
	
	
//设置超链接 end
  //新增
  function add(){
    fm.action ="<%=contextPath%>/claim/authorization/ClaimLaborWatchMain/claimLaborWatchAddInit.do";
    fm.submit();    
  }
</script>  

