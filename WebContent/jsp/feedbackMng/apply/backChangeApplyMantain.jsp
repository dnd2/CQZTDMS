<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script>
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
<title>退换车申请书维护</title>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  信息反馈管理&gt;信息反馈提报 &gt;退换车申请书</div>
  	<form id="fm" name="fm">
  	<input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
	<TABLE class="table_query">
       <tr>
         <td class="table_query_4Col_label_3Letter" align="right">工单号：</td>
         <td><input name="ORDER_ID" id="ORDER_ID" datatype="1,is_digit_letter,18"  type="text" class="middle_txt"></td>
         <td class="table_query_4Col_label_6Letter" align="right">车辆识别码(VIN)：</td>
         <td><input type="text" name="VIN" id="VIN"  class="middle_txt"/> </td>
       </tr>
       <tr>
         <td class="table_query_4Col_label_4Letter" align="right">创建时间： </td>
         <td>
        		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
        		&nbsp;至&nbsp;
        		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
		</td>
         <td class="table_query_4Col_label_2Letter" align="right">车型：</td>
         <td>
         <script type="text/javascript">
              var seriesList=document.getElementById("seriesList").value;
    	      var str="";
    	      str += "<select id='vehicleSeriesList' name='vehicleSeriesList' class='short_sel'>";
    	      str+=seriesList;
    		  str += "</select>";
    		  document.write(str);
	        </script>
         	 </td>
       </tr>
	   <tr>
           <td colspan="4"  align="center">
           <input class="normal_btn" type="button" name="button1" value="查询"  onclick="showButton();__extQuery__(1)"/>
		   <input class="normal_btn" type="button" name="button1" value="新增"  onclick="window.location.href='<%=contextPath%>/feedbackmng/apply/BackChangeApplyMantain/backChangeApplyAddInit.do'"/></td>
      </tr>
  </table>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>    
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
  
  <form name="form1" style="display:none">
    <table id="bt" class="table_list">
	  <tr><th align="left"><p>
	    <input class="normal_btn" type=button value='上报' name=modify onclick="subChecked('sub');"/>
	    &nbsp;
	    <input class="normal_btn" type=button value='删除' name=modify onclick="subChecked('del');"/>
	  </p></th>
	  </tr>
  </table>
  </form>
  
  </body>
</html>
<script type="text/javascript" >

		document.form1.style.display = "none";
		
		var HIDDEN_ARRAY_IDS=['form1'];


var myPage;
	var url = "<%=request.getContextPath()%>/feedbackmng/apply/BackChangeApplyMantain/backChangeApplyQuery.json?COMMAND=1";

	var title = null;
	
	var columns = [
				{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'orderId',renderer:myCheckBox},
				{header: "工单号",sortable: false,dataIndex: 'orderId',renderer:mySelect ,align:'center'},
				{header: "车辆识别码(VIN)",sortable: false,dataIndex: 'vin',align:'center'},
				{header: "车系",sortable: false,dataIndex: 'model',align:'center'},
				//{header: "退换类型",sortable: false,dataIndex: 'exType',align:'center' ,renderer:getItemValue},
				{header: "创建时间",sortable: false,dataIndex: 'createDate',align:'center',renderer:getDate},
				{header: "工单状态",sortable: false,dataIndex: 'exStatus',align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'orderId',renderer:myLink ,align:'center'}
		      ];

//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<a href=\"<%=contextPath%>/feedbackmng/apply/BackChangeApplyMantain/modfiBackChangeApplyInit.do?ORDER_ID="
			+ value + "\">[修改]</a>");
	}
	
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
	}
	//日期格式化：
	function getDate(value,metaDate,record){
		return String.format(value.substring(0,10));
	}
	
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+value+"\")'>["+ value +"]</a>");
	}
	
	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/apply/BackChangeApplyMantain/detailBackChangeApply.do?ORDER_ID='+value,900,500);
	}
	
	//具体操作
	
//设置超链接 end

function selectFun(){
	__extQuery__(1);
	showButton();
}
function showButton(){//显示上报、删除按钮
	document.getElementById("bt").style.visibility = "visible";
}

/*
  	上报或删除方法
  	参数：action : "del"删除，"sub"上报
  	取得已经选择的checkbox，拼接成字符串，各项目以,隔开
*/
function subChecked(action) {
	var str="";
	var chk = document.getElementsByName("orderIds");
	var l = chk.length;
	var cnt = 0;
	for(var i=0;i<l;i++){        
		if(chk[i].checked)
		{            
			str = chk[i].value+","+str; 
			cnt++;
		}
	}
	if(cnt==0){
        MyAlert("请选择！");
        return;
    }else{
    	if(action == 'del'){
    		MyConfirm("确认删除？",del,[str]);
    		
    	}else if(action == 'sub'){
    		MyConfirm("确认上报？",sub,[str]);
    		
    	}
    }
}
//删除
function del(str){
	makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/BackChangeApplyMantain/deleteBackChangeApply.json?orderIds='+str,delBack,'fm','');
}
//删除回调方法：
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("删除成功！");
		__extQuery__(1);
	} else {
		MyAlert("删除失败！请联系管理员！");
	}
}
//上报
function sub(str){
	makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/BackChangeApplyMantain/submitBackChangeApply.json?orderIds='+str,submitBack,'fm','');
}
//上报回调方法：
function submitBack(redata) {
	if(redata.success != null && redata.success == "true") {
		MyAlert("上报成功！");
		__extQuery__(1);
	} else {
		MyAlert("上报失败！请联系管理员！");
	}
}
</script>