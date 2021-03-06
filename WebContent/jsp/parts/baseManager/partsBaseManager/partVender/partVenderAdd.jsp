<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>供应商信息添加</title>
    <script type="text/javascript">

        function confirmAdd() {
            
            if (!submitForm('fm')) {
                return;
            }
            var isAbroad = $("#IS_ABROAD")[0].value;
        	if(!isAbroad){
            	layer.msg("请选择国内/国外!");
            	return;
        	}
        	
        	if($("#TEL")[0].value!=null&&$("#TEL")[0].value!=""){
                var tel = $("#TEL")[0].value;
                var pattern =/((^[0-9]{3,4}\-[0-9]{7,8})(-(\d{3,}))?$)|(^[0-9]{7,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^1[0-9]{10}$)/;
    			if(!pattern.exec(tel)){
    				layer.msg("请输入正确的联系电话!");
    				$("#TEL")[0].value="";
    				$("#TEL")[0].focus();
    				return;
    			}
            }

        	if($("#FAX")[0].value!=null&& $("#FAX")[0].value!=""){
                var s = $("#FAX")[0].value;
            	var pattern =/(^[0-9]{3,4}\-[0-9]{7,8}\-[0-9]{3,4}$)|(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{7,8}\-[0-9]{3,4}$)|(^[0-9]{7,15}$)/;
            	 if(!pattern.exec(s)){
            		 layer.msg('请输入正确的传真号码!');
            		 $("#FAX")[0].value="";
            		 $("#FAX")[0].focus();
            		 return;
            	 }
            }
            
        	var venderType = $("#VENDER_TYPE")[0].value;
        	if(!venderType){
            	layer.msg("请选择供应商类型!");
            	return;
        	}
        	MyConfirm("确定保存?", saveRecord);
        }
        
        function saveRecord(){
    		btnDisable();
            var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartVenderManager/addPartVenderInfo.json";
            sendAjax(url, getResult, 'fm');
        }

        function getResult(jsonObj) {
        	btnEnable();
            if (jsonObj != null) {
                var error = jsonObj.error;
                var success = jsonObj.success;
                var exceptions = jsonObj.Exception;
                if (error) {
                    MyAlert(error);
                }else if (success) {
                    MyAlert(success, goback);
                }else if(exceptions){
        	    	layer.msg(exceptions.message);
        	    }
            }
        }

        //返回查询页面
        function goback() {
            window.location.href = '<%=contextPath %>/parts/baseManager/partsBaseManager/PartVenderManager/queryPartVenderInit.do';
        }
    </script>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt;
        基础信息管理 &gt; 配件基础信息维护&gt; 供应商信息维护&gt; 新增
    </div>
    <form id="fm" name="fm" method="post">
        <div class="form-panel">
            <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>供应商信息</h2>
            <div class="form-body">
                <table class="table_query">
                    <tr>
                        <td class="right">供应商编码：</td>
                        <td>
                            <input class="middle_txt" type="text" name="VENDER_CODE" id="VENDER_CODE" datatype="0,is_null" maxlength="50"/>
                        </td>
                        <td class="right">供应商名称：</td>
                        <td><input type="text" class="normal_txt middle_txt" name="VENDER_NAME" id="VENDER_NAME" datatype="0,is_null" maxlength="60"/>
                        </td>
                        <td class="right">国内/国外：</td>
                        <td>
                            <script type="text/javascript">
                                genSelBoxExp("IS_ABROAD", <%=Constant.IS_ABROAD_V%>, "", false, "short_sel u-select", "", 'true', '');
                            </script>
                            </td>
                    </tr>
                    <tr>
                        <td class="right">联系人：</td>
                        <td><input class="middle_txt" type="text" name="LINKMAN"  id="linkman" maxlength="50"/></td>
                        <td class="right">联系电话：</td>
                        <td>
                            <input type="text" class="normal_txt middle_txt" name="TEL" id="TEL" maxlength="20"/>
                        </td>
                        <td   class="right">传真：</td>
                        <td>
                            <input type="text" class="normal_txt middle_txt" name="FAX" id="FAX" maxlength="20"/>
                        </td>
                    </tr>
                        <td class="right">供应商类型：</td>
                        <td>
                            <script type="text/javascript">
                                genSelBoxExp("VENDER_TYPE", <%=Constant.VENDER_TYPE%>, "", false, "short_sel u-select", "", 'true', '');
                            </script>
                        </td>
                        <td class="right">地址：</td>
                        <td colspan="3"><input class="middle_txt input-long-txt" type="text" name="ADDR" id="addr" maxlength="100"/></td>
                    </tr>
                </table>
                <table class="table_edit" width="100%">
                    <tr>
                        <td align="center">
                            <input type="button" name="saveBtn" id="saveBtn" value="保 存"  onclick="confirmAdd();" class="u-button"/>
                            <input type="button" name="saveBtn2" id="saveBtn2" value="返 回"  onclick="javascript:goback();" class="u-button"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </form>
</div>
</body>
</html>
