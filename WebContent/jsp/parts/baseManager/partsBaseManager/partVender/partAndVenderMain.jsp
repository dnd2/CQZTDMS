<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	String curPage = (String)request.getAttribute("curPage");
%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件供应商维护</title>
<script type="text/javascript">
    var myPage;
    var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartAndVenderManager/getPartAndVenderMain.json";
    var title = null;
    var columns = [
			{header: "序号", align: 'center', renderer: getIndex},
			{id:'action',header: "操作",sortable: false,dataIndex: 'SV_ID',renderer:myLink , align: 'center'},
			{header: "供应商代码", dataIndex: 'DEALER_CODE',  style: "text-align: center", width: '5%'},
			{header: "供应商名称", dataIndex: 'DEALER_NAME', style: "text-align: center", width: '5%'},
			{header: "配件编码", dataIndex: 'PART_OLDCODE', style: "text-align: center", width: '5%'},
			{header: "配件名称", dataIndex: 'PART_CNAME',  style: "text-align: center", width: '5%'},
			{header: "配件件号", dataIndex: 'PART_CODE',  style: "text-align: center", width: '5%'},
			{header: "默认供应商", dataIndex: 'IS_DEFULT',  align: 'center',renderer:setIsdefault},
			{header: "修改时间", dataIndex: 'MODIFY_DATE',  align: 'center'},
			{header: "修改人", dataIndex: 'MODIFY_USER',  align: 'center'},
			{header: "是否有效", dataIndex: 'STATE',  align: 'center',renderer: getItemValue},
			{header: "备注", dataIndex: 'REMARK',  align: 'center'}
    	];

    //设置超链接
    function myLink(value, meta, record) {
    	var state = record.data.STATE;
    	var str = '<a href="javascript:void(0)" onclick="toUpdateRelation(\''+value+'\')">[修改]</a>';
    	if(state=='<%=Constant.IF_TYPE_YES%>'){
    		str += '&nbsp;<a href="javascript:void(0)" onclick="deleteRelateion(\''+value+'\',\'<%=Constant.IF_TYPE_NO%>\')">[失效]</a>';
    	}else{
    		str += '&nbsp;<a href="javascript:void(0)" onclick="deleteRelateion(\''+value+'\',\'<%=Constant.IF_TYPE_YES%>\')">[有效]</a>';
    	}
    	
    	return String.format(str);
    }
    //操作结果
    function getResult(json){
    	var success = json.success;
    	if(success!='' && success!=null && success!='null' && success!='undefined'){
    		layer.msg(success, {icon: 1});
    	}else{
    		MyAlert(json.error);
    	}
    	btnEnable();
    	__extQuery__(1);
    }
    /*========================================*/
    //默认供应商
    function setIsdefault(value, meta, record){
    	if(value=='0'){
    		//
    		return '否';
    	}else{
    		return '是';
    	}
    }
    //状态设置
    function deleteRelateion(svId,state){
    	var str = '';
    	if(state=='<%=Constant.IF_TYPE_YES%>'){
    		str = '确认设置为有效？';
    	}else if(state=='<%=Constant.IF_TYPE_NO%>'){
    		str = '确认设置为失效？';
    	}else{
    		MyAlert('设置状态异常，请联系管理员？');
    		return;
    	}
    	MyConfirm(str, function(){
    		btnDisable();
    		var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartAndVenderManager/deleteRelateion.json?state='+state+'&svId='+svId;
    		sendAjax(url, getResult, 'fm');
    		
    	});
    }
    //修改
    function toUpdateRelation(svId){
   		$('input[type="button"]').prop('disabled',true);
   		location = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartAndVenderManager/toUpdateRelation.do?svId='+svId;
    }
    //根据id显示页面
    function showUpload(id){
    	$('#'+id).toggle();
    }
    //下载模板
    function exportExcelTemplate(){
    	fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartAndVenderManager/templateDownload.do";
        fm.submit();
    }
    //确认上传文件
    function confirmUpload(){
    	//验证文件格式
    	var file = $('#uploadFile').val();
    	if(file=='' || file==null){
    		MyAlert('请选择上传文件');
    		return;
    	}
    	var start = file.lastIndexOf('.');
    	var gsStr = file.substring(start,start.length).toUpperCase();
    	if(gsStr=='.XLS' || gsStr=='.XLSX'){
    		btnDisable();
    		if(confirm('确认上传文件？')){
    			fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartAndVenderManager/uploadXlsFile.do";
      		    fm.submit();
      		}else{
      			btnEnable();
      		}
      	}else{
      		MyAlert('只支持.XLS,.XLSX格式的文件，不支持'+gsStr+'格式的文件');
      		return;
      	}
      }

//新增
function toPartAndVenderAdd(){
	location = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartAndVenderManager/toPartAndVenderAdd.do';
}
//根据条件导出数据
function exportByCondition(){
	btnDisable();
	MyConfirm("确认导出?",confirmResult,[],noConfirmResult,[]);
}
function confirmResult(){
	fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartAndVenderManager/exportByCondition.do";
    fm.submit();
}
function noConfirmResult(){
	btnEnable();
}

function czan(){
	btnEnable();
}
if('${info}'!=null && '${info}'!='' && '${info}'!='null'){
	MyAlert('${info}');
}

$(function(){__extQuery__(1);});
</script>

</head>
<body>
<div class="wbox">
    <div class="navigation">
    	<img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置：&gt;配件管理 &gt;基础信息管理&gt;计划相关信息维护&gt;供应商管理&gt;配件供应商维护
    </div>
    <form method="post" name="fm" id="fm" enctype="multipart/form-data">
    <div class="form-panel">
        <h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
        <div class="form-body">
        <table class="table_query">
            <tr>
                <td class="right">供应商代码：</td>
                <td>
                	<input class="middle_txt" type="text" id="dealerCode" name="dealerCode"/>
               	</td>
                <td class="right">供应商名称：</td>
                <td>
                	<input class="middle_txt" type="text" id="dealerName" name="dealerName"/>
                </td>
                <td class="right">是否有效：</td>
                <td>
                	<script type="text/javascript">
                        genSelBox("state", <%=Constant.IF_TYPE%>, <%=Constant.IF_TYPE_YES%>, true, "");
                    </script>
                </td>
            </tr>
			<tr>
				<td class="right">配件编码：</td>
				<td>
					<input class="middle_txt" type="text" id="partOldcode" name="partOldcode"/>
				</td>
				<td class="right">配件名称：</td>
				<td>
					<input class="middle_txt" type="text" id="partCname" name="partCname"/>
				</td>
				<td class="right">配件件号：</td>
				<td>
					<input class="middle_txt" type="text" id="partCode" name="partCode"/>
				</td>
			</tr>
            <tr>
                <td colspan="10" class="center">
                	<input class="u-button" type="button" value="查 询" onclick="__extQuery__(1);"/>&nbsp;
                	<input class="u-button" type="reset" value="重置" onclick="reset()">&nbsp;
					<input class="u-button" type="button" value="新增" onclick="toPartAndVenderAdd()"/>&nbsp;
                	<input class="u-button" type="button" value="导出" onclick="exportByCondition()"/>&nbsp;
                </td>
            </tr>
        </table>
        </div>
        </div>
        
        <div style="display:none; heigeht: 5px" id="uploadDiv">
	        <table class="table_query">
	            <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif"/> 上传文件</th>
	            <tr>
	                <td colspan="2">
	                    <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate();" />
	                    <font color="red">提示文件过大，可以删除第二页的供应商信息，选择文件，点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
	                    <input type="file" name="uploadFile" id="uploadFile" style="width: 250px" value=""/>
	                    <font color="red">*</font>
	                    <input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="confirmUpload()"/>
	                </td>
	            </tr>
	        </table>
	    </div>
	    
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>
</div>
</body>
</html>