<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>投诉查询</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
   		__extQuery__(1);
	}
	
	function initDate(){
		var   Nowdate=new   Date();       
		document.getElementById('dateStart').value = DateUtil.Format("yyyy-MM-dd",Nowdate);  
		document.getElementById('dateEnd').value = DateUtil.Format("yyyy-MM-dd",Nowdate);  
	}
	
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 投诉咨询管理 &gt;投诉咨询处理（坐席）</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<input type="hidden" id="id" name="id" value="0">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />查询条件</th>
			
			<tr>
				<td align="right" nowrap="true">单据单号：</td>
				<td align="left" nowrap="true">
					<input type="text" id="cpNo" name="cpNo" class="middle_txt"/>
				</td>
				<td align="right" nowrap="true">单据类型：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("voucherType",<%=Constant.VOUCHER_TYPE%>,"",true,"short_sel","","false",'');
					</script>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">单据状态：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("voucherStatus",<%=Constant.VOUCHER_STATUS%>,"",true,"short_sel","","false",'');
					</script>
				</td>
				<td align="right" nowrap="true">受理时间：</td>
				<td align="left" nowrap="true">
					<input name="acceptStart" type="text" group="dealStart,dealEnd" datatype="1,is_date,10" class="short_time_txt" id="dealStart" readonly="readonly"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'dealStart', false);" />  	
		             &nbsp;至&nbsp;
		            <input name="acceptEnd" type="text" group="dealStart,dealEnd" datatype="1,is_date,10" class="short_time_txt" id="dealEnd" readonly="readonly"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'dealEnd', false);" /> 
				</td>	
			</tr>
			
			<tr>
				<td align="right" nowrap="true">单据级别：</td>
				<td align="left" nowrap="true">
					<script type="text/javascript">
						genSelBoxExp("level",<%=Constant.COMPLAINT_LEVEL%>,"",true,"short_sel","","false",'');
					</script>
				</td>
				<td align="right" nowrap="true">关闭时间：</td>
				<td align="left" nowrap="true">
					<input name="dealStart" type="text" group="dealStart,dealEnd" datatype="1,is_date,10" class="short_time_txt" id="dealStart" readonly="readonly"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'dealStart', false);" />  	
		             &nbsp;至&nbsp;
		            <input name="dealEnd" type="text" group="dealStart,dealEnd" datatype="1,is_date,10" class="short_time_txt" id="dealEnd" readonly="readonly"/> 
					<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'dealEnd', false);" /> 
				</td>		
			 </tr>
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查 询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
					&nbsp;
          			<input id="addBtn" name="addBtn" type="button" value="新 增" class="normal_btn" onclick="addComplaint();" />
        		</td>
			</tr>
		</table>
		
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
<script type="text/javascript">
	function addComplaint(){
		fm.action = '<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptZx/complaintAcceptInit.do';
		fm.submit();
	}
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/complaintConsult/ComplaintQuery/queryComplaintSearch.json";
				
	var title = null;

	var columns = [				
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'CPID',renderer:myLink},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "业务类型",dataIndex: 'BIZTYPE',align:'center',renderer:getItemValue},	
				{header: "单据级别",dataIndex: 'CPLEVEL',align:'center',renderer:getItemValue},
				{header: "内容类型",dataIndex: 'BIZCONT',align:'center',renderer:getItemValue},	
				{header: "单据号",dataIndex: 'CPNO',align:'center'},				
				{header: "受理时间", dataIndex: 'CPACCDATE', align:'center'},
				{header: "受理人",dataIndex: 'ACUSER',align:'center'},
				{header: "客户姓名",dataIndex: 'CTMNAME',align:'center'},
				{header: "客户电话",dataIndex: 'PHONE',align:'center'},
				{header: "联系人姓名",dataIndex: 'CP_CONCAT_PERSON',align:'center'},
				{header: "联系人电话",dataIndex: 'CP_CONCAT_PHONE',align:'center'},
				{header: "省份", dataIndex: 'PRO', align:'center'},
				{header: "城市",dataIndex: 'CITY',align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "购车日期",dataIndex: 'BDATE',align:'center'},
				{header: "备注",dataIndex: 'REMARK',align:'center'},
				{header: "关闭时间", dataIndex: 'CP_CLOSE_DATE', align:'center'}
		      ];

	function myLink(value,meta,record){
		var str1="<input name='detailBtn' type='button' class='middle_btn' onclick='complaintSearch(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='查看'/>&nbsp;";
		var str2="<input name='detailBtn' type='button' class='middle_btn' onclick='complaintDealerSet(\""+ record.data.CPID +"\",\""+ record.data.STATUS +"\")' value='分派'/>&nbsp;";
		var str3="<input name='detailBtn' type='button' class='middle_btn' onclick='updateComplaint(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='修改'/>&nbsp;";
		var str4="<input name='detailBtn' type='button' class='middle_btn' onclick='complaintClose(\""+ record.data.CPID +"\",\""+ record.data.STATUS +"\")' value='关闭'/>&nbsp;";
		var str5="<input name='detailBtn' type='button' class='middle_btn' onclick='comComplaint(\""+ record.data.CPID +"\")' value='处理'/>&nbsp;";
		var str=str1;
		if(record.data.STATUS == <%=Constant.VOUCHER_STATUS_01%>){
			if(record.data.BIZTYPE==<%=Constant.VOUCHER_TYPE_02%>){
				str+=str2+str3+str5;
			}
			if(record.data.BIZTYPE==<%=Constant.VOUCHER_TYPE_01%>){
				str+=str2+str3;
			}
		}else{
		if(record.data.STATUS == <%=Constant.VOUCHER_STATUS_02%> ){
			if(record.data.BIZTYPE==<%=Constant.VOUCHER_TYPE_02%>){
				str+=str2+str5+str4;
			}
			if(record.data.BIZTYPE==<%=Constant.VOUCHER_TYPE_01%>){
				str+=str2+str5
			}
		}
		if(record.data.STATUS == <%=Constant.VOUCHER_STATUS_06%> ){
			if(record.data.BIZTYPE==<%=Constant.VOUCHER_TYPE_02%>&&record.data.ISSELF=="0"){
				str+=str4+str5;
				}
			}
		if(record.data.STATUS == <%=Constant.VOUCHER_STATUS_07%> ){
			str+=str2+str4;
			}
		}
		return String.format(str);
		
	}
	//查询
	function complaintSearch(cpid,ctmid){		
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintQuery/complaintConsultWatch.do?openPage=1&cpid='+cpid+'&ctmid='+ctmid);
	}
	//修改
	function updateComplaint(cpid,ctmid,status){
		fm.action = '<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptZx/waitComplaintUpdate.do?pageId=complaintSearch&openPage=1&cpid='+cpid+'&ctmid='+ctmid+'&stauts='+status;
		fm.submit();
	}
	//分派
	function complaintDealerSet(cpid,status){
		if(confirm("确定分派吗？")){
			if(status==<%=Constant.VOUCHER_STATUS_07%>){
				OpenHtmlWindow('<%=contextPath%>/jsp/customerRelationships/complaintConsult/complaintDisposeZx.jsp?flag=0&cpId='+cpid,800,300);
			}else{
		makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptZx/complaintDealerSetInit.json?pageId=complaintSearch&openPage=1&cpId='+cpid,setBack,'fm','');
			}
		}
	}
	function complaintClose(cpid,status){
		if(confirm("确定关闭吗？")){
			if(status==<%=Constant.VOUCHER_STATUS_07%>){
			OpenHtmlWindow('<%=contextPath%>/jsp/customerRelationships/complaintConsult/complaintDisposeZx.jsp?cpId='+cpid,800,300);
		}else{
			makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptZx/complaintDealerCloseInit.json?cpId='+cpid,setBack1,'fm','');
			}
		}
	}
	function setBack(json){
		if(json.success != null && json.success=='true'){
			MyAlert("分派成功!");
			__extQuery__(1);
		}else{
			MyAlert("分派失败,请联系管理员!");
		}
	}
	function setBack1(json){
		if(json.success != null && json.success=='true'){
			MyAlert("关闭成功!");
			__extQuery__(1);
		}else{
			MyAlert("关闭失败,请联系管理员!");
		}
	}
	
	function comComplaint(cpid){
		fm.action = '<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptZx/doComplaintZX.do?pageId=complaintSearch&openPage=1&cpid='+cpid;
		fm.submit();
	}
	
	function changeRegionEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/cascadeOrgSmallOrg.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeRegionEventBack,'fm','');
		}else{
			resetSelectOption('pro',null,null,null,null,null);
		}
	}
		
	function changeRegionEventBack(json){
		resetSelectOption('pro',json.orgProList,'ORG_NAME','ORG_ID',json.defaultValue,json.isdisabled);
	}
	
	//重置下拉框数据
	function resetSelectOption(id,maps,dataName,dataValue,dataId,isdisabled){
		clearSelectNode(id);
		addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled);
	}
	
	//动态删除下拉框节点
	function clearSelectNode(id) {			
		document.getElementById(id).options.length=0; 			
	}
	//动态添加下拉框节点
	function addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled){
		document.getElementById(id).options.add(new Option('-请选择-',''));
		for(var i = 0; i<maps.length;i++){
			if((maps[i])['' +dataValue+''] == dataId){
				document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,true));
			}
			else{
				document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,false));
			}
		}
		if(isdisabled == 'true' || isdisabled == true){
			document.getElementById(id).disabled = "disabled";
		}else{
			document.getElementById(id).disabled = "";
		}
		
	}
	
	function changeRepeatComplaint(){
		var obj = document.getElementById('repeatComplaint');
		if(obj.checked){
			obj.value = "true";
		}else{
			obj.value = "";
		}
	}
</script>
</body>
</html>