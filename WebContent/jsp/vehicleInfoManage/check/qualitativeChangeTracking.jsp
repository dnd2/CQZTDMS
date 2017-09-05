<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	List levellist = (List)request.getAttribute("LEVELLIST");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>质改跟踪</title>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body onload="__extQuery__(1);">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;车辆信息管理&gt;质改跟踪</div>
<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>

  <form name='fm' id='fm' method="post" action="">
  <table class="table_query">
		<tr>
		<input type="hidden" name="tuser" id="tuser" value="${tuser }"/>
            <td  class="table_query_2Col_label_7Letter">车系：</td>
            <td >
           	<select name="carTieId" id="groupchexi" onchange="roadCheckBox(this);" style="width: 150px">
           		<option value="-1">-请选择-</option>
           		<c:forEach items="${groupList}"   var="groupList" >
           		<option value="${groupList.groupId}" >${groupList.groupCode}</option>
           		</c:forEach>
           	</select>
 		    </td> 	   
            <td class="table_query_2Col_label_7Letter" >车型：</td>
 			 <td >
            <select name="carTypeId"  id="groupchexing" style="width: 150px">
           		<option value="-1">-请选择-</option>
           	</select>
 		    </td> 	   
          </tr>
          <tr>
          	<td class="table_query_2Col_label_7Letter">底盘号：</td>
            <td>
           		<input name="VIN" id="VIN" value="" type="text" class="middle_txt" onblur="checkVin(this.value);" />
            </td> 
            <td  class="table_query_2Col_label_7Letter">部件厂代码：</td>
            <td><input name="makerCode" id="MAKER_CODE" value="" type="text" class="middle_txt" onblur="checkOut(this.value,1);" />
            </td>
          </tr>
          <tr>
          <td align="right">每页显示数：</td>
         	<td> 
			<select name="page_amount" id="page_amount" value="" onchange="__extQuery__(1);" class="short_sel">
			<option value="15" >15</option>
			<option value="50" selected="selected">50</option>
			<option value="100">100</option>
			<option value="200" >200</option>
			<option value="500">500</option>
			</select>
			</td>
			 <td  class="table_query_2Col_label_7Letter">零件号：</td>
            <td>
              <input name="PART_CODE" id="PART_CODE" value="" type="text" class="middle_txt"  />
            </td>
          </tr>
          <tr>
			 <td  class="table_query_2Col_label_7Letter">零件名称：</td>
            <td>
            <input name="PART_NAME" id="PART_NAME" value="" type="text" class="middle_txt"  />
            </td>
			 <td  class="table_query_2Col_label_7Letter"></td>
            <td>
            </td>
          </tr>
       <tr>
        <td colspan="4" align="center">
        		<input class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1);"/>
        		&nbsp;&nbsp;
				<input class="normal_btn" type="button" name="button1" value="动态"  onclick="showActive();"/>
				&nbsp;&nbsp;
				<input class="normal_btn" type="button"  name="button1" value="添加"  onclick="add();"/>
				&nbsp;&nbsp;
				<input class="normal_btn" type="button"  name="button1" value="导出"  onclick="exportToexcel();"/>
        </td>
       </tr>
 	</table>
 	 <!--分页 begin -->
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
  </body>
</html>
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/vehicleInfoManage.check/QualityInfoReportVerify/queFollowList.json?action=query";
	var title = null;
	var columns = [
                {header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\"  onclick='selectAll(this,\"check\")' />全选", align:'center',sortable:false, dataIndex:'ID',width:'2%',renderer:checkBoxShow},
				{header: "车系", dataIndex: 'CAR_TIE_ID', align:'center'},
				{header: "车型", dataIndex: 'CAR_TYPE_ID', align:'center'},
				{header: "底盘号", dataIndex: 'VIN', align:'center'},
				{header: "生产日期", dataIndex: 'RO_CREATE_DATE', align:'center'},
				{header: "跟踪天数", dataIndex: 'RO_CREATE_DATE', align:'center',renderer:mylinkTrackingNum},
				{header: "零件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "零件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "故障零件数", dataIndex: 'PART_NUM', renderer:partNum,align:'center'},
				{header: "故障零件数排序", dataIndex: 'PART_NUM', align:'center'},
				{header: "起始修理日期", dataIndex: 'RO_REPAIR_DATE_ONE', align:'center'},
				{header: "终止修理日期", dataIndex: 'RO_REPAIR_DATE_TWO', align:'center'},
				{header: "部件厂代码", dataIndex: 'MAKER_CODE', align:'center'},
				{header: "故障类别代码", dataIndex: 'MAL_CODE', align:'center'},
				{header: "故障现象", dataIndex: 'MAL_NAME', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center',renderer:mylinkremark},
				{header: "创建人", dataIndex: 'NAME', align:'center'},
				{header: "创建时间", dataIndex: 'CREATE_DATE', align:'center'},
				{id:'action', header: "操作", sortable: false, dataIndex: 'ID', renderer:oper, align:'center'}
		      ];
	
	function checkBoxShow(value,meta,record){//value='" + + "'
		return String.format("<input type='checkbox' id='check' onclick='checkActive(this,this.value)'  name='check' value='" + record.data.ID + "' />");
	}
	function mylinkTrackingNum(value,meta,record){
	    var	str = value.replace(/-/g,"/");
        var DateOne  = new Date();
        var Datetwo  = new Date(str);
        var cha="0";
        if(""!=value){
        cha=parseInt(Math.abs(((DateOne- Datetwo))/86400000))+"";   
        }
        return String.format(cha);  
	}
	function mylinkremark(value,meta,record){
		var str="";
		if (value.length>20){
            str= value.substring(0,20)+"……";
   	    }else{
            str+=value; 
   	   	    }
           return String.format(str);
    }
	function checkDate(obj,value){
		obj.checked = true;
	}
  //新增
  function add(){
    window.location.href="<%=contextPath%>/vehicleInfoManage.check/QualityInfoReportVerify/qualitativeChangeTrackingAdd.do";   
  }
  function  roadCheckBox(obj)
  {
  	var url =  "<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/groupchexitoxing.json?groupxi="+obj.value;
	makeCall(url,showRoadList,null);
  }
   function showRoadList(json){
		var obj = document.getElementById("groupchexing");
		if(json.groupList.length==0){
			obj.options.length=0
		    obj.options[0]=new Option("-请选择-",-1);
		}else{
			   obj.options[0]=new Option("-请选择-",-1);
			for(var i=0;i<json.groupList.length;i++){
				obj.options[i+1]=new Option(json.groupList[i].groupName, json.groupList[i].groupId + "");
			}
		}
	}
	//点击查看明细
	function oper(value,meta,record) {
		var link="<a href=\"#\" onclick='viewDetail(\""+record.data.ID+"\")'>[明细]</a>";
		link+="<a href=\"#\" onclick='updateThis(\""+record.data.ID+"\")'>[修改]</a>";
		link+="<a href=\"#\" onclick='deleteThis(\""+record.data.ID+"\")'>[删除]</a>";
		return String.format(link);
	}
	//详细信息查看页面
	function viewDetail(id) {
		fm.method = 'post';
		fm.action = '<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/queryDetail.do?id='+id;
		fm.submit();
	}
	function  updateThis(id){
        fm.method = 'post';
  		fm.action = '<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/queryDetailforUpdate.do?id='+id;
  		fm.submit();
	}
	//删除
	function deleteThis(id) {
		MyConfirm("是否确认删除？",delsubmit,[id]);
	}
	function delsubmit(id){
		makeNomalFormCall('<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/deleteThis.json?id='+id,returnBack,'fm','queryBtn');
	}
	//删除回调函数
	function returnBack(json){
		var avl = json.returnValue;
		if(avl==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	function partNum(value,meta,record){
		var partnum=record.data.PART_NUM;
		if(partnum==0){
			return String.format(""+partnum+"");
		}else{
			return String.format("<a href=\"#\" style='color:red' onclick='partNumDetail(\""+record.data.ID+"\")'>"+partnum+"</a>");
		}
	}
	function partNumDetail(id){
	    window.location.href="<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/partNumDetail.do?id="+id;   
	}
	function showActive(){
		var ids=document.getElementsByName("check");
		var idss="";
		for(var i=0;i<ids.length;i++){
			idss+=ids[i].value+",";
		}
		makeCall('<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/showActive.json?ids='+idss,showActiveBack,'fm');
	}
	function showActiveBack(json){
		if(json.succ=="1"){
			MyAlert("提示：当前页质改全动态数据已经更新！");
			__extQuery__(1);
		}
	}
	function checkActive(obj,val){
		var idss=val+",";
		makeCall('<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/showActive.json?ids='+idss,showActiveBack1,'fm');
	}
	function showActiveBack1(json){
		if(json.succ=="1"){
			MyAlert("提示：单个质改全动态数据已经更新！");
			__extQuery__(1);
		}
	}
	function exportToexcel(){
		 fm.action="<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/exportToexcelTarking.do";
	     fm.submit();
	}
</script>  

