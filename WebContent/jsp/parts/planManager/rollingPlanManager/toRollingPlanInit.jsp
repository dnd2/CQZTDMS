<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
    <title>月度需求计划编制</title>
    <style>#uploadDiv{margin-top: 10px;padding-top: 10px;border-top: 1px solid rgb(204, 204, 204);}</style>
    <script type="text/javascript">
        function getYearSelect(id, name, scope, value) {
            var date = new Date();
            var year = date.getFullYear();    //获取完整的年份
            var month = date.getMonth() + 2;
            if(month>12){
                year=year+1;
            }
            var str = "";
            str += "<select  id='" + id + "' name='" + name + "'  style='width:80px;' class='u-select'>";
            str += "<option selected value=''>-请选择-</option>";
            for (var i = (year - scope); i <= (year + scope); i++) {
                if (value == "") {
                    if (i == year) {
                        str += "<option  selected value =" + i + ">" + i + "</option >";
                    } else {
                        str += "<option   value =" + i + ">" + i + "</option >";
                    }
                } else {
                    str += "<option  " + (i == value ? "selected" : "") + "value =" + i + ">" + i + "</option >";
                }
            }
            str += "</select> 年";
            document.write(str);
        }
        function getMonThSelect(id, name, value) {
            var date = new Date();
            var month = date.getMonth() + 2;
            if(month>12){
                month=month-12;
            }
            var str = "";
            str += "<select  id='" + id + "' name='" + name + "'  style='width:45px;' class='u-select'>";
            str += "<option selected value=''>-请选择-</option>";
            for (var i = 1; i <= 12; i++) {
                if (value == "") {
                    if (i == month) {
                        str += "<option selected value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                    } else {
                        str += "<option  value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                    }
                } else {
                    str += "<option " + (i == value ? "selected" : "") + "value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                }
            }
            str += "</select> 月";
            document.write(str);
        }
    </script>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
    <form name='fm' id='fm' method="post" enctype="multipart/form-data">
        <div class="navigation">
            <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理> 采购计划管理  > 月度需求计划编制
        </div>
        <input type="hidden" id="PLAN_TYPES" name="PLAN_TYPES" value="<%=Constant.PART_PURCHASE_PLAN_TYPE_01 %>" />
        <input type="hidden" id="planIdTemp" name="planIdTemp" value="" />
        <div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
        <table class="table_query">
            <tr>
                <td class="right">计划年月:</td>
                <td >
                    <script type="text/javascript">
                        getYearSelect("MYYEAR", "MYYEAR", 1, '');
                    </script>
                    <script type="text/javascript">
                        getMonThSelect("MYMONTH", "MYMONTH", '');
                    </script>
                <td class="right"></td>
                <td ></td>
                <td class="right">计划单号：</td>
                <td><input name="planNo" id="planNo" value="" type="text" class="middle_txt"/></td>
            </tr>
            <tr id="tr1">
                <td colspan="6" class="center">
                    <input class="u-button" type="button" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1);" value="查 询"/>
                    <input class="normal_btn" style="width:120px;" type="button" value="导入月度计划" name="button5" onclick="showUpload('uploadDiv');">
                </td>
            </tr>
        </table>
        
        <!-- 批量导入start -->
        <div style="display: none;" id="uploadDiv">
            <table class="table_query">
                <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 上传文件</th>
                <tr>
                    <td colspan="6" style="padding-left: 12px; color:red;">
                        <label>格式：配件编码 +计划数量+配件备注</label>
                    </td>
                </tr>
                <tr>
                    <td colspan="6" style="padding-left: 12px;">
                    	<label>1.点击：</label>
                    	<input type="button" class="normal_btn" value="下载模版" onclick="downRollingPlanTemp()"/>
                    	<label>下载计划导入模板！</label>
                    </td>
                </tr>
                
                <tr>
                    <td colspan="6" style="padding-left: 12px;">
                    	<label>2.选择：</label>
                        <script type="text/javascript">
	                        getYearSelect("planYearImp", "planYearImp", 1, '');
	                    </script>
	                    <script type="text/javascript">
	                        getMonThSelect("planMonthImp", "planMonthImp", '');
	                    </script>
	                    
                        <label>，计划年月</label>
                        <font color="red">*(必选项)</font>
                    </td>
                </tr>
                
                <tr>
                    <td colspan="6" style="padding-left: 12px;">
                    	<label>3.选择：</label>
                        <input type="file" name="uploadFile" id="uploadFile" style="width: 250px" datatype="0,is_null,2000"/>
                        <label>要导入的计划文件！</label>
                        <font color="red">(必选项)</font>
                    </td>
                </tr>
               
                <tr>
                    <td colspan="6" style="padding-left: 12px;">
                    	<label>4.输入：</label>
                        <input type="text" name="uploadRemark" id="uploadRemark" class="middle_txt" style="width: 250px"/>
                        <label>计划备注(可选填)</label>
                    </td>
                </tr>
                <tr>
                    <td colspan="6" style="padding-left: 12px;">
                    	<label>5.点击：</label>
                        <input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="confirmUploadUpdate()"/>
                        <label>确认按钮，完成计划导入！</label>
                    </td>
                </tr>
            </table>
       		 </div>
       		 </div>
        </div>
        <!-- 批量导入end -->
        
        <!--分页 begin -->
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
        <!--分页 end -->
    </form>
</div>
<script type="text/javascript">
    var myPage;
    var url = "<%=contextPath%>/parts/planManager/PartPlanManager/getRollingPlanInfo.json";
    var title = null;
    var columns = [
			 {header: "序号", renderer: getIndex, align: 'center'},
             {header: "操作", align: 'center', dataIndex: 'PLAN_NO', renderer: myLink},
             {header: "计划单号", dataIndex: 'PLAN_NO', align: 'center'},
             {header: "计划种类", dataIndex: 'PLAN_NUMS', align: 'center'},
             {header: "计划数量", dataIndex: 'PLAN_COUNT', align: 'center'},
             {header: "计划年月", dataIndex: 'MONTH_DATE', align: 'center'},
             {header: "创建日期", dataIndex: 'CREATE_DATE', align: 'center'},
             {header: "是否提交", dataIndex: 'IS_SUBMIT', align: 'center',renderer:getItemValue},
             {header: "提交日期", dataIndex: 'SUBMIT_DATE', align: 'center'}
        ];
    //链接
    function myLink(value, meta, record) {
    	var tid = record.data.TID;
    	var isSubmit = record.data.IS_SUBMIT;
    	var str = "<a href=\"#\" onclick='view(\"" + tid + "\")'>[查看]</a>&nbsp;";
    		if(isSubmit=='<%=Constant.IF_TYPE_NO%>'){
    			str+= "<a href=\"#\" onclick='edit(\"" + tid + "\")'>[修改]</a>&nbsp;";
	    		str+= "<a href=\"#\" onclick='subPlanNew(\"" + tid +"\")'>[提交]</a>&nbsp;";
	    		str+= "<a href=\"#\" onclick='delPlan(\"" + tid + "\")'>[删除]</a>&nbsp;";
    		}
    		
    		
    		//暂时注释掉
    		//str+= "<a href=\"#\" onclick='expDlt(\"" + tid +"\")'>[导出计划]</a>&nbsp;";
    		//str+= "<a href=\"#\" onclick='expErrDlt(\"" + tid + "\")'>[导出错误]</a>";
    		//暂时注释掉
    		
    		
        return str;
    }
    //查看
    function view(value) {
        location = "<%=contextPath%>/parts/planManager/PartPlanManager/toRollingPlanSelect.do?planId="+value+"&planTypes=<%=Constant.PART_PURCHASE_PLAN_TYPE_01%>";
    }
    //修改
    function edit(value){
    	location = "<%=contextPath%>/parts/planManager/PartPlanManager/toRollingPlanEdit.do?planId="+value+"&planTypes=<%=Constant.PART_PURCHASE_PLAN_TYPE_01%>";
    }
    //滚动计划提交
    function subPlanNew(tid) {
        MyConfirm('确定要提交需求？',function(){
        	//提交给各个部门确认需求
        	var urlkey = "<%=contextPath%>/parts/planManager/PartPlanManager/subPlanStayConfim.json?planId=" + tid;
        	makeNomalFormCall(urlkey, getResult, "fm");
        });
    }
    //删除
    function delPlan(value) {

        MyConfirm('是否确定删除此计划单？',function(){
        	var url = "<%=contextPath%>/parts/planManager/PartPlanManager/delPlanByPlanId.json?planId="+value;
        	makeNomalFormCall(url, getResult, 'fm');
        });
        
    }
    //审核结果
    function getResult(json){
    	var success = json.success;
    	var error = json.error;
    	var ex = json.Exception;
    	if(success!=null && success!='' && success!='null' && success!='undefined'){
    		MyAlert(success);
    	}else if(error!=null && error!='' && error!='null' && error!='undefined'){
    		MyAlert(error);
    	}else if(ex!=null && ex!='' && ex!='null' && ex!='undefined'){
    		MyAlert(json.Exception.message);
    	}else{
    		MyAlert("操作异常，请联系管理员！");
    	}
    	btnEable();
    	__extQuery__(1);
    }
    //导出计划
    function expDlt(value) {
    	document.getElementById("planIdTemp").value = value;
        fm.action = "<%=contextPath%>/parts/planManager/PartPlanManager/expPlanById.do";
        fm.target = "_self";
        fm.submit();
    }
    //导出错误提示
    function expErrDlt(value) {
        document.getElementById("planIdTemp").value = value;
        fm.action = "<%=contextPath%>/parts/planManager/PartPlanManager/exportErrorInfoExcel.do";
        fm.target = "_self";
        fm.submit();
    }
    
    /**=============================================================================================================**/
    
    //显示隐藏
    function showUpload(id) {
        $('#'+id).toggle();
    }
    //下载计划模板
    function downRollingPlanTemp(){
    	MyConfirm('确认下载模板？',function(){
    		fm.action = "<%=contextPath%>/parts/planManager/PartPlanManager/downRollingPlanTemp.do";
     		fm.submit();
        });
    }
    //上传更新检查和确认信息
    function confirmUploadUpdate() {
    	var planYearImp = jQuery('#planYearImp').val();
    	var planMonthImp = jQuery('#planMonthImp').val();
    	if(planYearImp==''){
    		MyAlert('请选择计划年份');
    		return;
    	}
    	if(planMonthImp==''){
    		MyAlert('请选择计划月份');
    		return;
    	}
        var fileValue = jQuery('#uploadFile').val();
        if (fileValue == '') {
            MyAlert('请选择上传文件');
       		return;
        }
        var start = fileValue.lastIndexOf('.');
       	var gsStr = fileValue.substring(start,start.length).toUpperCase();
       	if(gsStr=='.XLS' || gsStr=='.XLSX'){
       		MyConfirm('确定新增滚动计划？',function(){
		        btnDisable();
		        fm.action = g_webAppName + "/parts/planManager/PartPlanManager/impRollingPlan.do";
		        fm.submit();
            });
       	}else{
       		MyAlert('只支持.XLS,.XLSX格式的文件，不支持'+gsStr+'格式的文件');
       		return;
       	}
    }
</script>
</body>
</html>