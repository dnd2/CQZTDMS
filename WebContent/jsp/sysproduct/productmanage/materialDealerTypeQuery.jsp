<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料类型经销商设置</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料类型经销商设置</div>
<form method="POST" name="fm" id="fm">
  	<table class="table_query" border="0">
		<tr>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商代码：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="dealerCode" maxlength="30" value="" datatype="1,is_noquotation,30" id="dealerCode" type="text" class="middle_txt" />
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商名称：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="dealerName" maxlength="30" value="" datatype="1,is_noquotation,30" id="dealerName" type="text" class="middle_txt" />
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商状态：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<script type="text/javascript">
			      	genSelBoxExp("status",<%=Constant.STATUS%>,"",true,"min_sel","","false",''); 
			    </script>
			</td>
		</tr>
		<tr> 
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">是否常规：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<script type="text/javascript">
			      	genSelBoxExp("is_rule_seach",<%=Constant.IF_TYPE%>,"",true,"min_sel","","false",'');
			    </script>
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">是否出口：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<script type="text/javascript">
			      	genSelBoxExp("is_export_seach",<%=Constant.IF_TYPE%>,"",true,"min_sel","","false",'');
			    </script>
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">是否内销：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<script type="text/javascript">
			      	genSelBoxExp("is_insale_seach",<%=Constant.IF_TYPE%>,"",true,"min_sel","","false",'');
			    </script>
			</td>
		</tr>
		<tr>
			<td colspan="6" class="table_query_4Col_input" style="text-align: center">
				<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> &nbsp; 
				<input type="button" class="normal_btn" onclick="reset();" value="重 置"/> &nbsp; 
			</td>
		</tr>
	</table>
	<table class="table_query" style="border: 1px #f0f0f0 solid; margin-top: 2px;">
		<tr>
			<td align="right">是否常规：</td>
			<td align="left">
				<script type="text/javascript">
					genSelBoxExp("isRuleMat",<%=Constant.IF_TYPE%>,"",true,"min_sel","","false",'');
			    </script>
			</td>
			<td align="right">是否出口：</td>
			<td align="left">
				<script type="text/javascript">
					genSelBoxExp("esflag",<%=Constant.IF_TYPE%>,"",true,"min_sel","","false",'');
			    </script>
			</td>
			<td align="right">是否内销：</td>
		    <td align="left">
		    	<script type="text/javascript">
			      	genSelBoxExp("isInsale",<%=Constant.IF_TYPE%>,"",true,"min_sel","","false",''); 
			    </script>
			    <font color='red'>* 如果不需要设置，请设置"请选择"</font>
		    </td>
			<td align="left">
				<input type="button" class="normal_btn" value="保 存" onclick="saveSetting()"/>
			</td>
		</tr>
	</table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">

	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialDealerTypeManage/materialDealerTypeQuery.json";;
				
	var title = null;

	var columns = [
					{
						header: "<input type='checkbox' class='checkbox' name='checkAll' onclick='selectAll(this,\"mid\")' />", align:'center', 
						dataIndex : 'MATERIAL_ID', 
						renderer:function(value,metaDate,record) {
							var oId=record.data.ID;
							var dId=record.data.DEALER_ID;
							var ht="<input type='checkbox' class='checkbox' name='mid' value='" + dId + "#" + oId + "' />";
							return ht;
						}
					},
					{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
					{header: "经销商名称", dataIndex: 'DEALER_NAME', style:'text-align:left'},
					{header: "是否常规", dataIndex: 'IS_RULE_MAT', align:'center', renderer:mySelect2},
					{header: "是否出口", dataIndex: 'EXPORT_SALES_FLAG', align: 'center',renderer:mySelect},
					{header: "是否内销", dataIndex: 'IS_INSALE', align: 'center', renderer:mySelect1},
					{id:'action', header: "操作" ,dataIndex: 'DEALER_ID', renderer:myLink}
			      ];	
			      
	function myLink(value,metaDate,record){
		var dId=record.data.DEALER_ID;
		var id=record.data.ID;
        return String.format("<input type='button' class='normal_btn' value='保 存' onclick='saveSettingOne("+dId+","+id+")'/>");
    }
	function mySelect(value,metaDate,record){
		var dId=record.data.DEALER_ID;
		var isSelected="",isSelected1="",isSelected3="";
		if(record.data.EXPORT_SALES_FLAG==10041001){
			isSelected="selected=selected";
		}else if(record.data.EXPORT_SALES_FLAG==10041002){
			isSelected1="selected=selected";
		}else{
			isSelected3="selected=selected";
		}
		var strOption="<option value='' "+isSelected3+">-请选择-</option>";
		strOption+="<option value='10041001' "+isSelected+">是</option>";
		strOption+="<option value='10041002' "+isSelected1+">否</option>";
		return String.format("<SELECT class=min_sel id='"+dId+"_OX' name='O_EXPORT_NAME'>"+strOption+"</SELECT>");
	}
	function mySelect1(value,metaDate,record){
		var dId=record.data.DEALER_ID;
		var isSelected="",isSelected1="",isSelected3="";
		if(record.data.IS_INSALE==10041001){
			isSelected="selected=selected";
		}else if(record.data.IS_INSALE==10041002){
			isSelected1="selected=selected";
		}else{
			isSelected3="selected=selected";
		}
		var strOption="<option value='' "+isSelected3+">-请选择-</option>";
		strOption+="<option value='10041001' "+isSelected+">是</option>";
		strOption+="<option value='10041002' "+isSelected1+">否</option>";
		return String.format("<SELECT class=min_sel id='"+dId+"_OI' name='O_IS_INSALE'>"+strOption+"</SELECT>");
	}
	function mySelect2(value,metaDate,record){
		var dId=record.data.DEALER_ID;
		var isSelected="",isSelected1="",isSelected3="";
		if(record.data.IS_RULE_MAT==10041001){
			isSelected="selected=selected";
		}else if(record.data.IS_RULE_MAT==10041002){
			isSelected1="selected=selected";
		}else{
			isSelected3="selected=selected";
		}
		var strOption="<option value='' "+isSelected3+">-请选择-</option>";
		strOption+="<option value='10041001' "+isSelected+">是</option>";
		strOption+="<option value='10041002' "+isSelected1+">否</option>";
		return String.format("<SELECT class=min_sel id='"+dId+"_OR' name='O_IS_RULE_MAT'>"+strOption+"</SELECT>");
	}
   	//paramOne:是否常规；paramTwo:是否出口；paramThree:内销；
 	function saveSettingOne(dId,value) {
 	 	var paramOne =  $(dId+"_OR").value;
 	 	var paramTwo =  $(dId+"_OX").value;
 	 	var paramThree =  $(dId+"_OI").value;
 	 	if(paramOne != '' || paramTwo != '' || paramThree!='') { 
			MyConfirm('确认设置?', function(){
				var subUrl = '<%=request.getContextPath()%>/sysproduct/productmanage/MaterialDealerTypeManage/materialManageSetOne.json';
				makeCall(subUrl,function(json){
					if(json.Exception) {
						MyAlert(json.Exception.message);
					} else {
						MyAlert(json.message);
						__extQuery__(1);
					}
				},{IS_RULE_MAT:paramOne,EXPORT_SALES_FLAG:paramTwo,IS_INSALE:paramThree,dealerId:dId,ID:value});
			});
		} else {
			MyAlert('您还未设置状态!');
		}
 	 	
   	}
	//批量修改
	//paramOne:是否常规；paramTwo:是否出口；paramThree:内销；
 	function saveSetting() {
 	 	var paramOne =  $("isRuleMat").value;
 	 	var paramTwo =  $("esflag").value;
 	 	var paramThree =  $("isInsale").value;
 	 	if(paramOne != '' || paramTwo != '' || paramThree!='') { 
			MyConfirm('确认设置?', function(){
				var subUrl = '<%=request.getContextPath()%>/sysproduct/productmanage/MaterialDealerTypeManage/materialManageSet.json';
				makeNomalFormCall(subUrl, function(json){
					if(json.Exception) {
						MyAlert(json.Exception.message);
					} else {
						MyAlert(json.message);
						__extQuery__(1);
					}
				}, 'fm');
			});
		} else {
			MyAlert('您还未设置状态!');
		}
 	 	
   	}
</script>
</body>
</html>
