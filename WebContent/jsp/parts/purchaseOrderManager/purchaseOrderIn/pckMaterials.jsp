<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>包装材料出入库汇总报表</title>
    <script language="JavaScript">

        //初始化方法
        function doInit() {
            loadcalendar();  //初始化时间控件
        }

    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 报表管理&gt;配件报表&gt;本部仓储报表&gt;包装材料出入库汇总报表</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="partId" id="partId"/>
    <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />查询条件</th>
	    <tr >
	       <td width="10%" align="right">规格：</td>
	       <td width="20%" ><input class="middle_txt" type="text" name="Specification" id="Specification"/></td>
	       <td width="10%" align="right">名称：</td>
	       <td width="20%" ><input class="middle_txt" type="text" name="c_name" id="c_name"/></td>
	       <td width="10%" align="right">日期：</td>
           <td width="20%" align="left"><input class="time_txt" id="SCREATE_DATE" name="SCREATE_DATE"
                                              datatype="1,is_date,10" maxlength="10" value="${start}" style="width:65px"
                                              group="SCREATE_DATE,ECREATE_DATE"/>
              <input class="time_ico" value=" " onclick="showcalendar(event, 'SCREATE_DATE', false);" type="button"/>
                                     至
              <input class="time_txt" id="ECREATE_DATE" name="ECREATE_DATE" datatype="1,is_date,10" value="${end}" style="width:65px"
                     maxlength="10" group="SCREATE_DATE,ECREATE_DATE"/>
              <input class="time_ico" value=" " onclick="showcalendar(event, 'ECREATE_DATE', false);" type="button"/>
           </td>
      </tr>
      
	  <tr>
	   <td   colspan="6" align="center">
	   		<input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1);"/>
            <input class="normal_btn" type="button" value="导出" onclick="expPurOrderInExcel();"/>
       </td>
      </tr>
      
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript">
autoAlertException();//输出错误信息
var myPage;
var url = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/queryPckMaterials.json";

var title = null;

var columns = [
    {header: "序号", align:'center',renderer:getIndex},
    {header: "规格", dataIndex: 'PACK_SPEC',  align: 'center'},
    {header: "包装类型", dataIndex: 'PACK_TYPE',  align: 'center'},
    {header: "名称", dataIndex: 'PACK_NAME',  align: 'center'},
    {header: "单位", dataIndex: 'PACK_UOM', style: 'text-align:left'},
    {header: "入库数量", dataIndex: 'IN_QTY', style: 'text-align:left'},
    {header: "出库数量", dataIndex: 'OUT_QTY', style: 'text-align:left'},
    {header: "账面库存", dataIndex: 'PACK_QTY', style: 'text-align:left'}//renderer: getItemValue
];

var len = columns.length;

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}

//导出
function expPurOrderInExcel() {
    fm.action = "<%=contextPath%>/report/partReport/partStockReport/PurOrderInReport/expPckMaterialsExcel.do";
    fm.target = "_self";
    fm.submit();
}

function clearInput() {
	//清空选定供应商
	document.getElementById("VENDER_ID").value = '';
	document.getElementById("VENDER_NAME").value = '';
}

</script>
</div>
</body>
</html>