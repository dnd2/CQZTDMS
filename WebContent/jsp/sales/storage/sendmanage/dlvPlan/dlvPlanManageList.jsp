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
<title>发运计划发送 </title>
</head>

<body onload="genLocSel('txt1','','');">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>发运计划发送</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>发运计划发送</h2>
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
		  <!-- <td class="right">发运结算省份：</td>  
			    <td align="left">
		  		<select class="u-select" id="txt1" name="jsProvince" onchange="_genCity(this,'txt2')"></select>
	     	 </td> -->
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
		  <!-- <td class="right">发运结算城市：</td>  
			    <td align="left">
		  		<select class="u-select" id="txt2" name="jsCity" onchange="_genCity(this,'txt3')"></select>
	     	 </td> -->
	     <!-- 	 <td class="right">发运结算区县：</td>
	   	  <td align="left">
	 			<select class="u-select" id="txt3" name="jsCounty"></select>
		 </td>-->
	     	 <td class="right" nowrap="true">组板日期：</td>
			<td align="left" nowrap="true">
				<input class="short_txt" readonly="readonly"  type="text" id="START_DATE" name="START_DATE" onFocus="WdatePicker({el:$dp.$('START_DATE'), maxDate:'#F{$dp.$D(\'END_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
				<input class="short_txt" readonly="readonly"  type="text" id="END_DATE" name="END_DATE" onFocus="WdatePicker({el:$dp.$('END_DATE'), minDate:'#F{$dp.$D(\'START_DATE\')}'})"  style="cursor: pointer;width: 80px;"/>
			</td>	
	</tr>
	<tr align="center">
	  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
	    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="doQuery()" />
	  		  <input type="reset"  id="resetButton" class="u-button u-reset" value="重置"/>
	    	  <input type="button" id="saveButton" class="normal_btn"  value="发送" onclick="doSave();" />
	    	     	 	
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
	var url = "<%=contextPath%>/sales/storage/sendmanage/DlvPlanManage/DlvPlanManageQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "操作",dataIndex: 'BO_ID',sortable: false, align:'center',renderer:myLink},
				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")'/>",sortable: false,dataIndex: 'BO_ID',renderer:myCheckBox},
				{header: "计划装车日期",dataIndex: 'PLAN_LOAD_DATE',align:'center',renderer:setLoadDate},
				{header: "最晚发运日期",dataIndex: 'DLV_FY_DATE',align:'center',renderer:setFyDate},
 				{header: "最晚到货日期",dataIndex: 'DLV_JJ_DATE',align:'center',renderer:setJhDate},
				{header: "组板号",dataIndex: 'BO_NO',align:'center'},
				{header: "发运方式",dataIndex: 'SHIP_NAME',align:'center'},
				{header: "承运商",dataIndex: 'LOGI_NAME',align:'center'},
				//{header: "发运结算地",dataIndex: 'BAL_ADDR',align:'center'},
				{header: "组板日期",dataIndex: 'BO_DATE',align:'center'},
				{header: "组板量",dataIndex: 'BO_NUM',align:'center'}
		      ];
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
    function seachSend(value,orderId,boNo){
   	 	
   	 	var urlss="<%=contextPath%>/sales/storage/sendmanage/DlvPlanManage/seachInit.do?Id="+value;
    	OpenHtmlWindow(urlss,1000,450);
     }
  	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' /><input type='hidden' name='hiddenIds' value='" + value + "' />");
	}
 	//计划装车日期
	function setLoadDate(value,metaDate,record){
 		var str="<input readonly='eadonly' name='LOAD_DATES' type='text' id='LOAD_DATES"+record.data.BO_ID+"' value='"+value+"' onFocus=\"WdatePicker({el:$dp.$('LOAD_DATES"+record.data.BO_ID+"')})\" style='cursor: pointer;width: 80px;'/>";
		return String.format(str);
	}
	//最晚发运日期
	function setFyDate(value,metaDate,record){
		var str="<input readonly='eadonly' name='FY_DATES' type='text' id='FY_DATES"+record.data.BO_ID+"' value='"+value+"' onFocus=\"WdatePicker({el:$dp.$('FY_DATES"+record.data.BO_ID+"')})\" style='cursor: pointer;width: 80px;'/>";
		return String.format(str);
	}
	//最晚收货日期
	function setJhDate(value,metaDate,record){
		var str="<input readonly='eadonly' name='JH_DATES' type='text' id='JH_DATES"+record.data.BO_ID+"' value='"+value+"' onFocus=\"WdatePicker({el:$dp.$('JH_DATES"+record.data.BO_ID+"')})\" style='cursor: pointer;width: 80px;'/>";
		return String.format(str);
	}
	//发送
	function doSave(){
		var b=0;
		var arrayObj = new Array(); 
		arrayObj=document.getElementsByName("groupIds");
		var loadDates=document.getElementsByName("LOAD_DATES");//计划装车日期
		var fyDates=document.getElementsByName("FY_DATES");//最晚发运日期
		var jhDates=document.getElementsByName("JH_DATES");//最晚交货日期
		var c=0;
		var d=0;
		var f=0;
		for(var i=0;i<arrayObj.length;i++){
			if(arrayObj[i].checked){
				b=1;//有选中
				if(loadDates[i].value==""){//计划装车日期为空
					c=1;
					break;
				}
				if(fyDates[i].value==""){//最晚发运日期为空
					d=1;
					break;
				}
				if(jhDates[i].value==""){//最晚交货日期为空
					f=1;
					break;
				}
			}
			
		}
		if(b==0){
			MyAlert("请选择需要发送的信息！");
			return ;
		}
		if(c==1){
			MyAlert("请填写选中记录的计划装车日期！");
			return ;
		}
		if(d==1){
			MyAlert("请填写选中记录的最晚发运日期！");
			return ;
		}
		if(f==1){
			MyAlert("请填写选中记录的最晚到货日期！");
			return ;
		}
		MyConfirm("确认发送计划！",saveModify);	
	}
	
	function saveModify()
	{ 
		disabledButton(["saveButton"],true);
		makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/DlvPlanManage/sendDlvPlan.json",sendDlvPlanBack,'fm'); 
	}
	
	function sendDlvPlanBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/DlvPlanManage/planManageInit.do";
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
