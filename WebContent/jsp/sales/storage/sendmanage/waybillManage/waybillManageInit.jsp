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
<title>运单生成管理 </title>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>运单生成管理
	</div>
<form id="fm" name="fm" method="post" >
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
	<tr>
		<td class="right">选择区域：</td>
		 <td align="left">
            <input type="text" maxlength="20"  id="orgCode" name="orgCode" value="" size="15" readonly="readonly"/>
             <input type="hidden" id="ORGID" name="ORGID" value="" size="15" readonly="readonly"/>
			<input name="obtn" id="obtn"  class="mini_btn" type="button"  value="&hellip;"   onclick="showOrg('orgCode','ORGID' ,'true','');"/>
		 	<input class="normal_btn" type="button" value="清空" onclick="clrTxt('orgCode');clrTxt('ORGID');"/>
		 </td>
	<td class="right">选择经销商：</td>
		<td align="left">
      		<input name="dealerName" type="text" maxlength="20"  id="dealerName" class="middle_txt" value=""  readonly="readonly"/>
            <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode', 'dealerId', 'true', '', 'true','','<%=Constant.DEALER_TYPE_DVS %>,<%=Constant.DEALER_TYPE_DP %>','dealerName');" value="..." />
    		<input type="button" class="normal_btn" onclick="clrTxt('dealerName');clrTxt('dealerId');" value="清 空" id="clrBtn" />
			<input name="dealerCode" type="hidden" id="dealerCode" class="middle_txt" value="" />
			<input type="hidden" name="dealerId" id="dealerId"  class="middle_txt"  />
		</td>
	</tr>
	<tr class="csstr" align="center">	 
 	 <td class="right">订单类型：</td> 
	  <td align="left">
		 <label>
				<script type="text/javascript">
						genSelBoxExp("ORDER_TYPE",1020,"",true,"u-select","","false",'10201003,10201004,10201005,10201006');
					</script>
			</label>
	  </td> 
	 <td class="right" width="15%">选择物料组：</td>
      <td align="left">
      	<input type="text" maxlength="20"  class="middle_txt" name="groupCode" size="15"  value="" id="groupCode" />
      	<input type="hidden" class="middle_txt" name="groupId" size="15"  value="" id="groupId" />
		<input name="button1" id="button1" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupId','false','','');" value="..." />
		<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');clrTxt('groupId');"/>
      </td>
  </tr> 
  <tr class="csstr" align="center">	 
 	<td class="right">发运申请号：</td> 
	  <td align="left">
		  <input type="text" maxlength="20"  id=ORDER_NO name="ORDER_NO" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
	  </td>	 
	 <td class="right">组板号：</td> 
	  <td align="left">
		  <input type="text" maxlength="20"  id="BO_NO" name="BO_NO" datatype="1,is_digit_letter,30" maxlength="30" class="middle_txt" size="15" />
	  </td>	 
  </tr> 
  <tr class="csstr" align="center">	 
 	 <td class="right">承运商：</td> 
	  <td align="left">
		 <select name="LOGI_NAME" id="LOGI_NAME" class="selectlist" >
		 	<option value="">--请选择--</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
	  </td> 	 
	<td class="right">发票号：</td> 
	  <td align="left">
		  <input type="text" maxlength="20"  id="INVOICE_NO" datatype="1,is_digit_letter,30" maxlength="30" name="INVOICE_NO" class="middle_txt" size="15" />
	  </td>	 
  </tr> 
  
<tr class="csstr" align="center">	
	 <td class="right" nowrap="true">组板日期：</td>
   <td align="left" nowrap="true">
			<input name="BO_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="BO_STARTDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'BO_STARTDATE', false);" />  	
             &nbsp;至&nbsp;
             <input name="BO_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="BO_ENDDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'BO_ENDDATE', false);" /> 
		</td>	 
	 <td class="right" nowrap="true">配车日期：</td>
     <td align="left" nowrap="true">
			<input name="ALLOCA_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="ALLOCA_STARTDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'ALLOCA_STARTDATE', false);" />  	
             &nbsp;至&nbsp;
             <input name="ALLOCA_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="ALLOCA_ENDDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'ALLOCA_ENDDATE', false);" /> 
		</td>	
  </tr> 
  <tr align="center">
  <td colspan="4" class="table_query_4Col_input" style="text-align: center">
  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" /> 
    	   <input name="button42" type="button" class="normal_btn" style="BORDER-BOTTOM: #5e7692 1px solid; BORDER-LEFT: #5e7692 1px solid; BACKGROUND: #eef0fc; COLOR: #1e3988; BORDER-TOP: #5e7692 1px solid; BORDER-RIGHT: #5e7692 1px solid"  value="生成运单"  onclick="shengcyund();" />  	
    	  <input type="button" id="queryBtn" class="normal_btn" style="width:80px;"  value="保存物流信息" onclick="saveLoiInfo();" />       </td>
  </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>

</body>
<script type="text/javascript" >
function aa(){
	MyAlert(document.getElementById("groupId").value);
}
var myPage;
//查询路径           
var url = "<%=contextPath%>/sales/storage/sendmanage/WaybillManage/allocaAdjustQuery.json";
							
var title = null;
var columns = [
			//{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")'/>",sortable: false,dataIndex: 'BO_ID',renderer:myCheckBox},
			{id:'action',header: "选择",sortable: false,dataIndex: 'BO_ID',renderer:myCheckBox},
			{header: "组板号",dataIndex: 'BO_NO',align:'center'},
//			{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
//			{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
			{header: "发运申请号",dataIndex: 'ORDER_NO',align:'center'},
//			{header: "收货地址",dataIndex: 'REC_DEALER_ADD',align:'center'},
//			{header: "产地",dataIndex: 'AREA_NAME',align:'center'},
			{header: "发票号",dataIndex: 'INVOICE_NO',align:'center'},
			{header: "承运商",dataIndex: 'LOGI_NAME',align:'center',renderer:mySelect},
//			{header: "计划处审核日期",dataIndex: 'PLAN_CHK_DATE',align:'center'},
//			{header: "财务审核日期",dataIndex: 'FIN_CHK_DATE',align:'center'},
			//{header: "组板人",dataIndex: 'NAME',align:'center'},
			{header: "组板时间",dataIndex: 'BO_DATE',align:'center'},
			{header: "配车日期",dataIndex: 'ALLOCA_DATE',align:'center'},
			{header: "组板数量",dataIndex: 'BO_NUM',align:'center'},
			{header: "配车数量",dataIndex: 'ALLOCA_NUM',align:'center'},
			{header: "出库数量",dataIndex: 'OUT_NUM',align:'center'},
			{header: "燃油费调节系数",dataIndex: 'FUEL_COEFFICIENT',align:'center'},
			{header: "操作",dataIndex: 'BO_ID',sortable: false, align:'center',renderer:myLink}
	      ];


//全选checkbox
function myCheckBox(value,metaDate,record){
	//return String.format("<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' />");
	return String.format("<input type='radio'  id='groupIds' name='groupIds' value='" + value + "' />");
}
//初始化    
function doInit(){
	//日期控件初始化
	//__extQuery__(1);
}
//清空数据
function clrTxt(txtId){
	document.getElementById(txtId).value = "";
}

function mySelect(value,metaDate,record){
	var format="";//显示值
	var strOption="<SELECT id='"+record.data.BO_ID+"logiName' name='logiName'>";
     <%
     	for(int i=0;i<list.size();i++){
     		Map map=(Map)list.get(i);
     	%>
     	if(record.data.LOGI_ID==<%=map.get("LOGI_ID")%>){
     		strOption+='<option value=<%=map.get("LOGI_ID")%> selected=selected><%=map.get("LOGI_NAME")%></option>';
     	}else{
     		strOption+='<option value=<%=map.get("LOGI_ID")%>><%=map.get("LOGI_NAME")%></option>';
     	}
     	<%	
     	}
     %>
     strOption+="</SELECT>";
     format=strOption;
	return String.format(format);
}


/**function shengcyund(){
	var b=0;
	var arrayObj = new Array(); 
	var arrayDerlerid = new Array(); 
	var logiIds = new Array(); 
	var areaids = new Array(); 
	var orderids = new Array();
	var fayunderids = new Array(); 
	var dinghuoderids = new Array(); 
	arrayObj=document.getElementsByName("groupIds");
	arrayDerlerid=document.getElementsByName("delerIds");//运送地址
	logiIds=document.getElementsByName("logiIds");
	areaids=document.getElementsByName("areaids");
	orderids=document.getElementsByName("orderids");
	fayunderids =document.getElementsByName("fayunDealerid");//发运经销商ID
	dinghuoderids=document.getElementsByName("tsaDealerid");//订货经销商ID
	var k;
	var h;
	var d;
	var w;
	var r;
	var l;
	for(var i=0;i<arrayObj.length;i++){
		if(arrayObj[i].checked){
			if( b==0 ){
			k=arrayDerlerid[i].value;
			h=logiIds[i].value;
		    d=areaids[i].value;
		    w=orderids[i].value;
		    r=fayunderids[i].value;
		    l=dinghuoderids[i].value;
			b=1;//有选中
			}
			if(k !=arrayDerlerid[i].value){
				MyAlert("请选择同一收货地址！");
				return ;
				}

			if(h !=logiIds[i].value){
				MyAlert("请选择同一物流商！");
				return ;
				}
			if(d !=areaids[i].value){
				MyAlert("请选择同一产地！");
				return ;
				}
			if(w !=orderids[i].value){
				MyAlert("请选择同一发票号！");
				return ;
				}
			if(r !=fayunderids[i].value){
				MyAlert("请选择同一发运经销商！");
				return ;
				}
			if(l !=dinghuoderids[i].value){
				MyAlert("请选择同一订货经销商！");
				return ;
				}
		}
		
	}
	if(b==0){
		MyAlert("请先选择运单信息！");
		return ;
	}
	MyConfirm("确认生成运单！",sendAssignment);	
}*/
function shengcyund(){
	var flag = false;
	arrayObj=document.getElementsByName("groupIds");
	for(var i=0;i<arrayObj.length;i++){
		if(arrayObj[i].checked){
			flag = true;
		}
		
	}
	if(!flag){
		MyAlert("请先选择运单信息！");
		return;
	}
	//MyConfirm("确认生成运单！",sendAssignment);
	sendAssignment();
}
function sendAssignment(){
	    var fm = document.getElementById('fm');
	    fm.action = "<%=contextPath%>/sales/storage/sendmanage/WaybillManage/allocaAdjustUpdate.do";
	    fm.submit();

}
	

function myLink(value,meta,record){
    var boNo=record.data.BO_NO;
    var link="<a href='javascript:void(0);' onclick='updateSend(\""+value+"\",\""+boNo+"\")'>[查看]</a>";
		return String.format(link);
}
function updateSend(value,boNo){
	 var fm = document.getElementById('fm');
	    fm.action = "<%=contextPath%>/sales/storage/sendmanage/WaybillManage/updateAllocaSeachInit.do?Id="+value+"&boNo="+boNo;
	    fm.submit();
	 	
 }
 
 function saveLoiInfo(){
	 var radio = document.getElementsByName('groupIds');
	 var boId = '';
	 var flag = false;
	 for(var i=0;i<radio.length;i++) {
			if(radio[i].checked) {
				boId = radio[i].value;
				flag = true;
			}
		} 
	 if(!flag){
		 MyAlert('请先选择运单信息！'); 
		return;
	 }
	 MyConfirm('确认修承运商信息？',function(){
		 var value = document.getElementById(boId+'logiName').value; 
		 var url =  "<%=contextPath%>/sales/storage/sendmanage/WaybillManage/updateLoinfo.json?boId="+boId+"&logoinfo="+value;
		 makeFormCall(url,function(json){
				if(json.result == "1") {
					MyAlert("操作成功!");
					__extQuery__(1);
				} else {
					MyAlert("操作失败,请刷新后重试!");
				}
			},'fm');
	 });
	
 }
</script>
</html>