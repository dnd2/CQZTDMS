<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%String contextPath = request.getContextPath();%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>

    <title>滚动计划编制</title>
    <script type="text/javascript">
    	jQuery.noConflict();//初始化jquery
        loadcalendar();//初始化方法
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
            <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：总部采购管理&gt; 采购计划管理 &gt; 月度需求计划审核
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
                </td>
            </tr>
        </table>
        
        <!-- 批量导入start -->
        <div style="display: none;" id="uploadDiv">
            <table class="table_query">
                <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 上传文件</th>
                <tr>
                    <td colspan="6" style="padding-left: 12px; color:red;">
                        <label>格式：备件编码 +计划数量+备件备注</label>
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
                        <input type="text" name="uploadRemark" id="uploadRemark" style="width: 250px"/>
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
	//btnEable();btnDisable();//全局
   // autoAlertException();//输出错误信息
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
             {header: "状态", dataIndex: 'STATUS', align: 'center',renderer:getV},
             {header: "创建日期", dataIndex: 'CREATE_DATE', align: 'center'},
             {header: "是否提交", dataIndex: 'IS_SUBMIT', align: 'center',renderer:getItemValue},
             {header: "提交日期", dataIndex: 'SUBMIT_DATE', align: 'center'}
        ];
    //链接
    function myLink(value, meta, record) {
    	var tid = record.data.TID;
    	var isSubmit = record.data.IS_SUBMIT;
    	var status = record.data.STATUS;
    	
    	var str = "<a href=\"#\" onclick='view(\"" + tid + "\")'>[查看]</a>&nbsp;";
    		if(isSubmit=='<%=Constant.IF_TYPE_YES%>' && status==0){
    			str+= "<a href=\"#\" onclick='edit(\"" + tid + "\")'>[审核]</a>&nbsp;";
	    		
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
    
    function getV(value, meta, record) {

    		if(value==0){
    			return "未审核";
	    		
    		}else{return "已通过";}
    
    }
    //修改
    function edit(value){
    	location = "<%=contextPath%>/parts/planManager/PartPlanManager/toRollingPlanSh.do?planId="+value+"&planTypes=<%=Constant.PART_PURCHASE_PLAN_TYPE_01%>";
    }
    //滚动计划提交
    function subPlanNew(tid) {
        if(confirm('确定要提交需求？')){
        	//提交给各个部门确认需求
        	var urlkey = "<%=contextPath%>/parts/planManager/PartPlanManager/subPlanStayConfim.json?planId=" + tid;
        	sendAjax(urlkey, getResult, "fm");
        }
    }
    //删除
    function delPlan(value) {
        if(confirm('是否确定删除此计划单？')){
        	var url = "<%=contextPath%>/parts/planManager/PartPlanManager/delPlanByPlanId.json?planId="+value;
        	sendAjax(url, getResult, 'fm');
        }

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
        jQuery('#'+id).toggle();
    }
    //下载计划模板
    function downRollingPlanTemp(){
    	if(confirm('确认下载模板？')){
    		fm.action = "<%=contextPath%>/parts/planManager/PartPlanManager/downRollingPlanTemp.do";
     		fm.submit();
    	}
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
       		if(confirm('确定新增滚动计划？')){
		        btnDisable();
		        fm.action = g_webAppName + "/parts/planManager/PartPlanManager/impRollingPlan.do";
		        fm.submit();
	        }
       	}else{
       		MyAlert('只支持.XLS,.XLSX格式的文件，不支持'+gsStr+'格式的文件');
       		return;
       	}
    }
</script>
</body>
</html>