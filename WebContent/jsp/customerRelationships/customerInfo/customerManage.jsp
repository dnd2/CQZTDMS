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
<title>客户信息管理</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户信息管理 &gt;客户信息管理</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<tr>
				<td class="table_query_2Col_label_6Letter">客户姓名：</td>
		    	<td><input type='text'  class="middle_txt" name="ctmName"  id='ctmNameId' value=""/></td>
		    	<td class="table_query_2Col_label_6Letter">客户电话：</td>
		    	<td><input type='text'  class="middle_txt" name="mainPhone"  id='mainPhoneId' value=""/></td>
		    </tr>
		    <tr>	
		    	<td class="table_query_2Col_label_6Letter">客户类型：</td>
		    	<td>
		    	<script type="text/javascript">
		    			genSelBoxExp("ctmType",<%=Constant.CUSTOMER_TYPE%>,"",true,"short_sel","","false",'');
		    	</script>
		    	</td>
				<td class="table_query_2Col_label_6Letter">客户性别：</td>
				<td align="lift"><script type="text/javascript">
                		genSelBoxExp("sex",<%=Constant.GENDER_TYPE%>,"",true,"short_sel","","false",'');
            		</script></td>
            </tr>
            <tr>
					<td class="table_query_2Col_label_6Letter">客户星级：</td>
					<td><script type="text/javascript">
					genSelBoxExp("guestStars",<%=Constant.GUEST_STARS%>,"",true,"short_sel",'',"false",'');
				</script></td>
					<!--<td class="table_query_2Col_label_6Letter">选择物料：</td>
      				<td align="left" >
      					<input type="text" id="materialName" name="materialName" class="middle_txt"/>
						<input type="button" class="mini_btn" onclick="showMaterial('materialName','','true');" value="..."/>
						<input class="normal_btn" type="button" value="清空" onclick="clrTxt('materialName');"/>
     				</td>-->
     			<!-- 	<td align="right" nowrap="true">物料组：</td>
        			<td class="table_query_4Col_input" nowrap="nowrap">
						<input class="middle_txt" type="text" name="GROUP_NAME" size="20" id="GROUP_NAME" value="" />
						<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('GROUP_NAME','','false','3','true')" value="..." />
						<input class="normal_btn" type="button" value="清空" onclick="clrTxt('GROUP_NAME');"/>
					</td> -->
					<td class="table_query_2Col_label_6Letter">生产基地：</td>
				<td>
					<script type="text/javascript">
					genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
                	</script>
				</td>
     		</tr>
			<tr>
				<td align="right" nowrap="true">购车日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="dateStart" name="dateStart" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dateEnd" name="dateEnd" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateEnd', false);" type="button"/>
				</td>
				<td class="table_query_2Col_label_6Letter">VIN：</td>
				<td><input type="text" class="middle_txt" name="vin" id='vin' value=""/></td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">车系：</td>
				<td align="left">
					<select id="series" name="series" class="short_sel" onchange="queryModel();">
						<option value=''>-请选择-</option>
						<c:forEach var="ser" items="${seriesList}">
							<option value="${ser.seriesId}" title="${ser.seriesName}">${ser.seriesName}</option>
						</c:forEach>
					</select>
				</td>
				<td class="table_query_2Col_label_6Letter">车型：</td>
				<td align="left">
					<div id="model_div">
						<script type="text/javascript">
							$('model_div').innerHTML = '<select class="short_sel"><option>-请选择-</option></select>' ;
							function showModel(json){
								$('model_div').innerHTML = json.modelStr;
							}
						</script>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
        		</td>
			</tr>
		</table>
		
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>

<script type="text/javascript" >
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/customerInfo/customerManage/customerManageGet.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "客户名称",dataIndex: 'CTMNAME',align:'center'},
				{header: "联系电话", dataIndex: 'MAINPHONE', align:'center'},
				{header: "性别", dataIndex: 'SEX', align:'center',renderer:getItemValue},
				{header: "客户类型",dataIndex: "CTMTYPE",align:'center',renderer:getItemValue},
				{header: "客户星级", dataIndex: 'GUEST_STARS', align:'center',renderer:getItemValue},
				{header: "车系", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "购车日期", dataIndex: 'PURCHASEDDATE', align:'center',renderer:fmtD},
				{header: "生产基地", dataIndex: 'YIELDLY', align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'CTMID',renderer:myLink}
		      ];
	//处理的超链接
	function fmtD(value,rema,record){
		if(value) return value.substr(0,10);
		else return '' ;
	}
	
	function myLink(value,meta,record){
		return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='修改'/>");
	}

	function viewDetail(value){
		location = '<%=contextPath%>/customerRelationships/customerInfo/customerManage/manageAmend.do?id='+value ;
	}
	
	//清空按钮
	function clrTxt(){
		document.getElementById("GROUP_NAME").value="";
	}

	//车型下拉列表
	function queryModel(){
		var id=$('series').value ;
		if(id){
			var url = '<%=contextPath%>/customerRelationships/customerInfo/customerManage/queryModel.json?id='+id ;
			makeNomalFormCall(url,showModel,'fm');
		}
		else
			$('model_div').innerHTML = '<select class="short_sel"><option>-请选择-</option></select>' ;
	}
</script>
</body>
</html>