<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
List list =(List)request.getAttribute("list_logi");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配车调整 </title>
</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理> 配车调整
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
  <tr class="csstr" align="center">  
   <td class="right" nowrap="true">组板日期：</td>
		<td align="left" nowrap="true">
			<input name="RAISE_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="RAISE_STARTDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'RAISE_STARTDATE', false);" />  	
             &nbsp;至&nbsp;
             <input name="RAISE_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="RAISE_ENDDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'RAISE_ENDDATE', false);" /> 
		</td>	
		<td class="right">组板号：</td> 
	  <td align="left">
		  <input type="text" maxlength="20"  id=BO_NO name="BO_NO" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
	  </td>	 
	   <td class="right">VIN：</td> 
	  <td align="left">
		  <input type="text" maxlength="20"  id=VIN name="VIN" class="middle_txt" size="15" />
	  </td>	 
</tr>
  <tr align="center">
  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
          <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />   	 	
    </td>
  </tr>
</table>
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
	var url = "<%=contextPath%>/sales/storage/sendmanage/AllocaAdjust/allocaAdjustQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "组板号",dataIndex: 'BO_NO',align:'center'},
				//{header: "是否有零售",dataIndex: 'HAVE_RETAIL',align:'center'},
				{header: "发运方式",dataIndex: 'SEND_TYPE',align:'center',renderer:getItemValue},
				{header: "组板人",dataIndex: 'NAME',align:'center'},
				{header: "组板时间",dataIndex: 'BO_DATE',align:'center'},
				//{header: "配车时间",dataIndex: 'ALLOCA_DATE',align:'center'},
				{header: "组板数量",dataIndex: 'BO_NUM',align:'center'},
				{header: "配车数量",dataIndex: 'ALLOCA_NUM',align:'center'},
				{header: "出库数量",dataIndex: 'OUT_NUM',align:'center'},
				//{header: "发运数量",dataIndex: 'SEND_NUM',align:'center'},
				//{header: "验收数量",dataIndex: 'ACC_NUM',align:'center'},
				{header: "操作",dataIndex: 'BO_ID',sortable: false, align:'center',renderer:myLink}
		      ];
	//初始化    
	function doInit(){
		//日期控件初始化
		//__extQuery__(1);
	}
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    function myLink(value,meta,record){
        var orderId=record.data.ORDER_ID;
        var boNo=record.data.BO_NO;
        var outNum=record.data.OUT_NUM;
        var sendType=record.data.SEND_TYPE;
        var outStatus=record.data.FUK_NUM;//付款状态
        var link="<a href=\"javascript:void(0);\" onclick='updateSend(\""+value+"\",\""+orderId+"\",\""+boNo+"\")'>[调整]</a>";
        	link+="<a href=\"javascript:void(0);\" onclick='canelAlOrder(\""+value+"\",\""+outNum+"\",\""+outStatus+"\")'>[取消配车]</a>";
        	link+="<a href=\"javascript:void(0);\" onclick='canelBoOrder(\""+value+"\",\""+outNum+"\",\""+sendType+"\",\""+outStatus+"\")'>[取消组板]</a>";
  		return String.format(link);
    }
    function updateSend(value,orderId,boNo){
   	 	window.location.href="<%=contextPath%>/sales/storage/sendmanage/AllocaAdjust/updateAllocaAdjustInit.do?Id="+value+"&orderId="+orderId+"&boNo="+boNo;
     }
    function canelAlOrder(value,outNum,outStatus){
    	 if(outStatus>0){
         	MyAlert("该组板下有中转库单已付款或付款中,无法取消配车！");
         	return;
         }
    	MyConfirm("此操作将取消此组板下的全部配车信息,确认取消?",confirmCanel,[value]);	
    }
    function canelBoOrder(value,outNum,sendType,outStatus){
        if(sendType==<%=Constant.TRANSPORT_TYPE_01%>){
			MyAlert("自提单不允许取消组板！");
		return ;
        }
        if(outStatus>0){
        	MyAlert("该组板下有中转库单已付款或付款中,无法取消组板");
        	return;
        }
    	MyConfirm("此操作将取消此组板以及组板下的全部配车信息,确认取消?",confirmBoCanel,[value,sendType]);	
    }
    function confirmCanel(boId){    
    	var url = "<%=request.getContextPath()%>/sales/storage/sendmanage/AllocaAdjust/canelAllocaMain.json";
    	makeCall(url,canelBoOrderBack,{boId:boId});
    }
    function confirmBoCanel(boId){    
    	var url = "<%=request.getContextPath()%>/sales/storage/sendmanage/AllocaAdjust/canelBoardMain.json";
    	makeCall(url,canelBoOrderBack,{boId:boId});
    }
	function canelBoOrderBack(json){
		if(json.returnValue==1){
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/sales/storage/sendmanage/AllocaAdjust/allocaAdjustInit.do";
			fm.submit();
		}else if(json.returnValue==3){
			parent.MyAlert("此组板有车辆已出库，无法取消组板！");
		}
		else{
			MyAlert(json.getException());
			
		}
	}
</script>
</body>
</html>
