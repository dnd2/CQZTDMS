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
<title>选择索赔单号</title>
</head>
<body onload="__extQuery__(1)">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 选择索赔单号</div>
	<form method="post" name = "fm" id="fm">
	<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>未申报索赔单</h2>
				<div class="form-body">	
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<tr>
				<td class="right">索赔单号：</td>
				<td >
					<input type="text" id="CLAIM_NO" name="CLAIM_NO"/>
				</td>
				
				<td class="right">VIN：</td>
				<td >
					<input type="text" id="VIN" name="VIN"/>
				</td>
				<td class="left" nowrap="true">
				
				<input onclick="changeCheck2()" class="normal_btn" type="button" id="Relation" value="未申报索赔单"/> 
<!--				是否关联索赔单：-->
				</td>
				<td align="left" nowrap="true"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<!--				     <input type="radio" name="Relation" checked="checked" value="yes"/>&nbsp;是&nbsp;-->
<!--				     <input type="radio" name="Relation" value="no"/>&nbsp;否&nbsp;-->
				</td>
			</tr>
	
			<tr>
				<td colspan="6 " class="center">
					<input class="u-button u-query" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
        		</td>
			</tr>
		</table>
		</div>
		</div>
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
	</div>
<script type="text/javascript">
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/vehicleInfoManage/apply/QualityReportInfo/queryVINInfoList.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择", width:'8%',sortable: false,dataIndex: 'CLAIM_NO',renderer:myCheckBox},
				{header: "索赔单号", dataIndex: 'CLAIM_NO', style: 'text-align:center'},
				{header: "VIN", dataIndex: 'VIN', style: 'text-align:center'},
				{header: "维修日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'}
		      ];
	
	function myCheckBox(value,metaDate,record){
		return String.format("<input name='radio' type='radio' onclick='changeCheck1(this,\""+ record.data.CLAIM_NO +"\",\""+ record.data.VIN +"\",\""+ record.data.CREATE_DATE +"\")'/>");
	}
	
	function doRowClick(obj){
    	if(obj.cells[0].firstChild.checked != true){
    		obj.cells[0].firstChild.checked = true;
    		var str = obj.cells[0].firstChild.value;
    		
    		changeCheck1(obj.cells[0].firstChild,str.split(',')[0],str.split(',')[1]);
    	}
    }

    function changeCheck2(){
      	var  vin= document.getElementById("VIN").value;
      	if (parent.$('inIframe')) 
		{
      		__parent().changeDataFrist1(vin);
		}
	   _hide();

    }
	//返回的数据 更新页面数据
	function changeCheck1(checkBox,claim_no,vin,create_date){
      	var  types= document.getElementsByName("Relation");
      	var type="";
      	for(i=0;i<types.length;i++) 
      	 { 
      	 if(types[i].checked) 
      	  type = types[i].value;
      	 } 
		if(checkBox.checked){
			if (parent.$('inIframe')) 
			{
				__parent().changeDataFrist(claim_no,vin,create_date,type);
			}
		   _hide();
		}
	}
	
</script>
</body>
</html>