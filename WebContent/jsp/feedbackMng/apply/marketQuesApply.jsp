<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>市场问题工单处理</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 信息反馈管理&gt;信息反馈提报 &gt;市场问题工单处理</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="seriesList" id="seriesList" value="<%=request.getAttribute("seriesList")%>"/>
    <TABLE class="table_query">
       <tr>
         <td width="7%" align="right" nowrap>工单号：</td>
         <td colspan="6"><input id="query_order_no" name="query_order_no" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,18" callFunction="javascript:MyAlert();"></td>
         <td width="9%" align="right" nowrap>车辆识别码(VIN)：</td>
         <td colspan="2" align="left" ><input type="text" id="query_vin" name="query_vin"  value="" class="middle_txt"/> </td>
       </tr>
       <tr>
         <td width="7%" align="right" nowrap >创建时间： </td>
         <td colspan="6" nowrap>
          <input name="query_start_date" id="query_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="query_start_date,query_end_date" hasbtn="true" callFunction="showcalendar(event, 'query_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="query_end_date" id="query_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="query_start_date,query_end_date" hasbtn="true" callFunction="showcalendar(event, 'query_end_date', false);">
          </td>
         <td align="right">车系：</td>
         <td><script type="text/javascript">
              var seriesList=document.getElementById("seriesList").value;
    	      var str="";
    	      str += "<select id='vehicleSeriesList' name='vehicleSeriesList'>";
    	      str+=seriesList;
    		  str += "</select>";
    		  document.write(str);
	        </script></td>
         <td width="29%" align="right" >			</td>
       </tr>
       <tr>
         <td align="right" nowrap >&nbsp;</td>
         <td colspan="6" nowrap>&nbsp;</td>
         <td align="center" nowrap><input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           <input class="normal_btn" type="button" id="addButton" name="addButton" value="新增"  onClick="addOrder();"></td>
         <td>&nbsp;</td>
         <td align="right" ></td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
<form name="form1" style="display:none">
	  <table id="bt" class="table_list">
		  <tr><th align="left"><p>
		    <input class="normal_btn" type=button value='上报' name=modify onclick="subChecked('sub');">
		    &nbsp;<input class="normal_btn" type=button value='删除' name=modify onclick="subChecked('del');">
		  </p></th>
		  </tr>
	  </table>
    </form>
<!-- 查询条件 end -->
<br>
<script type="text/javascript">

	document.form1.style.display = "none";
	
	var HIDDEN_ARRAY_IDS=['form1'];
	
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/feedbackmng/apply/MarketQuestionOrderApplyManager/queryOrderInfo.json";
				
   var title = null;

   var columns = [
  				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'orderId',renderer:myCheckBox},
  				{id:'action',header: "工单号",sortable: false,dataIndex: 'orderId',renderer:orderDetail ,align:'center'},
  				{header: "车辆识别码(VIN)", dataIndex: 'vin', align:'center'},
  				{header: "车系", dataIndex: 'group_name', align:'center'},
  				/*{header: "投诉类别", dataIndex: 'compType', align:'center',renderer:exportCHN},*/
  				{header: "投诉类别", dataIndex: 'compType', align:'center',renderer:exportCHN},
  			  	{header: "信息类型", dataIndex: 'infoType', align:'center',renderer:getItemValue},
  				{header: "提报日期", dataIndex: 'orderDate', align:'center'},
  				{header: "工单状态", dataIndex: 'code_desc', align:'center'},
  				{id:'action',header: "操作",sortable: false,dataIndex: 'orderId',renderer:myLink ,align:'center'}
  		      ];
   //新建工单
   function addOrder(){
	   fm.action = "<%=contextPath%>/feedbackmng/apply/MarketQuestionOrderApplyManager/goAddMarkerOrder.do";
	   fm.submit();
   }
   //修改的超链接设置
   function myLink(value,meta,record){
      return String.format(
       "<a href=\"<%=contextPath%>/feedbackmng/apply/MarketQuestionOrderApplyManager/modifyOrderDetailInfo.do?ORDER_ID="
		+ value + "\">[修改]</a>");
   }
   //前台页面解释投诉类型
   function exportCHN(value,metaDate,record){
	   var strEn=record.data.compType;
	   var strCHN="";
	   if(null!==strEn){
	   var strCHN=strEn.replace("F","服务").replace("C","产品质量").replace("B","备件");
		}
	   return String.format(strCHN);
   }
   //全选checkbox
   function myCheckBox(value,metaDate,record){
	  return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
   }
   function doInit(){
	   loadcalendar();
	}
   //按工单号明细查询
   function orderDetail(value){
     	return String.format(
         "<a href=\"#\" onclick='sel(\""+value+"\")'>["+ value +"]</a>");
   }
   	//详细页面
	function sel(value){
		OpenHtmlWindow('<%=contextPath%>/feedbackmng/apply/MarketQuestionOrderApplyManager/queryOrderDetailInfo.do?orderId='+value,800,500);
	}
   /**
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
	    		MyConfirm("确认删除？",delMarketOrder,[str]);
	    	}else if(action == 'sub'){
	    		MyConfirm("确认上报？",reportMarketOrder,[str]);
	    		
	    	}
	    }
	}
    //删除开始
	function delMarketOrder(str){
	   makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/MarketQuestionOrderApplyManager/delMarketOrder.json?orderIds='+str,returnBack,'fm','queryBtn');
	}
	//删除回调函数
	function returnBack(json){
		var del = json.returnDelFlag;
		if(del==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	//上报开始
	function reportMarketOrder(str){
		makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/MarketQuestionOrderApplyManager/reportMarketOrder.json?orderIds='+str,returnResport,'fm','queryBtn');
	}
	//上报回调函数
	function returnResport(json){
		var report = json.returnReportFlag;
		if(report==1){
			__extQuery__(1);
			MyAlert("上报成功！");
		}else{
			MyAlert("上报失败！请联系管理员！");
		}
	}
   //设置超链接 end
   function showButton(value,meta,record){//显示上报、删除按钮
	   document.getElementById("bt").style.visibility = "visible";
   }
</script>
</BODY>
</html>