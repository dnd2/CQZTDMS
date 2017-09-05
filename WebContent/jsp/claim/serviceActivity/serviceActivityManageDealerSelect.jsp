<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动-经销商列表</title>
<% String contextPath = request.getContextPath();%>
<% 
//List<TmOrgPO> listSelected=(List<TmOrgPO>)request.getAttribute("listSelected"); 
%>
<script type="text/javascript">
//下拉列表回显
function selectedIndex(){
 var groupIdXi=document.getElementById("orgIdSelected").value;
 document.getElementById(groupIdXi).selected="selected";
}
//当关闭子页面时，执行此方法调用父页面doInit()方法中的__extQuery__(1);刷新查询
function showReaction(){
	parentContainer.doInit();
}
</script>
</head>

<body onbeforeunload="showReaction()">
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动经销商管理
	</div>
  <form method="post" name="fm" id="fm">
     <input type="hidden" name="activityId" id="activityId" value="<%=request.getAttribute("activityId") %>"/>
     <input type="hidden" id="dealerCode" name="dealerCode" value=""/>
     <input type="hidden" id="dealerName" name="dealerName" value=""/>
     <input type="hidden" name="orgIdSelected" id="orgIdSelected" value="<%=request.getAttribute("orgIdName")%>"/>
   <table class="table_query">
          <tr>
            <td class="table_query_2Col_label_5Letter">区域名称：</td>
            <td align="left">
	           <select name="orgId" id="orgId" class="short_sel">
	                       <option value="1"><c:out value="--请选择--"/></option>
						<c:forEach var="listSelected" items="${listSelected}">
							<option value="${listSelected.orgId}" id="${listSelected.orgId}"><c:out value="${listSelected.orgName}"/></option>
						</c:forEach>
			  </select>
			  <script type="text/javascript">
					  selectedIndex();
			  </script>
            </td>
            <td class="table_query_2Col_label_5Letter">省份：</td>
            <td align="left">
            	<input type="text"  name="regionName"  id="regionName"  class="middle_txt"  size="25" value=""  />
            </td>
          </tr>
          <tr>
            <td class="table_query_2Col_label_5Letter">经销商代码：</td>
            <td align="left">
            	<input type="text"  name="dealerCodes"  id="dealerCodes"  class="middle_txt"  size="25" value=""  />
            </td>
            <td class="table_query_2Col_label_5Letter">经销商名称：</td>
            <td align="left">
            	<input type="text"  name="dealerNames"  id="dealerNames"  class="middle_txt"  size="25" value="" />
            </td>
    </tr>
    <tr>
            <td align="center" colspan="4">
	             <input class="normal_btn" type="button" name="button1" value="查询"  onclick="showButton();__extQuery__(1);"/>
	             <input class="normal_btn" type="button" name="button1" value="关闭" onclick="parent.window._hide();" />
            </td>
    </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<!--没有数据时 上报/删除按钮隐藏 开始  -->
<form name="form1" style="display:none">
<table id="bt" class="table_query">
  	<tr> 		  
  		  <td align="center">
  		  	  <input type="button" class="normal_btn" value="全选" onclick="mySelAll();"/>
  		  	  &nbsp;
  		  	  <input type="button" class="normal_btn" value="全不选" onclick="myNoSelAll();"/>
  		  	  &nbsp;
		      <input type="button" name="bt_new" id="bt_new_id" class="normal_btn" value="新增" onclick="subChecked();"/>
		      &nbsp;
	      	  <input type="button" name="bt_return" id="bt_return_id" class="normal_btn" value="关闭" onclick="parent.window._hide();" />
	      </td>
  	</tr>
  </table>
</form>
<!--没有数据时 上报/删除按钮隐藏 结束  -->
<!--页面列表 begin -->
<script type="text/javascript" >
//没有数据时 上报/删除按钮隐藏 开始 
document.form1.style.display = "none";

var HIDDEN_ARRAY_IDS=['form1'];
//没有数据时 上报/删除按钮隐藏 结束

	var myPage;
	var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageDealer/serviceActivityManageDealerNoExitsQuery.json";
	var title = null;
	var columns = [
     			{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'DEALER_ID',renderer:myCheckBox},
     			{header: "所属区域", dataIndex: 'ORG_NAME', align:'center'},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME' ,align:'center'},
				{header: "联系电话",dataIndex: 'PHONE' ,align:'center'},
				{header: "地址",dataIndex: 'ADDRESS' ,align:'center'}
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
				str += chk[i].value;
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
    	document.getElementById("dealerCode").value = names;
    	document.getElementById("dealerName").value = couts;
    	MyDivConfirm("是否确认增加？",sures,[str]);
    	
    }
}

function sures(str){
	makeNomalFormCall('<%=request.getContextPath()%>/claim/serviceActivity/ServiceActivityManageDealer/serviceActivityManageDealerOption.json?groupIds='+str,delBack,'fm','queryBtn');
}
//新增回调方法：
function delBack(json) {
	if(json.returnValue == '1')
	{
		MyDivAlert("新增经销商成功！");
		parent.window._hide();
	}else
	{   
		MyAlert("新增经销商失败！");
	}
}

function showButton(){//显示上报、删除按钮
	document.getElementById("bt").style.visibility = "visible";
}
function mySelAll(){
	$('checkAll').checked = true ;
	var arr = document.getElementsByName('orderIds');
	for(var i = 0;i<arr.length;i++)
		arr[i].checked = true ;
}
function myNoSelAll(){
	$('checkAll').checked = false ;
	var arr = document.getElementsByName('orderIds');
	for(var i = 0;i<arr.length;i++)
		arr[i].checked = false ;
}
</script>
<!--页面列表 end -->
</body>
</html>
