<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动-活动其它项目选择</title>
<% String contextPath = request.getContextPath();%>
</head>

<body onload="__extQuery__(1);">
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理
	</div> 
  <form name="fm" >
	  <input type="hidden" id="itemCode"  name="itemCode">
	  <input type="hidden" id="itemDesc"  name="itemDesc">
	  <input type="hidden" id="activityId" name="activityId" value="<%=request.getAttribute("activityId") %>" /><!--
	 <table class="table_edit" >
		    <tr>
		      <td height="20" align="right" class="" >代码：</td>
		      <td align="left" nowrap="nowrap" class="">
			       <select  name='CON_ITEM_TYPE2'  id='CON_ITEM_TYPE2' class="short_sel" >
			        <option value='11661001' >全部</option>
	                <option value='11661001' >外出费用</option>
			        <option value='11661002' >拖车费用</option>
			        <option value='11661003' >餐饮费用 </option>
			        </select>
		        </td>
		      <td width="20%" align="right" class="" >&nbsp;</td>
	          <td width="39%" align="left" nowrap="nowrap" class="" >&nbsp;</td>
	        </tr>
		    <tr align="left" class="">
		      <td height="20" align="right"></td>
		      <td align="left">&nbsp;&nbsp;&nbsp;</td>
		      <td align="right"><input type="button" name="list" value="查询" class="normal_btn" />
	          <input type="button" name="list2" value="返回" class="normal_btn"  onclick="history.go(-1)" /></td>
		      <td >&nbsp;</td>
		    </tr>
		  </table>
           -->
           <table align=center  class="table_list" style="border-bottom:1px solid #DAE0EE" >
	           <tr>
		           <th colspan="3" align="left">
			           <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 活动其它项目列表               
			          	  <input type="button" name="list3" value="添加" class="normal_btn" onclick="subChecked();"/>
			          	  <input type="button" name="list2" value="返回" class="normal_btn"  onclick="goBack();" />
		          </th>
	          </tr>
		  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageItem/serviceActivityManageItemOthersQuery.json";
	var title = null;
	var columns = [
     			{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'FEE_ID',renderer:myCheckBox},
				{header: "其他费用代码", dataIndex: 'FEE_CODE', align:'center'},
				{header: "其他费用名称",dataIndex: 'FEE_NAME' ,align:'center'}
		      ];
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
	}
	
	/*
           功能：增加方法
  	描述：取得已经选择的checkbox，拼接成字符串，各项目以,隔开
   */
function subChecked() {
	var str="";
	var chk = document.getElementsByName("orderIds");
	var l = chk.length;
	var cnt = 0;
	for(var i=0;i<l;i++){        
		if(chk[i].checked)
		{          
			if(str)
			{  
				str += ","+chk[i].value;
			}else{
				str = chk[i].value;
			} 
			cnt++;
		}
	}
	if(cnt==0){
		MyDivAlert("请选择！");
        return;
    }else{
    	
    	var tab = document.getElementById("myTable");
    	var codes = "";
    	var names = "";
    	if(tab.rows.length >1)
    	{
    		for(var i=1; i < tab.rows.length; i++)
    		{
    			var checkObj = tab.rows[i].cells[0].firstChild;

    			if(checkObj.checked ==  true)
    			{
    				var code = tab.rows[i].cells[1].innerText;
    				if(codes){
    					codes += "," + code;
    				}else{
    					codes = code;
    				}
    				var name = tab.rows[i].cells[2].innerText;
    				if(names){
    					names += "," + name;
    				}else{
    					names = name;
    				}
    			}
    		}
    	}
    	document.getElementById("itemCode").value = codes;
    	document.getElementById("itemDesc").value = names;
    	MyDivConfirm("是否确认增加？",sures,[str]);
    	
    }
}

function sures(str){
	makeNomalFormCall('<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManageItem/serviceActivityManageItemOthersOption.json?id='+str,delBack,'fm','queryBtn');
}


//新增回调方法
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyDivAlert("新增成功！");
		//__extQuery__(1);
		goBack();
	} else {
		MyDivAlert("新增失败！请联系管理员！");
	}
}
//返回配件列表页面
function goBack(){
	fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageItem/serviceActivityManageItemQuery.do?activityId='+<%=request.getAttribute("activityId") %>";
	fm.submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>