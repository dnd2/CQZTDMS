<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import=" com.infodms.dms.util.CommonUtils"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售经销商信息导入确认</title>
</head>
<body onunload='javascript:destoryPrototype()'>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置 >系统管理>经销商管理>销售经销商维护>导入确认</div>
<form  name="fm" id="fm">
<table class="table_query" id="subtab">
  <tr class="csstr" align="center"> 
		<th>
			<div align="left">
				<input class="cssbutton" type="button" id="savebtn" name='saveResButton' onclick='isSave();' value='保存' />
				<input class="cssbutton" type='button' name='saveResButton' onclick='history.back();' value='返回' />
			</div>
		</th>	
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
var url = "<%=contextPath%>/sysmng/dealer/XsDealerImport/xsImportOperateQuery.json";
var title = null;
var columns = [
			{header: "序号",align:'center',renderer:getIndex},
			{header: "所属区域",dataIndex: 'A1',align:'center'},
			{header: "省份",dataIndex: 'A2',align:'center'},
			{header: "所属行政区域城市",dataIndex: 'A3',align:'center'},
			{header: "所属行政区域区县",dataIndex: 'A4',align:'center'},
			{header: "所属一级经销商名称",dataIndex: 'A5',align:'center'},
			{header: "所属一级经销商代码",dataIndex: 'A6',align:'center'},
			{header: "邮编",dataIndex: 'A7',align:'center'},
			{header: "二级网络编码",dataIndex: 'A8',align:'center'},
			{header: "二级网络状态",dataIndex: 'A9',align:'center'},
			{header: "二级网络全称",dataIndex: 'A10',align:'center'},
			{header: "二级网络简称",dataIndex: 'A11',align:'center'},
			{header: "对应服务商编码",dataIndex: 'A12',align:'center'},
			{header: "对应服务商状态",dataIndex: 'A13',align:'center'},
			{header: "销售展厅地址",dataIndex: 'A14',align:'center'},
			{header: "代理其它品牌名称",dataIndex: 'A15',align:'center'},
			{header: "二级网络销售热线",dataIndex: 'A16',align:'center'},
			{header: "二级网络邮箱",dataIndex: 'A17',align:'center'},
			{header: "代理车型",dataIndex: 'A18',align:'center'},
			{header: "最低库存",dataIndex: 'A19',align:'center'},
			{header: "北汽幻速专营面积",dataIndex: 'A20',align:'center'},
			{header: "北汽幻速专营销售人员数",dataIndex: 'A21',align:'center'},
			{header: "全年任务量",dataIndex: 'A22',align:'center'},
			{header: "代理区域",dataIndex: 'A23',align:'center'},
			{header: "负责人",dataIndex: 'A24',align:'center'},
			{header: "负责人办公电话",dataIndex: 'A25',align:'center'},
			{header: "负责人手机",dataIndex: 'A26',align:'center'},
			{header: "负责人邮箱",dataIndex: 'A27',align:'center'},
			{header: "服务经理",dataIndex: 'A28',align:'center'},
			{header: "服务经理办公电话",dataIndex: 'A29',align:'center'},
			{header: "服务经理手机",dataIndex: 'A30',align:'center'},
			{header: "服务经理邮箱",dataIndex: 'A31',align:'center'},
			{header: "信息员",dataIndex: 'A32',align:'center'},
			{header: "信息员办公电话",dataIndex: 'A33',align:'center'},
			{header: "信息员手机",dataIndex: 'A34',align:'center'},
			{header: "信息员QQ",dataIndex: 'A35',align:'center'},
			{header: "信息员邮箱",dataIndex: 'A36',align:'center'},
			{header: "整车收货地址",dataIndex: 'A37',align:'center'},
			{header: "整车收货联系人",dataIndex: 'A38',align:'center'},
			{header: "整车收货联系人性别",dataIndex: 'A39',align:'center'},
			{header: "整车收货联系人办公电话",dataIndex: 'A40',align:'center'},
			{header: "整车收货联系人手机",dataIndex: 'A41',align:'center'},
			{header: "服务站地址",dataIndex: 'A42',align:'center'},
			{header: "24小时服务热线",dataIndex: 'A43',align:'center'},
			{header: "二级网络性质",dataIndex: 'A44',align:'center'},
			{header: "竞品品牌",dataIndex: 'A45',align:'center'},
			{header: "与竞品行驶距离（米）",dataIndex: 'A46',align:'center'},
			{header: "月均销量",dataIndex: 'A47',align:'center'},
			{header: "门头长度",dataIndex: 'A48',align:'center'},
			{header: "是否具有销售门头",dataIndex: 'A49',align:'center'},
			{header: "是否具有销售形象墙",dataIndex: 'A50',align:'center'},
			{header: "服务网点性质",dataIndex: 'A51',align:'center'},
			{header: "维修资质",dataIndex: 'A52',align:'center'},
			{header: "服务车间面积",dataIndex: 'A53',align:'center'},
			{header: "是否具有服务门头",dataIndex: 'A54',align:'center'},
			{header: "是否具有服务形象墙",dataIndex: 'A55',align:'center'},
			{header: "服务离销售网点距离",dataIndex: 'A56',align:'center'},
			{header: "维修技术师最低配备 （人员数量）",dataIndex: 'A57',align:'center'}
	      ];
//初始化    
function doInit(){
	__extQuery__(1);
}
function isSave(){
    if(submitForm('fm')){
	    MyConfirm("是否确认保存信息?",importSave);
    }
 }
function importSave() {
	if(submitForm('fm')){
			fm.action = "<%=contextPath %>/sysmng/dealer/XsDealerImport/importExcel2nd.do";
			fm.submit();
		}
}
</script>
</body>
</html>
