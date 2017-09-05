<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动-服务活动工时选择</title>
<% String contextPath = request.getContextPath();%>
</head>

<body>
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理
	</div> 
  <form name="fm" id="fm" >
	  <input type="hidden" id="cnDes" name="cnDes">
	  <input type="hidden" id="labourHour" name="labourHour">
	  <input type="hidden" id="labourCode" name="labourCode">
	  <input type="hidden" name="activityId" id="activityId" value="<%=request.getAttribute("activityId") %>">
	 <table class="table_query" >
		    <tr>
		      <td class="table_query_2Col_label_5Letter">工时代码：</td>
	          <td align="left" >
	           		 <input name="labourCodes" type="text"  class="middle_txt" id="labourCodes" value="" datatype="1,is_digit_letter_cn,18"/>
	          </td>
	          <td class="table_query_2Col_label_5Letter">工时名称: </td>
	          <td align="left" >
	         		 <input name="cnDess" type="text"   class="middle_txt" id="cnDess" value="" datatype="1,is_digit_letter_cn,18"/>
	          </td>
	        </tr>
		    <tr align="left">
		      <td colspan="4" align="center">
			      <input type="button" name="list" value="查询" id="queryBtn" class="normal_btn" onclick="__extQuery__(1);" />
		          <input type="button" name="list2" value="返回" class="normal_btn"  onclick="goBack();" />
		      </td>
		    </tr>
		  </table>
           <table align=center  class="table_list" style="border-bottom:1px solid #DAE0EE" >
           <tr>
	           <th colspan="3" align="left">
		           <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 工时列表               
		           <input type="button" name="list3" value="添加" class="normal_btn" onclick="subChecked();" />
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
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageItem/serviceActivityManageItemWorkHoursQuery.json";
	var title = null;
	var columns = [
     			{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'LABOURID',renderer:myCheckBox},
				{header: "工时代码", dataIndex: 'LABOUR_CODE', align:'center'},
				{header: "工时名称",dataIndex: 'CN_DES' ,align:'center'},
				{id:'action',header: "工时数",sortable: false,dataIndex: 'LABOUR_HOUR' ,align:'center'}
		      ];
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
	}
	
	/*
  	增加方法
  	参数：action : "add":增加
  	取得已经选择的checkbox，拼接成字符串，各项目以,隔开
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
    	var couts = "";
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
    			    var cot = tab.rows[i].cells[3].innerText;
    			    if(couts){
    			    	couts += "," + cot;
    			    }else{
    			    	couts = cot;
    			    }
    			      
    			}
    		}
    	}
    	document.getElementById("cnDes").value = names;
    	document.getElementById("labourHour").value = couts;
    	document.getElementById("labourCode").value = codes;
    	MyDivConfirm("是否确认增加？",sures,[str]);
    }
}
function sures(str){
	makeNomalFormCall('<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManageItem/serviceActivityManageItemWorkHoursOption.json?labourId='+str,delBack,'fm','queryBtn');
}
//新增回调方法：
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyDivAlert("新增成功！");
		//__extQuery__(1);
		goBack();
	} else {
		MyDivAlert("新增失败！请联系管理员！");
	}
}
//返回活动工时列表
function goBack(){
	fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageItem/serviceActivityManageItemQuery.do?activityId="+<%=request.getAttribute("activityId") %>;
	fm.submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>
