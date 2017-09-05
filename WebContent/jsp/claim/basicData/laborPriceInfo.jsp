<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%String contextPath = request.getContextPath();%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<title>工时单价维护</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	</head>
	<body>
	<div class="wbox">
		<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;工时单价政策明细</div>
		<form name='fm' id='fm'>
		<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
			<input type="hidden" name="regionCode" id="regionCode" value="${regionCode }" />
			<input type="hidden" name="regionName" id="regionName" value="${regionName }" />
			<input type="hidden" name="wrGroupId" id="wrGroupId" value="${wrGroupId }" />
			<input type="hidden" name="wrGroupName" id="wrGroupName" value="${wrGroupName }" />
			<input type="hidden" name="changeId" id="changeId" value="${ID }" />
			<table class="table_query">
				<tr>
					<td class="table_add_2Col_label_6Letter" style="text-align:right">变更类型：</td>
					<td>
						<script type="text/javascript">
        					genSelBoxExp("changeType",<%=Constant.LABOR_CHANGE_TYPE%>,"${poValue.changType }",true,"","disabled","false","");
        				</script>
					</td>
					
					<td class="table_add_2Col_label_6Letter" style="text-align:right">政策名称：</td>
					<td>
						${poValue.policyName }
						<input name="change_value" id=""change_value"" value="${poValue.changValue }" type="hidden" class="middle_txt"/>
					</td>
					<td class="table_add_2Col_label_6Letter" style="text-align:right">加价方式：</td>
					<td>
						<script type="text/javascript">
        					genSelBoxExp("moneyType",<%=Constant.MONEY_TYPE%>,"${poValue.addType }",true,"","disabled","false","");
        				</script>
        				<select class="u-select" style="width:50px;" disabled="disabled" name="money" id="money">
							<%
							double j=-10;
							double k = 0;
							for(int i=0;i<=40;i++){
							%>
							
							<%
								if(j+k!=0.0){
									
								%>	
								<option value=<%=j+k%>><%=j+k%></option>
							<%	}
							%>
							
							
							
							<% k=k+0.5;}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td class="table_add_2Col_label_6Letter" style="text-align:right">政策编号：</td>
					<td>${poValue.policyNo }</td>
					<td class="table_add_2Col_label_6Letter" style="text-align:right">政策生效日期：</td>
					<td>
						${startDate }
					</td>
					<td class="table_add_2Col_label_6Letter" style="text-align:right">政策失效日期：</td>
					<td>
						${endDate }
					</td>
				</tr>
				<tr>
					<td class="table_add_2Col_label_6Letter" style="text-align:right">备注：</td>
					<td colspan="4">
						<textarea name="remark" id="remark" rows="5" cols="80" readonly>${poValue.remark }</textarea>
					</td>
				</tr>
				
				<!-- <tr>
				<td colspan="4">
        			<font color="red" size="3">首页新闻:</font><input class="normal_btn"  name="add4" type="button" onclick="selectMainNew();" value ='新增'/></td>
    			</tr> -->
    			<tr>
	      			<td colspan="6">
	      				<table class="table_list" id="t_news" border="1">
		        			<tr >
					          <th width="50" align="center" nowrap="nowrap" >NO </th>
					          <th width="220" align="center" nowrap="nowrap" >编码 </th>
					          <th width="400" align="center" nowrap="nowrap" >新闻名称</th>
					          <!-- <th width="80" align="center" nowrap="nowrap" >操作 </th> -->
		        			</tr>
		        			<c:if test="${!empty listNews}">
					        	<c:forEach var="newDetail" items="${listNews}" varStatus="vs">
					        		<tr >
							          <td width="50" align="center" nowrap="nowrap" >${vs.index+1 } </td>
							          <td width="220" align="center" nowrap="nowrap" ><a href="#" onclick='viewNews(${newDetail.NEWS_ID })'>${newDetail.NEWS_CODE }</a></td>
							          <td width="400" align="center" nowrap="nowrap" >${newDetail.NEWS_TITLE }</td>
							          
							         
							          <!-- <td width="80" align="center" nowrap="nowrap" >
							          	<input class="normal_btn" disabled="disabled" name="delete" type="button" value ="删除" onclick="javascript:deleteRowConfirm(this);" />
							          </td> -->
							          
							          <!-- <th width="80" align="center" nowrap="nowrap" ><a href="#" onclick='viewNews(${newDetail.NEWS_ID })'>查看</a></th> -->
							        </tr>
					        	</c:forEach>
				        	</c:if>
						</table>
					</td>
				</tr>
  		  		<!-- 经销商身份变更 -->
  		  		<table id="mileage_body"  class="table_query">
		        <tr>
		        	<td align="left" nowrap="nowrap">经销商选择：</td>
		       		<td align="left" nowrap="nowrap">
		       			<input id="dealerCode" type="hidden" name="dealerCode" value=${dealerCode }></input>
		       			<input id="dealerId" type="hidden" name="dealerId"   value=${dealerId }></input>
		       		</td>
		       	</tr>
		        <tr id="showDealer">
		          <td></td>
		          <td></td>
		          <td></td>
		        </tr>
		        </table>
  		  		
  		  		<table id="freetimes_body" style="display:none">
		        <tr>
		        	<td align="right" nowrap="nowrap">省份选择：
		        		<input type="button" disabled="disabled" class="normal_btn" value="新增" onclick="addUserRegion()"/>&nbsp;
		        	</td>
		       		<td></td>
		       		<td></td>
		       		<td></td>
		       	</tr>
		        <tr id="showRegion">
		          <td></td>
		          <td></td>
		          <td></td>
		          <td></td>
		        </tr>
		        </table>
		        
		        <table id="purchase_date_chg" style="display:none">
		        <tr>
		        	<td align="right" nowrap="nowrap">车型组：</td>
		       		<td>
						<input type="button" disabled="disabled" class="normal_btn" value="新增" onclick="addModelGroup()"/>&nbsp;
					</td>
		       		<td></td>
		       		<td></td>
		       	</tr>
		        <tr id="showModelGroup">
		          <td></td>
		          <td></td>
		          <td></td>
		          <td></td>
		        </tr>
		        </table> 		
			</table>
			<table class="table_query">
				<tr>
					<td colspan="6" style="text-align:center">
						<input class="normal_btn" name="back" type="button" onclick="history.back();" value="返回"/>	
					</td>
				</tr>
			</table>
			</div>
			</div>
		</form>
		</div>
<script>
function doCusChange(){
	/* document.getElementById("a").style.display = "none";
	document.getElementById("b").style.display = "none"; */
	
	var selectType = document.getElementById("changeType").value;
	if (selectType == '') {
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
	}
	if (selectType == 80621001) {
		document.getElementById("mileage_body").style.display = "inline";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
	}
	else if (selectType == 80621002) {
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("freetimes_body").style.display = "inline";
		document.getElementById("purchase_date_chg").style.display = "none";
	}
	else if(selectType == 80621003) {
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "inline";
	}

	/* var moneyType = document.getElementById("moneyType").value;
	if (moneyType == '') {
		document.getElementById("a").style.display = "none";
		document.getElementById("b").style.display = "none";
	}
	if (moneyType == '80631001') {
		document.getElementById("a").style.display = "";
		document.getElementById("b").style.display = "none";
	}
	if (moneyType == '80631002') {
		document.getElementById("a").style.display = "none";
		document.getElementById("b").style.display = "";
	} */
}

function viewDealerCode2(){
	var dealerCodes = $('#dealerCode')[0].value;
	var dealerIds = $('#dealerId')[0].value;
	var codes = new Array();
	var ids = new Array();
	codes = dealerCodes.substr(0,dealerCodes.length-1).split(',');
	ids = dealerIds.substr(0,dealerIds.length-1).split(',');
	ids_code = $('#dealer_code');
	if(codes!=null&&codes!=''){
	
		for(var i=0;i<codes.length;i++){
			var tb = document.getElementById("mileage_body") ;
			var tr = $('#showDealer');
			//var newTr = tr.cloneNode(true);
			//tr.insert({'after':newTr});
			var newTr =  tb.insertRow(1) ;
			//var newTd1 = newTr.insertCell();
			var j=0;
			if(ids_code!=null){
				var  flag=0;
				 if(flag==0){
					 var newTd1 = newTr.insertCell();
					 var newTd2 = newTr.insertCell();
					 newTd1.innerHTML="<input type='hidden' name='dealer_id' id='dealer_id' value='"+ids[i]+"'>";
					 newTd2.innerHTML="经销商代码:"+codes[i]+"<input type='hidden' name='dealer_code' id='dealer_code' value='"+codes[i]+"'>" ;
				}
			}
		}
	 }
}

/* function viewRegionCode2(){
    var regionCodes = $('#regionCode')[0].value;
    var regionNames = $('#regionName')[0].value;
    var codes = new Array();
	var names = new Array();

	names = regionNames.substr(0,regionNames.length-1).split(',');
	codes = regionCodes.substr(0,regionCodes.length-1).split(',');

	var dealerCodes = names;
	var dealerIds = codes;
	var codes = new Array();
	var ids = new Array();
	codes = dealerCodes;
	ids = dealerIds;
	ids_code = $('#region_name');
	if(codes!=null&&codes!=''){

		
		for(var i=0;i<codes.length;i++){
			var tr = $('#showRegion');
			var newTr = tr.cloneNode(true);
			tr.insert({'after':newTr});
			var j=0;
			if(ids_code!=null){
				var dealers = document.getElementsByName('region_name');
				var  flag=0;
				for(var k=0;k<dealers.length;k++){
					name=dealers[k].value;	
				    if(name==codes[i]){
					    flag=1;
					}
				}
				if(flag==0){
					newTr.cells[j].innerHTML="<td></td><td><input type='hidden' name='region_code' id='region_code' value='"+ids[i]+"'></td>";
					newTr.cells[j+1].innerHTML="<td>省份名称:</td><td>"+codes[i]+"<input type='hidden' name='region_name' id='region_name' value='"+codes[i]+"'></td>"
				}
			}else{
				newTr.cells[0].innerHTML="<td></td><td><input type='hidden' name='region_code' id='region_code' value='"+ids[i]+"'></td>";
				newTr.cells[1].innerHTML="<td>省份名称:</td><td>"+codes[i]+"<input type='hidden' name='region_name' id='region_name' value='"+codes[i]+"'></td>"
			}
		}
	}
} */

/* function viewModelGroup2(){
	var wrGroupId = $('#wrGroupId').value;
    var wrGroupName = $('#wrGroupName').value;
    var codes = new Array();
	var names = new Array();

	names = wrGroupName.substr(0,wrGroupName.length-1).split(',');
	codes = wrGroupId.substr(0,wrGroupId.length-1).split(',');
	
	var dealerCodes = names;
	var dealerIds = codes;
	var codes = new Array();
	var ids = new Array();
	
	codes = dealerCodes;
	ids = dealerIds;
	ids_code = $('#wrgroup_name');
	if(codes!=null&&codes!=''){
	
		for(var i=0;i<codes.length;i++){
			var tr = $('#showModelGroup');
			var newTr = tr.cloneNode(true);
			tr.insert({'after':newTr});
			var j=0;
			if(ids_code!=null){
				var dealers = document.getElementsByName('wrgroup_name');
				var  flag=0;
				for(var k=0;k<dealers.length;k++){
					name=dealers[k].value;	
				    if(name==codes[i]){
					    flag=1;
					}
				}
				if(flag==0){
					newTr.cells[j].innerHTML="<td></td><td><input type='hidden' name='wrgroup_id' id='wrgroup_id' value='"+ids[i]+"'></td>";
					newTr.cells[j+1].innerHTML="<td>车型组名称:</td><td>"+codes[i]+"<input type='text' name='wrgroup_name' id='wrgroup_name' value='"+codes[i]+"'></td>"
				}
			}else{
				newTr.cells[0].innerHTML="<td></td><td><input type='hidden' name='wrgroup_id' id='wrgroup_id' value='"+ids[i]+"'></td>";
				newTr.cells[1].innerHTML="<td>车型组名称:</td><td>"+codes[i]+"<input type='hidden' name='wrgroup_name' id='wrgroup_name' value='"+codes[i]+"'></td>"
			}
		}
	}
} */
</script>
<script type="text/javascript">
function doInit(){
   	//loadcalendar();  //初始化时间控件
   	doCusChange();
   	viewDealerCode2();
   /* 	viewRegionCode2();
   	viewModelGroup2(); */
   	/* var changeValue=$('#change_value')[0].value;
   	var value =  changeValue * 100;// + "%"; 
    var po = document.getElementById("money1").options ;
    for(var i = 0; i < po.length; i++) {
        if (po[i].value == value) {
        	po[i].selected  = true;
           }
    } */

  	var changeValueType=$('#change_value')[0].value;
   	var valueType =  changeValueType ;// + "%"; 
    var po1 = document.getElementById("money").options ;
    for(var i = 0; i < po1.length; i++) {
        if (po1[i].value == valueType) {
        	po1[i].selected  = true;
           }
    }
	
   	
}
<%-- function selectMainNew(){
	OpenHtmlWindow('<%=contextPath%>/claim/other/Bonus/newsQuery.do',800,500);
} --%>
/* function addRow(tableId,newId,newCode,newTitle){
    var addTable = document.getElementById(tableId);
	var rows = addTable.rows;
	var length = rows.length;
	var insertRow = addTable.insertRow(length);

	insertRow.className = "table_list_row1";
	insertRow.insertCell(0);
	insertRow.insertCell(1);
	insertRow.insertCell(2);
	insertRow.insertCell(3);
		addTable.rows[length].cells[0].innerHTML =  '<td>'+length+'</td>';
		addTable.rows[length].cells[1].innerHTML =  '<td><a href="#" onclick="viewNews('+newId+')">'+newCode+'</a><input type=hidden name="newsId" value="'+newId+'"/></td>';
		addTable.rows[length].cells[2].innerHTML =  '<td>'+newTitle+'</td>';
		addTable.rows[length].cells[3].innerHTML =  '<td><input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRowConfirm(this);" /></td>';
		
		return addTable.rows[length];
	} */
/* function deleteRowConfirm(obj){
	 var tabl=document.all['t_news'];
	 var index = obj.parentElement.parentElement.rowIndex;
	 
	 tabl.deleteRow(index); 
	 countSeq();
} */
/* function countSeq(){
	 var table=document.all['t_news'];

	var trs = table.getElementsByTagName('tr');//
	var rowCount=trs.length;
	var index=0;
	for(var i = 1; i <rowCount ; i++){
		 var cells = trs[i].cells;
		 cells[0].innerHTML=++index;
	}
}

function deleteRowConfirm1(obj,id){
	 var index = obj.parentElement.parentElement.rowIndex;
	 $(id).deleteRow(index);
}

function deleteRowConfirm2(obj,id){
	 var index = obj.parentElement.parentElement.rowIndex;
	 $(id).deleteRow(index);
}
function deleteRowConfirm3(obj,id){
	 var index = obj.parentElement.parentElement.rowIndex;
	 $(id).deleteRow(index);
} */

function viewNews(value){
	OpenHtmlWindow("<%=contextPath%>/claim/basicData/HomePageNews/viewNews.do?comman=2&newsId="+value,900,500);
}
<%-- function addUserRegion(){
	OpenHtmlWindow('<%=contextPath%>/sysmng/usemng/UserRegionRelation/queryRegion1.do',800,500);
}
function addModelGroup(){
	OpenHtmlWindow('<%=contextPath%>/sysmng/usemng/UserRegionRelation/queryModelGroup.do',800,500);
} --%>
function back(){
	location.href = "<%=contextPath%>/claim/basicData/LaborPriceMain/LaborPriceInit.do";
}
</script>
</body>
</html>
