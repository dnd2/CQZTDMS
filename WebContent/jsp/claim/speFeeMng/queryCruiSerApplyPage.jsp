<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡航服务线路申请</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;特殊费用管理 &gt;巡航服务线路申请</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <TABLE class="table_query">
       <tr>
          <td  class="table_query_2Col_label_6Letter">单据编码：</td>
          <td><input id="xh_order_no" name="xh_order_no" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,20" callFunction="javascript:MyAlert();"></td>
          <td  class="table_query_2Col_label_6Letter">建单日期： </td>
          <td nowrap>
          <input name="create_start_date" id="create_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="create_end_date" id="create_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_end_date', false);"></td>
       </tr>
       <tr>
          <td  class="table_query_2Col_label_6Letter">巡航目的地：</td>
          <td>
            <input id="xh_aim_area" name="xh_aim_area" value="" type="text" class="middle_txt" datatype="1,is_digit_letter_cn,30" callFunction="javascript:MyAlert();">
          </td>
          <td class="table_query_2Col_label_6Letter"></td>
          <td  align="left" >
          </td>
       </tr>
       <tr>
         <td align="center" colspan="4" nowrap>
          <input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
          &nbsp;&nbsp;
          <input class="normal_btn" type="button" name="qryButton" value="新增"  onClick="addOrd();">
         </td>
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
	    <input class="normal_btn" type="button" value='上报' name="report_btn" onclick="subChecked('sub');">
	    &nbsp;<input class="normal_btn" type="button" value='删除' name="del_btn" onclick="subChecked('del');">
	  </p></th>
	  </tr>
  </table>
</form>
<br>
<script type="text/javascript">
   var myPage;

   document.form1.style.display = "none";
	
   var HIDDEN_ARRAY_IDS=['form1'];
   //查询路径
   var url = "<%=contextPath%>/claim/speFeeMng/CruiServicePathApplyManager/queryUnreportedOrdList.json";
				
   var title = null;

   var columns = [
  				{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'id',renderer:myCheckBox},
  				{header: "单据编码", dataIndex: 'crNo', align:'center'},
  				{header: "建单日期", dataIndex: 'createDate', align:'center',renderer:formatDate},
  				{header: "巡航目的地", dataIndex: 'crWhither', align:'center'},
  				{header: "巡航服务负责人", dataIndex: 'crPrincipal', align:'center'},
  				{id:'action',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink ,align:'center'}
  		      ];
   //全选checkbox
   function myCheckBox(value,metaDate,record){
	  return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
   }
   //修改的超链接设置
   function myLink(value,meta,record){
	   return String.format("<a href='#' onClick='modifyOrd("+value+");'>[修改]</a>");
   }
   function modifyOrd(value){
	   var goUrl="<%=contextPath%>/claim/speFeeMng/CruiServicePathApplyManager/modifyOrdPage.do?ord_id="+value;
	   OpenHtmlWindow(goUrl,900,500);
   }
   function doInit(){
	  loadcalendar();
   }
   //新增巡航工单
   function addOrd(){
	   var submit_url="<%=contextPath%>/claim/speFeeMng/CruiServicePathApplyManager/addCruiServiceOrd.do";
	   OpenHtmlWindow(submit_url,900,500);
   }
   //日期格式化：
   function formatDate(value,metaDate,record){
	 return String.format(value.substring(0,10));
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
	    	str=str.substring(0,str.length-1);
	    	if(action == 'del'){
	    		MyConfirm("确认删除？",delOrder,[str]);
	    	}else if(action == 'sub'){
	    		MyConfirm("确认上报？",reportOrder,[str]);
	    		
	    	}
	    }
	}
    //删除
	function delOrder(str){
	   makeNomalFormCall('<%=request.getContextPath()%>/claim/speFeeMng/CruiServicePathApplyManager/delOrder.json?orderIds='+str,callForDel,'fm','queryBtn');
	}
	//删除回调函数
	function callForDel(json){
		var retCode = json.retCode;
		if(retCode=="del_success"){
			refreshPage();
			MyAlert("删除成功！");
		}else if(retCode=="del_failure"){
			MyAlert("删除失败！");
		}
	}
	//上报
	function reportOrder(str){
		makeNomalFormCall('<%=request.getContextPath()%>/claim/speFeeMng/CruiServicePathApplyManager/reportOrder.json?orderIds='+str,callForReport,'fm','queryBtn');
	}
	//上报回调函数
	function callForReport(json){
		var retCode = json.retCode;
		if(retCode=="report_success"){
			refreshPage();
			MyAlert("上报成功！");
		}else if(retCode=="report_failure"){
			MyAlert("上报失败！");
		}
	}
	function refreshPage(){
		__extQuery__(1);
	}
</script>
</body>
</html>