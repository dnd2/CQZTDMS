<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>包装材料维护</title>
</head>
<body onload=" __extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：基础信息管理&gt;配件基础信息维护&gt;包装材料</div>
<form method="post" name ="fm" id="fm" enctype="multipart/form-data" >
	<div class="form-panel">
		<h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" class="panel-query-title nav" />查询条件</h2>
		<div class="form-body">
			<table class="table_query" >
				<tr id="groupId">
					<td class="right" width="10%" align="right">规格：</td>
					<td  width="15%"><input class="middle_txt" type="text" name="SPEC" id="SPEC"/>
					</td>
					<td class="right" width="10%" align="right">名称：</td>
					<td  width="15%"><input class="middle_txt" type="text" name="NAME" id="NAME"/>
					</td>
					<td class="right" width="10%" align="right">包装类别：</td> 
					<td width="15%" align="left">
						${selectBox}
					</td>
				</tr>	
				<tr>
					<td class="txt-center" align='center' colspan=6>
					<input id="queryBtn" type="button" class="u-button u-query" onclick="__extQuery__(1);" value="查 询"/>
					<input id="addBtn" type="button" class="u-button" onclick="addPackage();" value="新 增"/>
					<input id="addBtn" type="button" class="u-button" onclick="exportPkg();" value="导出库存"/>
					<input class="u-button" type="button" value="导出计划" onclick="exportPkgPlan()"/>
					<input type="button" class="u-button" value="出入库明细查询" onclick="detailOrder1();"/>
					</td>
				</tr>      
			</table>
		</div>
	</div>
    
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>

<script language="javascript">
	
	var myPage;
	var url = "<%=contextPath%>/parts/baseManager/packageManager/packageManager/getMainList.json";
	var title = null;
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},	           	
                {header: "规格", dataIndex: 'PACK_SPEC', align:'center'},
                {header: "包装类别", dataIndex: 'PACK_TYPE', align:'center',renderer:getItemValue},
                {header: "名称", dataIndex: 'PACK_NAME', align:'center'},
                {header: "单位", dataIndex: 'PACK_UOM', align:'center'},
                {header: "库存", dataIndex: 'PACK_QTY', align:'center'},
                {header: "操作",  align:'center',sortable: false,dataIndex: 'PACK_ID',renderer:myLink}
		      ];   
    
    //设置超链接  begin
	function myLink(value, meta, record) {
    	  var formatString = "";       
          formatString += "<a href='#' onclick='change("+record.data.PACK_ID+");'>[出入库]</a>";
          formatString += "<a href='<%=contextPath%>/parts/baseManager/packageManager/packageManager/packageEditInit.do?packageId="+record.data.PACK_ID+" '>[修改]</a>";
          formatString += "<a href='#' onclick='del("+record.data.PACK_ID+");'>[删除]</a>";
        return String.format(formatString);
    }
    
    //新增方法
	function addPackage() {
		fm.action="<%=contextPath%>/parts/baseManager/packageManager/packageManager/packageAddInit.do";
		fm.submit();
    }


    //新增方法
	function del(packageId) {
		var url = "<%=contextPath%>/parts/baseManager/packageManager/packageManager/packageDel.json?PACK_ID="+packageId;
		sendAjax(url,callBackDel,'fm');
    }

	function callBackDel(json){
	    if (json != null) {
	        var success = json.success;	        
	        var error = json.error;
	        var exceptions = json.Exception;
	        if (success) {
	        	MyAlert(success);
	        	__extQuery__(1);
	        } else if (error) {
	        	MyAlert(error);
	        } else if (exceptions) {
	        	MyAlert(exceptions.message);
	        }
	    }		
	}
    
    //出入库
	function change(packageId) {
		var url=g_webAppName+"/jsp/parts/baseManager/partsBaseManager/packageManager/packageChange.jsp?packageId="+packageId;
		OpenHtmlWindow(url,500,220, '出入库');
    }

    //下载
    function exportPkgPlan(){
        document.fm.action="<%=contextPath%>/parts/baseManager/packageManager/packageManager/expPartPkgProExcel.do";
        document.fm.target="_self";
        document.fm.submit();
    }
    //库存导出
    function exportPkg(){
        document.fm.action="<%=contextPath%>/parts/baseManager/packageManager/packageManager/expPartPkgToExcel.do";
        document.fm.target="_self";
        document.fm.submit();
    }
    //zhumingwei add 2013-11-19 出入库明细查询
    function detailOrder1() {
    	var buttonFalg="1";
    	OpenHtmlWindow("<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/pckMaterialsDetailInit.do?buttonFalg="+buttonFalg,800,400);
    }

	$(function() {
		$('select[name="TYPE"]').addClass('u-select');
	});
</script>

</body>
</html>