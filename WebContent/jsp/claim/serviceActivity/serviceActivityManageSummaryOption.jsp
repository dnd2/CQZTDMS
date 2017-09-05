<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.List"%>
<%@page import="edu.emory.mathcs.backport.java.util.LinkedList"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.po.TtAsActivityEvaluatePO"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/common.js"></script>

<head>
<%
TtAsActivityEvaluatePO evaluatePO =(TtAsActivityEvaluatePO)request.getAttribute("evaluatePO");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动评估总结</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" language="javascript">
function doInit()
{
   loadcalendar();
}
function save(){
	fm.action="<%=contextPath%>/claim/basicData/HomePageNews/saveNews.do"
	fm.submit();
}
function delt(id,obj)
{
	 var tabl=document.all['table_info'];
	 var index = obj.parentElement.parentElement.rowIndex;
	 tabl.deleteRow(index); 
	 makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummary/ServiceActivityManagedel.json?id='+id,delBack,'fm','');
}
function delBack(json)
{
	MyAlert('删除成功');
}
function checkedLoad(){

    if('${ReStatus}' == '2')
    {
    	 if('NO' == '${typeDate}')
	     {
	    	MyAlert("活动未结束不能上报");
	    	return false;
	     }
    }else
    {
        if('NO' == '${typeDate}')
	    {
	    	MyAlert("活动未结束不能上报");
	    	return false;
	    }
        if('NO' == '${typeDate1}')
	   {
	   		MyAlert("活动已经停止上报");
	    	return false;
	   }
	   
    }
   
  
	if(!submitForm('fm')) {
		return false;
	}else{
		MyConfirm("是否确认上报？",updateLoad);
		
	}
}
function checkedLoadbao()
{
	if(!submitForm('fm')) {
		return false;
	}else{
		MyConfirm("是否确认保存？",updateLoadbao);
	}
}
function updateLoadbao()
{
	makeNomalFormCall('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummary/ServiceActivityManagedbao.json',baoBack,'fm','commitbao');
}
function baoBack(json)
{
	if(json.back == '1')
	{
		 fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummary/serviceActivityManageSummaryInit.do";
    	 fm.submit();
		
	}else{
		MyAlert('保存失败！请与管理员联系');
	}
}

//服务活动评估总结上报
function updateLoad(){
		fm.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageSummary/serviceActivitySummaryOption.do";
		fm.submit();
	}
	
	function addRow(tableId){
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		var selObj=document.getElementById('code_type');
		 var code_type = document.getElementById('code_type').value;
		var selectOptionText=selObj.options[selObj.selectedIndex].innerText;
		addTable.rows[length].cells[0].align='right';	
		addTable.rows[length].cells[0].align='right';		
		addTable.rows[length].cells[0].align='right';	
		addTable.rows[length].cells[0].align='right';		
		addTable.rows[length].cells[0].innerHTML = selectOptionText; 
		addTable.rows[length].cells[1].innerHTML = '<input type="hidden" name="code_typena" id="code_type"  datatype="0,is_null,50"   value="'+code_type+'" size="8"/><input type="text" name="part_name" id="part_name"  datatype="0,is_null,50"   value="" size="20"/>'; 
		addTable.rows[length].cells[2].innerHTML = '<input type="text" name="per_count" id="per_count"  datatype="0,is_digit,4"   value="" size="8"/>'; 
		addTable.rows[length].cells[3].innerHTML = '<input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow(this);" />'; 
		return addTable.rows[length];
	}
	function deleteRow(obj){
		 var tabl=document.all['otherTableId'];
		 var index = obj.parentElement.parentElement.rowIndex;
		 tabl.deleteRow(index); 
		 countSeq();
	}
	
	
	
	function deleteRow02(obj)
	{
		 var tabl=document.all['actTableId'];
		 var index = obj.parentElement.parentElement.rowIndex;
		 tabl.deleteRow(index); 
		 countSeq();
	}
	var i = 100
	function addRowtype(tableId){
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		insertRow.insertCell(4);
		insertRow.insertCell(5);
		addTable.rows[length].cells[0].align='left';	
		addTable.rows[length].cells[0].align='left';		
		addTable.rows[length].cells[0].align='left';	
		addTable.rows[length].cells[0].align='left';		
		addTable.rows[length].cells[0].innerHTML = '<input type="text" name="W_name" id="W_name"  datatype="0,is_null,90"   value="" size="8"/>'; 
		addTable.rows[length].cells[1].innerHTML = '<input type="text" name="media_name" id="media_name"  datatype="0,is_null,90"   value="" size="8"/>'; 
		var type = "'";
		addTable.rows[length].cells[2].innerHTML = '<input name="publish_date" class="short_txt" id="publish_date'+i+'"  maxlength="10" datatype="0,is_date,10"/> <input class="time_ico" onclick="showcalendar(event, '+type+'publish_date'+i+type+', false);" value=" " type="button" />'; 
		addTable.rows[length].cells[3].innerHTML = '<input type="text" name="W_add" id="W_add"  datatype="0,is_null,90"   value="" size="8"/>'; 
		
		addTable.rows[length].cells[4].innerHTML = '<input type="text" name="conduct_cont" id="conduct_cont"  datatype="0,is_null,1000"   value="" size="30"/>'; 
		addTable.rows[length].cells[5].innerHTML = '<input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow02(this);" />'; 
		i++;
		return addTable.rows[length];
	}
	
	function  ffTableId()
	{
	   if('${Stype}' != '0')
	   {
	   	var ffTable = document.getElementById('ffTableId');
		var tr = ffTable.insertRow(-1);
		var td1 = tr.insertCell(-1);
		var td2 = tr.insertCell(-1);
		var td3 = tr.insertCell(-1);
		var td4 = tr.insertCell(-1);
		td1.style.color = "red";
		td1.innerHTML = '合计种金额';
		
		td2.colSpan = 2;
		td2.align = "center";
		td2.innerHTML = '<input disabled="disabled" id="SAMOUNT"  type="text" value="${SAmount.YAMOUNT}"/>';
		td3.colSpan = 2;
		td3.align = "center";
		td3.innerHTML = '${SAmount.ADD_ITEM_AMOUNT}';
		
		var trs = ffTable.rows;
		var tds = trs[1].insertCell(-1);
		tds.rowSpan = trs.length-1;
		tds.align = "center";
		tds.innerHTML = '昌河汽车承担';
	   }
		
		
	}
	
	function  rrTableId()
	{
		if('${Ztype}' != '0')
		{
			var ffTable = document.getElementById('rrTableId');
			var tr = ffTable.insertRow(-1);
			var td1 = tr.insertCell(-1);
			var td2 = tr.insertCell(-1);
			var td3 = tr.insertCell(-1);
			var td4 = tr.insertCell(-1);
			td1.style.color = "red";
			td1.innerHTML = '合计种金额';
			
			td2.colSpan = 2;
			td2.align = "center";
			td2.innerHTML = '<input disabled="disabled" id="ZAMOUNT" type="text" value="${ZAmount.YAMOUNT}"/>';
			td3.colSpan = 2;
			td3.align = "center";
			td3.innerHTML = '${ZAmount.ADD_ITEM_AMOUNT}';
			
			var trs = ffTable.rows;
			var tds = trs[0].insertCell(-1);
			tds.rowSpan = trs.length;
			tds.width = '20%';
			tds.align = "center";
			tds.innerHTML = '经销商承担';
		
		}
		
	}
	
	function blurBack()
	{
		 var monney = document.getElementsByName('monney');
		 var monney1 = document.getElementsByName('monney1');
		 var SAMOUNT = 0;
		 var ZAMOUNT =0;
		 
		 if(monney.length > 0)
		 {
		 	 for(var i = 0 ;i < monney.length; i++)
			 {
			    SAMOUNT = SAMOUNT +  Number(formatNumber(monney[i].value));
			 }
			 document.getElementById("SAMOUNT").value = SAMOUNT;
		 }
		 
		 if(monney1.length > 0)
		 {
		 	 for(var j = 0 ;j < monney1.length; j++)
			 {
			    ZAMOUNT = ZAMOUNT +  Number(formatNumber(monney1[j].value))
			 }
			 document.getElementById("ZAMOUNT").value = ZAMOUNT;
		 }
		
	}
		function formatNumber(value)
		{
			if(value)
			{ 
				return value;
			}
			else return 0;
		}
</script>
</head>

<body onload="doInit();ffTableId();rrTableId();">
	<div class="navigation">
	     <img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动评估
	</div>
 <form name="fm" id="fm" method="post">
  <table width="100%" class="tab_edit">
	     <tr>
		      <th colspan="4" align="left">
		      		<img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息
		      </th >
	     </tr>
          <tr>
            <td width="19%"   align="right">主题编号：</td>
            <td width="30%" align="left" ><c:out value="${ttAsActivityBean.subject_no}"></c:out> </td>
            <td width="15%" align="right">主题名称： </td>
            <td width="36%" align="left">
                  <input  type="hidden" name="subjectid" id="subjectid"   value="${ttAsActivityBean.subjectId}"/>
                   <input type="hidden" name="evaluateid" id="evaluateid"   value="${ttAsActivityBean.evaluateid}"/>
          		  <c:out value="${ttAsActivityBean.subject_name}"></c:out>
            </td>
          </tr>
          <tr>
            <td align="right">活动类型：</td>
            <td align="left">
                <script type='text/javascript'>
				       var activityType=getItemValue('${ttAsActivityBean.activityType}');
				       document.write(activityType) ;
				     </script>
            </td> 
            <td align="right">责任人：</td>
            <td>
            	<c:out value="${ttAsActivityBean.duty_person}"></c:out>
            </td>
          </tr>
          <tr>
            <td  align="right">活动日期：</td>
            <td  align="left">
            	 ${ttAsActivityBean.subject_start_date}<span style="PADDING-LEFT: 2px; WIDTH: 7px; HEIGHT: 18px; COLOR: red; FONT-SIZE: 9pt"></span>&nbsp;至&nbsp;${ttAsActivityBean.subject_end_date}
            </td> 
            <td  align="right">所在区域：</td>
            <td  align="left">
            	${org_name} 
            </td>
          </tr>
          <tr>
            <td  align="right">老带新信息留存数：</td>
            <td align="left" >
                <input type="text"  name="onAmount"  id="onAmount" value="${ttAsActivityBean.on_amount}"   datatype="1,is_digit,10" /></td>
            <td align="right">老带新成交数：</td>
            <td align="left">
                <input type="text"  name="onCamount" id="onCamount" value="${ttAsActivityBean.on_camount}"  datatype="1,is_digit,10" /></td>
          </tr>
          <tr>
            <td  align="right">
           		 用户反应及自我评价：</td>
            <td  colspan="3" align="left" >
	            <span class="tbwhite">
	                <textarea  name="evaluate"  id="evaluate"    rows="5" cols="80" datatype="0,is_null,200">${ttAsActivityBean.evaluate}</textarea>
	            </span>
            </td>
          </tr>
          <tr>
            <td  align="right">建议及改进措施：</td>
            <td  colspan="3" align="left" >
	            <span class="tbwhite">
	              <textarea  name="measures"  id="measures"   rows="5" cols="80" datatype="0,is_null,200">${ttAsActivityBean.measures}</textarea>
	            </span>
            </td>
          </tr>
		  <tr>
            <td  align="left">&nbsp;</td>
            <td  colspan="3" align="left" >&nbsp;</td>
          </tr>
        </table>
        <br />
        <table id="otherTableId" width="100%" class="tab_list">
		  <tr>
			  <th colspan="4" align="left">准备项目
			   <script type="text/javascript">
				 genSelBoxExp("code_type",<%=Constant.PERPAR_TYPE%>,"",false,"short_sel","","true",'');
		 		</script>
		 		<input type="button" class="normal_btn"  value="新增" onclick="addRow('otherTableId');" id="" />
			  </th>
		  </tr>
		  <tr>
		    <th width="20%"> 项目名称</th>
		  	<th width="40%"> 物品名称</th>
		  	<th width="20%"> 准备数量</th>
		  	<th width="20%"> 操作</td>
		 </tr>
		  <c:forEach var="perList" items="${perList}">
			   <tr class="table_list_row1">
			    <td align="left">
			    <script type='text/javascript'>
			       var activityKind=getItemValue('${perList.codeType}');
			       document.write(activityKind) ;
			     </script>
			    </td>
			  	<td>
				  	<input type="hidden" name="code_typena" id="code_type"  datatype="0,is_null,50"   value="${perList.codeType}" size="8"/>
				  	<input type="text" name="part_name" id="part_name"  datatype="0,is_null,50"   value="${perList.partName}" size="20"/>
			  	</td>
			  	<td><input type="text" name="per_count" id="per_count"  datatype="0,is_digit,4"   value="${perList.perCount}" size="8"/></td>
			  	<td><input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow(this);" /></td>
			  </tr>
		  </c:forEach>
		  	
  	</table>
  	
  	<br/>
  	<table id="actTableId"  width="100%" class="tab_list">
		  <tr>
			  <th colspan="6" align="left"> 活动宣传&nbsp;&nbsp;&nbsp;
		 		<input type="button" class="normal_btn"  value="新增" onclick="addRowtype('actTableId');" id="" />
			  </th>
		  </tr>
		  <tr>
		    <th width="15%">宣传方式</th>
		    <th width="20%">媒体名称</th>
		  	<th width="10%">发布时间</th>
		  	<th width="30%">链接/版面/音频/视频/其他</th>
		  	<th width="20%">宣传主题</th>
		  	<th width="5%">操作</th>
		  </tr>
		  <% int countAll=0; %> 
		  <c:forEach var="conList" items="${conList}">
			   <tr class="table_list_row1">
			    <td><input type="text" name="W_name" id="W_name"  datatype="0,is_null,90"   value="${conList.WNAME}" size="8"/></td>
			    <td><input type="text" name="media_name" id="media_name"  datatype="0,is_null,90"   value="${conList.MEDIA_NAME}" size="8"/></td>
			    <td>
			  	<input name="publish_date" value="${conList.PUBLISHDATE}" class="short_txt" id="publish_date<%=countAll %>"  maxlength="10" datatype="0,is_date,10"/> 
			  	<input class="time_ico" onclick="showcalendar(event,'publish_date<%=countAll%>', false);" value=" " type="button" />
			  	</td>
			  	<td><input type="text" name="W_add" id="W_add"  datatype="0,is_null,90"   value="${conList.WADD}" size="8"/></td>
			  	<td><input type="text" name="conduct_cont" id="conduct_cont"  datatype="0,is_null,1000"   value="${conList.CONDUCTCONT}" size="30"/></td>
			  	<td><input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow02(this);" /></td>
			  </tr>
			   <%  countAll=countAll +1; %> 
		  </c:forEach>
		  
  	</table>
  	<br/>
  	<table  id="ttTableId" width="100%" class="tab_list">
  	<tr>
  	<th width="20%">提升项目名称</th>
  	<th width="20%">提升目标数值</th>
  	<th width="20%">活动前平均数据</th>
  	<th width="20%">活动区间数据</th>
  	<th width="20%">增长%</th>
  	</tr>
  	<tr>
	  	<td>进站量(台)</td>
	  	<td><input type="text" name="pull_in_Num" id="pull_in_Num"  datatype="1,is_digit,8"   value="${ttAsActivityBean.pull_in_num}" size="8"/></td>
	  	<td><input type="text" name="pull_in_mean" id="pull_in_mean"  datatype="1,is_digit,8"   value="${ttAsActivityBean.pull_in_mean}" size="8"/></td>
	  	<td><input type="text" name="pull_in_region" id="pull_in_region"  datatype="1,is_digit,8"   value="${ttAsActivityBean.pull_in_region}" size="8"/></td>
	  	<td><input type="text" name="pull_in_incre" id="pull_in_incre"  datatype="1,is_double,5" decimal="2"  value="${ttAsActivityBean.pull_in_incre}" size="8"/></td>
  	</tr>
  	<tr>
	  	<td>老客户返回量</td>
	  	<td><input type="text" name="customer_Num" id="customer_Num"  datatype="1,is_digit,8"   value="${ttAsActivityBean.customer_num}" size="8"/></td>
	  	<td><input type="text" name="customer_mean" id="customer_mean"  datatype="1,is_digit,8"   value="${ttAsActivityBean.customer_mean}" size="8"/></td>
	  	<td><input type="text" name="customer_region" id="customer_region"  datatype="1,is_digit,8"   value="${ttAsActivityBean.customer_region}" size="8"/></td>
	  	<td><input type="text" name="customer_incre" id="customer_incre"  datatype="1,is_double,5" decimal="2"  value="${ttAsActivityBean.customer_incre}" size="8"/></td>
  	</tr>
  	<tr>
	  	<td>客单价(元)</td>
	  	<td><input type="text" name="price_Num" id="price_Num"  datatype="1,is_digit,8"   value="${ttAsActivityBean.price_num}" size="8"/></td>
	  	<td><input type="text" name="price_mean" id="price_mean"  datatype="1,is_digit,8"   value="${ttAsActivityBean.price_mean}" size="8"/></td>
	  	<td><input type="text" name="price_region" id="price_region"  datatype="1,is_digit,8"   value="${ttAsActivityBean.price_region}" size="8"/></td>
	  	<td><input type="text" name="price_incre" id="price_incre"  datatype="1,is_double,5" decimal="2"  value="${ttAsActivityBean.price_incre}" size="8"/></td>
  	</tr>
  	<tr>
	  	<td>营业额(元)</td>
	  	<td><input type="text" name="open_Num" id="open_Num"  datatype="1,is_digit,8"   value="${ttAsActivityBean.open_num}" size="8"/></td>
	  	<td><input type="text" name="open_mean" id="open_mean"  datatype="1,is_digit,8"   value="${ttAsActivityBean.open_mean}" size="8"/></td>
	  	<td><input type="text" name="open_region" id="open_region"  datatype="1,is_digit,8"   value="${ttAsActivityBean.open_region}" size="8"/></td>
	  	<td><input type="text" name="open_incre" id="open_incre"  datatype="1,is_double,5" decimal="2"  value="${ttAsActivityBean.open_incre}" size="8"/></td>
  	</tr>
  	
  	</table>
  	<br/>
  	<table id="ffTableId" width="100%" class="tab_list">
  	<tr>
	  	<th width="20%">活动项目</th>
	  	<th width="15%">预算数量</th>
	  	<th width="15%">预算金额</th>
	  	<th width="15%">发生数量</th>
	  	<th width="15%">发生金额</th>
	  	<th width="20%">备注</th>
  	</tr>
  	<% int j = 100; %>
  	<c:forEach var="SList" items="${SList}">
	  	<tr>
	  	<td>
		  	 <script type='text/javascript'>
		       var activityKind=getItemValue('${SList.ADD_ITEM_CODE}');
		       document.write(activityKind) ;
		     </script>
	  	</td>
	  	
	  	<td align="center"><input type="text"   id="count_sum<%= j %>" datatype="0,is_digit,4"  name="count_sum" value="${SList.YACCOUNT}" />  </td>
	  	<td align="center"><input type="text" blurback="true"  datatype="0,is_double,5" decimal="2" id="monney<%= j %>" name="monney" value="${SList.YAMOUNT}" /> </td>
	  	<td align="center">${SList.ACOUNT}<input type="hidden" name="ADD_ITEM_CODE" value="${SList.ADD_ITEM_CODE}" />  </td>
	  	<td align="center">${SList.ADD_ITEM_AMOUNT}</td>
	  	</tr>
	  	<% j++; %>
  	</c:forEach>
  	</table>
  	<table id="rrTableId"    width="100%" class="tab_list" >
  	<c:forEach var="ZList" items="${ZList}">
	  	<tr>
	  	<td width="20%" >
	  	   <script type='text/javascript'>
		       var activityKind=getItemValue('${ZList.ADD_ITEM_CODE}');
		       document.write(activityKind) ;
		     </script>
	  	</td>
	  	<td width="15%"><input type="text" datatype="0,is_digit,4"  id="count_sum<%= j%>" name="count_sum1" value="${ZList.YACCOUNT}" />  </td>
	  	<td width="15%" ><input type="text" blurback="true"  datatype="0,is_double,5" decimal="2" id="monney<%= j %>"  name="monney1" value="${ZList.YAMOUNT}" /> </td>
	  	<td width="15%" >${ZList.ACOUNT} <input type="hidden" name="ADD_ITEM_CODE1" value="${ZList.ADD_ITEM_CODE}" /></td>
	  	<td width="15%" >${ZList.ADD_ITEM_AMOUNT}</td>
	  	</tr>
	  	<% j++; %>
  	</c:forEach>
  	
  	</table>
  	<br/>
     <table width="100%">
       <tr> 
         <td height="12" align=center>
           <input type="button" class="normal_btn"  value="保存" onclick="checkedLoadbao();" id="commitbao" />
		  &nbsp;&nbsp;
		  <input type="button" class="normal_btn"  value="上报" onclick="checkedLoad();" id="tijiao" />
		  &nbsp;&nbsp;
		  <input type="button" onClick="javascript:history.go(-1);"  class="normal_btn"  value="返回"/>
	    </td>
	  </tr>
     </table>
  	<br/>
	<table class="tab_edit" width="100%" id="file">
			<input type="hidden" id="fjids" name="fjids"/>
		    <tr>
		        <th align="left">
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
				     <input type="button" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
				</th>
			</tr>
			<tr>
	    		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
	  		</tr>
	  		<% List<FsFileuploadPO> fileList = (List<FsFileuploadPO>) request.getAttribute("FileuploadPO"); %>
	  		<%for(int i=0;i<fileList.size();i++) { %>
		 	 <script type="text/javascript">
		 	 addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>');
		 	 </script>
		<%}%>
	</table> 
</form>
</body>
</html>