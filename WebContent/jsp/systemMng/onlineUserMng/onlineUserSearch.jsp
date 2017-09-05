<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
	String check = (String)request.getAttribute("check");
%>
<jsp:include page="${contextPath}/common/globalVariable.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/calendar.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath %>/style/page-info.css" rel="stylesheet" type="text/css" />
	<link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet"	type="text/css" />
	<script type="text/javascript" src="<%=contextPath%>/js/jslib/mootools.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/jslib/my-grid-pager.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/mtcommon.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dealer_tree.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/web/dept_tree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath %>/js/validate/validate.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/web/dtree.js" charset="UTF-8"></script>
	<script type="text/javascript" src="<%=contextPath%>/js/dict.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/common.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/InfoAjax.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jslib/dialog_new.js"></script>

<title>当前在线用户查询</title>
<style>
	.img{ border:none}
</style>
</head>

<body onload="__extQuery__(1);init();" onunload="javascript:destoryPrototype()">
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 在线用户查询 &gt; 当前在线用户查询
		</div>
		<form id="fm" name="fm">
			<input type="hidden" name="curPage" id="curPage" value="1" />
			<input id="DEALER_ID" name="DEALER_ID" type="hidden" value=""/>
			<input type="hidden" name="DDEPT_ID" id="DDEPT_ID" value="" />
			<input type="hidden" id="orderCol" name="orderCol" value="" />
			<input type="hidden" id="order" name="order" value="" />
			<input type="hidden" name="tree_root_id" id="tree_root_id" value=""/>
			<table class="table_query" border="0">
				<tr><input type="hidden" name="userOnlineId" id="userOnlineId" value="" />
					<td width="8%" nowrap="nowrap" class="table_query_label">
						<input type="radio" id="radio2" name="radiobutton" value="2" onclick="showdlr();" <%if(check.equals("2")){%>checked<%} %>/>经销商：</td>
					<td class="table_query_input" nowrap="nowrap">
						<input class="middle_txt" id="DEALER_NAME" onclick="showPan()" readonly="readonly" style="cursor: pointer;" name="DEALER_NAME" type="text"/>
					</td>
					<td class="table_query_label" nowrap="nowrap" width="3%">
						<input type="radio" id="radio1" name="radiobutton" value="1" onclick="showorg();" <%if(check.equals("1") || check==""){%>checked<%} %>/>JMC部门：</td>
					<td class="table_query_input" nowrap="nowrap">
<input class="middle_txt" id="DEPT_NAME" onblur="isCloseTreeDiv(event,this,'deptt')" onclick="showDEPT()"
style="cursor: pointer;" name="DEPT_NAME" type="text"/>
					</td>
					<td class="table_query_label" nowrap="nowrap"  align="left" width="8%">
						<input class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);" id="queryBtn"/>&nbsp;
						<input class="normal_btn" type="button" value="重 置" onclick="rest();"/>
					</td>
				</tr>
			</table>
		</form>	
		<div id="_page" style="margin-top:15px;display:none;"></div>
		<div id="myGrid" ></div>
		<div id="myPage" class="pages"></div>
		<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
	</div>

<form id="fm2" name="fm2">
<input type="hidden" name="curPage2" id="curPage2" value="1" />
<input type="hidden" name="DEPT_ID" id="DEPT_ID" value="" />
<input type="hidden" id="orderCol2" name="orderCol2" value="" />
<input type="hidden" id="order2" name="order2" value="" />
<div id='pan' style="z-index: 3000;position:absolute;border:1px solid #5E7692;background: #FFFFFF; width: 715px;height: 379px;">
	<div id='myquery' style="z-index: 3001;position:absolute;border:1px solid #5E7692;width: 715px;height: 30px;">
		<table class="table_info" border="0" style="height: 30px;" width="100%">
		<tr>
				<td class="table_query_3Col_label_5Letter" nowrap="nowrap">经销商代码：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input class="middle_txt" id="DRLCODE" datatype="1,is_noquotation,30" name="DRLCODE" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
				</td>
				<td class="table_query_3Col_label_5Letter" nowrap="nowrap">经销商简称：</td>
				<td class="table_query_3Col_input" nowrap="nowrap">
					<input class="middle_txt" id="DELSNAME" datatype="1,is_noquotation,30" name="DELSNAME" onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
				</td>
				<td class="table_query_3Col_input" nowrap="nowrap"><input class="normal_btn" type="button" value="查 询" id="queryBtn2" onclick="getDrl(1)"/>
				<input class="normal_btn" type="button" value="重 置" onclick="requery2()"/></td>
			</tr>
		</table>
	</div>
	<div id='dtree' class="dtree" style="z-index: 3000;position:absolute;border:1px solid #5E7692;width: 213px;height: 349px;">
        <script type="text/javascript">
        a = new dTree('a','dtree','false','false','true');
        </script>
    </div>
    <div id="drlv" style="z-index: 3000;position:absolute;border:1px solid #5E7692;width: 501px;height: 349px;  overflow-y: auto; overflow-x:hidden;">
    	<br />
    	<table width="100%">
    		<tr>
    			<td>
    				<div id="_page2" style="display:none;"></div>
					<div id="myGrid2" ></div>
					<div id="myPage2" class="pages"></div>
    			</td>
    		</tr>
    	</table>
    </div>
</div>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>

	<script type="text/javascript" >
		validateConfig.isOnBlur = false;
		var drlurl = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/drlQuery.json?COMMAND=1";
		var tree_url = "<%=contextPath%>/sysmng/usemng/SgmDealerSysUser/initOrgTree.json";
		var dept_tree_url = "<%=contextPath%>/sysmng/usemng/SgmSysUser/initOrgTree.json";

		var myPage;
		function __extQuery__(page){
			$('DDEPT_ID').value=$('DEPT_ID').value;
			$("queryBtn").disabled = "disabled";
			showMask();
			submitForm('fm') ? sendAjax(url+(url.lastIndexOf("?") == -1?"?":"&")+"curPage="+page,callBack,'fm') : ($("queryBtn").disabled = "",removeMask());
		}
		var url = "<%=contextPath%>/sysmng/onlineusermng/OnlineUserInfo/queryOnlineUser.json";

		//设置表格标题
		var title= null;

		var columns = [
						{header: "序号",width:'6%',  renderer:getIndex},
						{header: "用户名", width:'13%', dataIndex: 'empNum',orderCol:"emp_num"},
						{header: "姓名", width:'13%',orderType:"pingyin", dataIndex: 'userName',orderCol:"name"},
						{header: "所属公司", width:'13%',orderType:"pingyin", dataIndex: 'companyName',orderCol:"company_short_name"},
						{header: "部门", width:'13%',orderType:"pingyin", dataIndex: 'orgName',orderCol:"dept_name"},
						{header: "登录时间", width:'15%', dataIndex: 'loginDate',orderCol:"login_date"},
						{header: "IP地址", width:'13%', dataIndex: 'userIP',orderCol:"user_ip"},
						{id:'action',header: "操作",width:'7%',dataIndex: 'userOnlineId',renderer:myLink}
					  ];
				      
		//设置超链接
	   function myLink(value){
	        return String.format("<a href='#' onClick='del("+value+");'>[下线]</a>");
	    }
	   function del(value){
	    	$("userOnlineId").value = value;
	       MyConfirm("是否确认清除当前在线用户?",delsub);
	    }
	    function delsub(){	 	
		    sendAjax('<%=contextPath%>/sysmng/onlineusermng/OnlineUserInfo/delOnlineUser.json',showResult,'fm');
	    }
	    function showResult(json){
			if(json.ACTION_RESULT == '1'){
				var ck = json.check;
				window.location.href = '<%=request.getContextPath()%>/sysmng/onlineusermng/OnlineUserInfo/queryOnlineUser.do?radiobutton='+ck;
			}
		}
		function showdlr(){
			document.fm.DEPT_NAME.disabled = true;
			document.fm.DEALER_NAME.disabled = false;
			document.fm2.DEPT_ID.value = "";
			document.fm.DDEPT_ID.value = "";
			document.fm.DEPT_NAME.value = "";
		}
		function showorg(){
			document.fm.DEPT_NAME.disabled = false;
			document.fm.DEALER_NAME.disabled = true;
			document.fm.DEALER_ID.value = "";
			document.fm.DEALER_NAME.value = "";
		}
		function init(){
			document.fm.DEALER_NAME.disabled = true;
		}

		function requery2() {
			$('DRLCODE').value="";
			$('DELSNAME').value="";
			$('DEPT_ID').value="";
		}
		function rest(){
			document.fm.DEALER_ID.value = "";
			document.fm.DEALER_NAME.value = "";
			document.fm2.DEPT_ID.value = "";
			document.fm.DDEPT_ID.value = "";
			document.fm.DEPT_NAME.value = "";
		}
	</script>
</body>
</html>
