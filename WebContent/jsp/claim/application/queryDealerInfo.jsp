<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%String contextPath = request.getContextPath();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>经销商信息管理</title>
	<script type="text/javascript">
	    function doInit(){
		   loadcalendar();
		   genLocSel('txt1','','','','',''); //加载省份城市和县
		}
		//区域内容清除操作
		function wrapOut(){
			$('area_code').value = '' ;
			$('area_id').value = '' ;
		}
		function clearInput(inputId){
			var inputVar = document.getElementById(inputId);
			inputVar.value = '';
		}
	</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />当前位置：售后服务管理&gt;索赔结算管理&gt;经销商信息管理</div>
<form method="post" name="fm" id="fm">
<table align="center" class="table_query">
	<tr>
		<td align="right" nowrap="true">经销商代码：</td>
		<td align="left" nowrap="true">
			<input class="long_txt" id="dealerCode"  name="dealerCode" type="text"/>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput('dealerCode');" value="清除"/>
		</td>
		<td align="right" nowrap="true">经销商名称：</td>
		<td align="left" nowrap="true">
			<input name="dealerName" value="" type="text" class="middle_txt"/>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">经销商等级：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				 genSelBoxExp("DEALER_LEVEL",<%=Constant.DEALER_LEVEL%>,"",true,"short_sel","","false",'');
		    </script>
		</td>
		<td align="right" nowrap="true">状态：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
				 genSelBoxExp("STATUS",<%=Constant.STATUS%>,"",true,"short_sel","","false",'');
		    </script>
		</td>
	</tr>
	<tr>
		<td align="right" nowrap="true">大区：</td>
		<td align="left" nowrap="true">
			<input type="text" name="area_code" id="area_code" class="long_txt"/>
 			<input type="hidden" name="area_id" id="area_id"/>
			<input type="button" class="mini_btn" value="..." onclick="showOrg('area_code' ,'area_id' ,true,'');"/>
			<input type="button" class="normal_btn" value="清除" onclick="wrapOut();"/>
		</td>
		<td align="right" nowrap="true">省份：</td>
        <td>
        	<select class="short_sel" id="txt1" name="province"></select>
        </td>
	</tr>
	<tr>
		<td colspan="4" align="center">
			<input class="normal_btn" type="button" name="button1" id="queryBtn" value="查询" onclick="__extQuery__(1)"/>
		</td>
	</tr>
</table>
</form>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<script type="text/javascript">
			var myPage;
			var url = "<%=contextPath%>/claim/application/DealerBalance/dealerInfoQuery.json";
			var title = null;
			
			var columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "大区",dataIndex: 'ROOT_ORG_NAME',align:'center'},	
						{header: "省份",dataIndex: 'REGION_NAME',align:'center'},	
						{header: "经销商代码",dataIndex:'DEALER_CODE',align:'center'},
						{header: "经销商名称",dataIndex: 'ONE_NAME',align:'center'},		
						{header: "经销商等级",dataIndex: 'DEALER_LEVEL',align:'center',renderer:getItemValue},
						{header: "上级经销商名称",dataIndex: 'TWO_NAME',align:'center'},
						{header: "状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
						{header: "三包员电话",dataIndex: 'CLAIMER_TEL',align:'center'}, 
						{header: "站长电话",dataIndex: 'STATIONER_TEL',align:'center'}
						//renderer:getItemValue
						//{header: "操作",dataIndex: 'ID',align:'center', renderer:accAudut}
				      ];
	</script>
</body>
</html>