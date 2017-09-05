<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 发运司机调整 </title>
</head>

<body onload="">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>发运司机调整</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>发运司机调整</h2>
	<div class="form-body">
	<!-- 查询条件 begin -->
	<table class="table_query" id="subtab">
	<tr class="csstr" align="center"> 
		<td class="right">承运商：</td> 
		  <td align="left">
			 <select name="LOGI_NAME" id="LOGI_NAME" class="u-select" >
			 	<option value="">-请选择-</option>
					<c:if test="${list_logi!=null}">
						<c:forEach items="${list_logi}" var="list_logi">
							<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
						</c:forEach>
					</c:if>
		  		</select>
		  </td>	 
		  <td class="right">组板号：</td> 
		  <td align="left" >
			  <input type="text" maxlength="20"  id=BO_NO name="BO_NO" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
		  </td>	
	</tr>
	  <tr class="csstr" align="center">   
		  <td class="right">发运方式：</td> 
		  <td align="left">
			   <label>
					<script type="text/javascript">
							genSelBoxExp("TRANSPORT_TYPE",<%=Constant.TT_TRANS_WAY%>,"",true,"u-select","","false",'');
						</script>
				</label>
		  </td>
		  <td class="right">驾驶员姓名：</td>  
		  <td align="left">
	  		<input type="text" maxlength="20"  id="driverName" name="driverName"  class="middle_txt" size="15" />
		  </td> 
	</tr>
	<tr class="csstr" align="center"> 
	     	<td class="right" nowrap="true">组板日期：</td>
			<td align="left" nowrap="true">
				<input class="short_txt" readonly="readonly"  type="text" id="START_DATE" name="START_DATE" onFocus="WdatePicker({el:$dp.$('START_DATE'), maxDate:'#F{$dp.$D(\'END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
				<input class="short_txt" readonly="readonly"  type="text" id="END_DATE" name="END_DATE" onFocus="WdatePicker({el:$dp.$('END_DATE'), minDate:'#F{$dp.$D(\'START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
			</td>
		  <td class="right">驾驶员电话：</td>  
		   <td align="left">
		  			<input type="text"  id="driverTel" name="driverTel" datatype="1,is_digit,11" maxlength="11" class="middle_txt"/>
	       </td>
	</tr>
	<tr align="center">
	  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
	    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="doQuery()" />
	  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
	    	  <input type="button" id="saveButton" class="normal_btn"  value="保存物流信息" onclick="doSave();" />
	    	     	 	
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
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/sendmanage/SendDriverManage/sendDriverSeachQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")'/>",sortable: false,dataIndex: 'BO_ID',renderer:myCheckBox},
				{header: "操作",dataIndex: 'BO_ID',sortable: false, align:'center',renderer:myLink},
				{header: "驾驶员姓名",dataIndex: 'DRIVER_NAME',align:'center',renderer:setDriverName},
				{header: "驾驶员电话",dataIndex: 'DRIVER_TEL',align:'center',renderer:setDriverTel},
 				{header: "领票车队",dataIndex: 'CAR_TEAM',align:'center',renderer:setCarteam},
				{header: "车牌号",dataIndex: 'CAR_NO',align:'center',renderer:setCarno},
				{header: "组板号",dataIndex: 'BO_NO',align:'center'},
				{header: "组板日期",dataIndex: 'BO_DATE',align:'center'},
				{header: "组板数量",dataIndex: 'BO_NUM',align:'center'}
		      ];
	//初始化    
	function doInit(){
		
		//__extQuery__(1);
	}
	function doQuery(){
		__extQuery__(1);
	}
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    function myLink(value,meta,record){
        var link="<a href='javascript:void(0);' class='u-anchor' onclick='seachSend(\""+value+"\")'>查看</a>";
       
  		return String.format(link);
    }
    function updateSend(value,orderId,boNo){
   	 	window.location.href="<%=contextPath%>/sales/storage/sendmanage/SendBoardSeach/updateSendBordSeachInit.do?Id="+value;
     }
    function seachSend(value,orderId,boNo){
   	 	
   	 	var urlss="<%=contextPath%>/sales/storage/sendmanage/SendBoardSeach/seachInit.do?Id="+value;
    	OpenHtmlWindow(urlss,1000,450);
     }
  	//全选checkbox
	function myCheckBox(value,metaDate,record){
		//var orderNum=record.data.CHK_NUM;
		//var areaId=record.data.AREA_ID;
		//return String.format("<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' /><input type='hidden' name='hiddenIds' value='" + value + "' /><input type='hidden' name='areaIds' value='" + areaId + "' /><input type='hidden' name='orderNum' value='" + orderNum + "' />");
		return String.format("<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' /><input type='hidden' name='hiddenIds' value='" + value + "' />");
	}
 	//驾驶员姓名
	function setDriverName(value,metaDate,record){
		return String.format("<input type='text' maxlength='20'  id='dName"+record.data.BO_ID+"' name='dNames' value='"+value+"' size='10' />");
	}
	//驾驶员电话
	function setDriverTel(value,metaDate,record){
		return String.format("<input type='text'  id='dTel"+record.data.BO_ID+"' name='dTels' value='"+value+"' datatype='1,is_digit,11' maxlength='11' />");
	}
	//领票车队
	function setCarteam(value,metaDate,record){
		return String.format("<input type='text' maxlength='30'  id='carTeam"+record.data.BO_ID+"' name='carTeams' value='"+value+"' size='10' />");
	}
	//车牌号
	function setCarno(value,metaDate,record){
		return String.format("<input type='text' maxlength='20'  id='carNo"+record.data.BO_ID+"' name='carNos' value='"+value+"' size='10' />");
	}
	//保存
	function doSave(){
		var b=0;
		var arrayObj = new Array(); 
		arrayObj=document.getElementsByName("groupIds");
		var dNames=document.getElementsByName("dNames");//驾驶员姓名
		var dTels=document.getElementsByName("dTels");//驾驶员电话
		var c=0;
		var d=0;
		for(var i=0;i<arrayObj.length;i++){
			if(arrayObj[i].checked){
				b=1;//有选中
				if(dNames[i].value==""){//驾驶员姓名为空
					c=1;
					break;
				}
				if(dTels[i].value==""){//驾驶员电话为空
					d=1;
					break;
				}
			}
			
		}
		if(b==0){
			MyAlert("请选择需要保存的信息！");
			return ;
		}
		if(c==1){
			MyAlert("请填写选中记录的驾驶员姓名！");
			return ;
		}
		if(d==1){
			MyAlert("请填写选中记录的驾驶员电话！");
			return ;
		}
		
		MyConfirm("确认保存物流信息吗？",saveModify);	
	}
	
	function saveModify()
	{ 
		disabledButton(["saveButton"],true);
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendDriverManage/saveSendDriverModify.json",saveModifyBack,'fm','queryBtn'); 
	}
	
	function saveModifyBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/SendDriverManage/sendDriverSeachInit.do";
			fm.submit();
		}else
		{
			disabledButton(["saveButton"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>
