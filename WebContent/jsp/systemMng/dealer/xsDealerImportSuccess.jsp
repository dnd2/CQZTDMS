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
			{header: "邮编",dataIndex: 'A5',align:'center'},
			{header: "经销商编码",dataIndex: 'A6',align:'center'},
			{header: "经销商状态",dataIndex: 'A7',align:'center'},
			{header: "所属上级单位",dataIndex: 'A8',align:'center'},
			{header: "对应服务商编码",dataIndex: 'A9',align:'center'},
			{header: "对应服务商状态",dataIndex: 'A10',align:'center'},
			{header: "经销商全称",dataIndex: 'A11',align:'center'},
			{header: "经销企业注册资金",dataIndex: 'A12',align:'center'},
			{header: "经销企业组织机构代码",dataIndex: 'A13',align:'center'},
			{header: "经销商简称",dataIndex: 'A14',align:'center'},
			{header: "企业注册地址",dataIndex: 'A15',align:'center'},
			{header: "销售展厅地址",dataIndex: 'A16',align:'center'},
			{header: "单位性质",dataIndex: 'A17',align:'center'},
			{header: "代理级别",dataIndex: 'A18',align:'center'},
			{header: "经营类型",dataIndex: 'A19',align:'center'},
			{header: "是否经营其他品牌",dataIndex: 'A20',align:'center'},
			{header: "代理其它品牌名称",dataIndex: 'A21',align:'center'},
			{header: "经销商销售热线",dataIndex: 'A22',align:'center'},
			{header: "经销商传真",dataIndex: 'A23',align:'center'},
			{header: "经销商邮箱",dataIndex: 'A24',align:'center'},
			{header: "代理车型",dataIndex: 'A25',align:'center'},
// 			{header: "代理区域",dataIndex: 'A26',align:'center'},
			{header: "国家品牌授权城市",dataIndex: 'A26',align:'center'},
			{header: "国家品牌授权区县",dataIndex: 'A27',align:'center'},
			{header: "国家品牌授权",dataIndex: 'A28',align:'center'},
			{header: "国家品牌授权信息收集时间",dataIndex: 'A29',align:'center'},
			{header: "国家品牌授权提交时间",dataIndex: 'A30',align:'center'},
			{header: "国家品牌授权起始时间",dataIndex: 'A31',align:'center'},
			{header: "工商总局公告号",dataIndex: 'A32',align:'center'},
			{header: "工商总局公布日期",dataIndex: 'A33',align:'center'},
			{header: "国家品牌授权截止时间",dataIndex: 'A34',align:'center'},
			{header: "品牌授权书打印",dataIndex: 'A35',align:'center'},
			{header: "经销企业法人",dataIndex: 'A36',align:'center'},
			{header: "法人办公电话",dataIndex: 'A37',align:'center'},
			{header: "法人手机",dataIndex: 'A38',align:'center'},
			{header: "法人邮箱",dataIndex: 'A39',align:'center'},
			{header: "总经理",dataIndex: 'A40',align:'center'},
			{header: "总经理办公电话",dataIndex: 'A41',align:'center'},
			{header: "总经理手机",dataIndex: 'A42',align:'center'},
			{header: "总经理邮箱",dataIndex: 'A43',align:'center'},
			{header: "销售经理",dataIndex: 'A44',align:'center'},
			{header: "销售经理办公电话",dataIndex: 'A45',align:'center'},
			{header: "销售经理手机",dataIndex: 'A46',align:'center'},
			{header: "销售经理邮箱",dataIndex: 'A47',align:'center'},
			{header: "市场经理",dataIndex: 'A48',align:'center'},
			{header: "市场经理办公电话",dataIndex: 'A49',align:'center'},
			{header: "市场经理手机",dataIndex: 'A50',align:'center'},
			{header: "市场经理邮箱",dataIndex: 'A51',align:'center'},
			{header: "服务经理",dataIndex: 'A52',align:'center'},
			{header: "服务经理办公电话",dataIndex: 'A53',align:'center'},
			{header: "服务经理手机",dataIndex: 'A54',align:'center'},
			{header: "服务经理邮箱",dataIndex: 'A55',align:'center'},
			{header: "财务经理",dataIndex: 'A56',align:'center'},
			{header: "财务经理办公电话",dataIndex: 'A57',align:'center'},
			{header: "财务手机",dataIndex: 'A58',align:'center'},
			{header: "财务邮箱",dataIndex: 'A59',align:'center'},
			{header: "信息员",dataIndex: 'A60',align:'center'},
			{header: "信息员办公电话",dataIndex: 'A61',align:'center'},
			{header: "信息员手机",dataIndex: 'A62',align:'center'},
			{header: "信息员QQ",dataIndex: 'A63',align:'center'},
			{header: "信息员邮箱",dataIndex: 'A64',align:'center'},
			{header: "开票公司名称",dataIndex: 'A65',align:'center'},
			{header: "开票联系人",dataIndex: 'A66',align:'center'},
			{header: "开票联系人办公电话",dataIndex: 'A67',align:'center'},
			{header: "开票联系人手机",dataIndex: 'A68',align:'center'},
			{header: "开票信息地址",dataIndex: 'A69',align:'center'},
			{header: "开户行全称",dataIndex: 'A70',align:'center'},
			{header: "开户行账号",dataIndex: 'A71',align:'center'},
			{header: "纳税识别号",dataIndex: 'A72',align:'center'},
			{header: "信函收件地址",dataIndex: 'A73',align:'center'},
			{header: "信函收件联系人",dataIndex: 'A74',align:'center'},
			{header: "信函收件人性别",dataIndex: 'A75',align:'center'},
			{header: "信函收件联系人办公电话",dataIndex: 'A76',align:'center'},
			{header: "信函收件联系人手机",dataIndex: 'A77',align:'center'},
			{header: "VI建设申请日期",dataIndex: 'A78',align:'center'},
			{header: "VI建设开工日期",dataIndex: 'A79',align:'center'},
			{header: "VI建设竣工日期",dataIndex: 'A80',align:'center'},
			{header: "VI形象验收日期",dataIndex: 'A81',align:'center'},
			{header: "拟建店类别",dataIndex: 'A82',align:'center'},
			{header: "VI形象验收确定级别",dataIndex: 'A83',align:'center'},
			{header: "VI支持总金额",dataIndex: 'A84',align:'center'},
			{header: "VI支持首批比例",dataIndex: 'A85',align:'center'},
			{header: "VI支持后续支持方式",dataIndex: 'A86',align:'center'},
			{header: "VI支持起始时间",dataIndex: 'A87',align:'center'},
			{header: "VI支持截止时间",dataIndex: 'A88',align:'center'},
			{header: "首次提车时间",dataIndex: 'A89',align:'center'},
			{header: "首次到车日期",dataIndex: 'A90',align:'center'},
			{header: "首次销售时间",dataIndex: 'A91',align:'center'},
			{header: "备注",dataIndex: 'A92',align:'center'}
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
			fm.action = "<%=contextPath %>/sysmng/dealer/XsDealerImport/importExcel.do";
			fm.submit();
		}
}
</script>
</body>
</html>
