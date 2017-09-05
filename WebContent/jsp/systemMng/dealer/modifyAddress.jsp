<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.infodms.dms.po.TtAddressAreaRPO" %>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
    <title>经销商维护</title>
    <script type="text/javascript">
        function doInit()
        {
            genLocSel('txt1','txt2','txt3','<c:out value="${map.PROVINCE_ID}"/>','<c:out value="${map.CITY_ID}"/>','<c:out value="${map.AREA_ID}"/>'); // 加载省份城市和县
        }

        function chkAddress() {
            var status = document.getElementById("ADDRESSSTATUS").value ;

            if(status == <%=Constant.STATUS_DISABLE%>) {
                var url = "<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/cheAddress.json" ;

                makeFormCall(url, chkResult, 'fm') ;
            }
        }

        function chkResult(json) {
            var flag = json.flag ;

            if(flag == "success") {

            } else {
                document.getElementById("ADDRESSSTATUS").value = <%=Constant.STATUS_ENABLE%> ;
                MyAlert("该地址存在订单未处理完成,不能进行无效操作!") ;

                return false ;
            }
        }
    </script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;修改经销商地址</div>
<form method="post" name = "fm" id="fm" >
    <input id="DEALER_ID" name="DEALER_ID" type="hidden" value="${dealerId}"/>
    <input id="addressId" name="addressId" type="hidden" value="<c:out value="${map.ID}"/>"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
        <tr>
            <td class="table_query_2Col_label_6Letter">地址名称：</td>
            <td>
                <input type='text'  class="middle_txt" name="address"  id="address" datatype="0,is_null,150" size="80" style="width:320px"  value="<c:out value="${map.ADDRESS}"/>" maxlength="150"/>
            </td>
            <td class="table_query_2Col_label_6Letter">联系人：</td>
            <td>
                <input type='text'  class="middle_txt" name="linkMan"  id="linkMan" datatype="1,is_name,8"  value="<c:out value="${map.LINK_MAN}"/>" maxlength="8"/>
            </td>
        </tr>
        <tr>
            <td class="table_query_2Col_label_6Letter">手机：</td>
            <td>
                <input type='text'  class="middle_txt" name="mobilePhone"  id="mobilePhone" datatype="1,is_name,20"  value="<c:out value="${map.MOBILE_PHONE}"/>" />
            </td>
            <td class="table_query_2Col_label_6Letter">电话：</td>
            <td>
                <input type='text'  class="middle_txt" name="tel"  id="tel" datatype="1,is_null,30"  value="<c:out value="${map.TEL}"/>" maxlength="30"/>
            </td>
        </tr>
        <tr>
            <td class="table_query_2Col_label_6Letter">地址状态：
            </td>
            <td>
                <label>
                    <script type="text/javascript">
                        genSelBoxExp("ADDRESSSTATUS",<%=Constant.STATUS%>,"<c:out value="${map.STATUS}"/>",'',"",'onchange=\'chkAddress() ;\'',"false",'');
                    </script>
                </label>
            </td>
            <td class="table_query_2Col_label_6Letter">收货单位：</td>
            <td>
                <input type='text'  class="middle_txt" name="myaddress"  id="myaddress" datatype="1,is_null,90"  value="<c:out value="${map.RECEIVE_ORG}"/>" maxlength="90"/>
            </td>
        </tr>
        <tr>
            <td class="table_query_2Col_label_6Letter">省份：</td>
            <td><select class="u-select" id="txt1" id="province" name="province" onchange="_genCity(this,'txt2')"></select> </td>
            <td class="table_query_2Col_label_6Letter">地级市：</td>
            <td><select class="u-select" id="txt2" id="city" name="city"  onchange="_genCity(this,'txt3')" ></select></td>
        </tr>
        <tr>
            <td class="table_query_2Col_label_6Letter">县：</td>
            <td><select class="u-select" id="txt3"  id="area" name="area" ></select></td>
        </tr>
        <tr style=display:none>
            <td class="table_query_2Col_label_6Letter">业务范围:</td>
            <td colspan="3">
                <%
                    List<Map<String, Object>> areaList = (List<Map<String, Object>>)request.getAttribute("areaList") ;
                    Map<String, Object> taarList = (Map<String, Object>)request.getAttribute("map") ;

                    if(areaList != null) {
                        int areaLen = areaList.size() ;

                        for(int i=0; i<areaLen; i++) {
                            boolean flag = false ;
                            long areaId = Long.parseLong(areaList.get(i).get("AREA_ID").toString()) ;
                            String areaName = areaList.get(i).get("AREA_NAME").toString() ;

                            if(taarList != null) {
                                int taarLen = taarList.size() ;

                                for(int j=0; j<taarLen; j++) {
                                    long checkedArea = Long.parseLong(taarList.get("B_AREA_ID") == null ? "0" : taarList.get("B_AREA_ID").toString()) ;

                                    if(areaId == checkedArea) {
                                        flag = true ;

                                        break ;
                                    }
                                }
                            }

                            if(flag) {
                %>
                <input type="checkBox" name="addressAreas" id="<%=areaId %>" value="<%=areaId %>|${dealerId}" checked="checked" disabled="disabled" />
                <label for="<%=areaId %>"><%=areaName %></label>
                <%
                } else {
                %>
                <input type="checkBox" name="addressAreas" id="<%=areaId %>" value="<%=areaId %>|${dealerId}" />
                <label for="<%=areaId %>"><%=areaName %></label>
                <%
                            }
                        }
                    }
                %>
            </td>
        </tr>
        <tr>
            <td class="table_query_2Col_label_6Letter">备注：</td>
            <td><textarea name="remark" id="remark" cols="40" rows="2" datatype="1,is_textarea,1000"><c:out value="${map.REMARK}"/></textarea></td>
        </tr>
    </table>
    <table class=table_query>
        <tr>
            <td>
                <input type="button" value="修改" name="saveBtn" class="normal_btn" onclick="modifyAddress()"/>
                <input type="button" value="关闭" name="cancelBtn"  class="normal_btn" onclick="_hide();" /></td>
        </tr>
    </table>
</form>

<script type="text/javascript" >
    function modifyAddress()
    {
        /*var aAreas = document.getElementsByName("addressAreas") ;

         if(!aAreas || aAreas.length == 0) {
         MyAlert("业务范围未维护!") ;

         return false ;
         } else {
         var iAreaLen = aAreas.length ;
         var bAreaFlag = false ;

         for(var i=0; i<iAreaLen; i++) {
         if(aAreas[i].checked) {
         bAreaFlag = true ;
         }
         }

         if(!bAreaFlag) {
         MyAlert("请选择业务范围!") ;

         return false ;
         }
         }*/
        var province = document.getElementById('txt1');
        var provinceValue=province.options[province.selectedIndex].value
        var city = document.getElementById('txt2');
        var cityValue=city.options[city.selectedIndex].value
        var area = document.getElementById('txt3');
        var areaValue=area.options[area.selectedIndex].value
        if(provinceValue==''){
            MyAlert('必须填写省份！');
            return;
        }
        if(cityValue==''){
            MyAlert('必须填写地级市！');
            return;
        }

        if(areaValue==''){
            MyAlert('必须填写区县！');
            return;
        }

        if(submitForm('fm'))
        {
            if(confirm("确认修改吗？"))
            {
                isSubmit();
            }
        }
    }

    function isSubmit() {
        //var url = "<%=request.getContextPath()%>/sales/storageManage/AddressAddApply/chkAddressSame.json" ;

        //makeFormCall(url, toSubmit, "fm") ;
        makeNomalFormCall('<%=contextPath%>/sysmng/dealer/DealerInfo/updateAction.json',showResult,'fm');

    }

    function toSubmit(json){
        var flagStr = json.flagStr ;
        var areaStr = json.areaStr ;

        if(flagStr == "error") {
            MyAlert("业务范围为:" + areaStr + "的地址,已存在!") ;

            return false ;
        }

        makeNomalFormCall('<%=contextPath%>/sysmng/dealer/DealerInfo/updateAction.json',showResult,'fm');
    }
    //回调方法
    function showResult(json){
        if(json.returnValue == '1'){
            parentContainer.parentMonth();
            _hide();
        }else{
            windows.parent.MyAlert("操作失败！请联系系统管理员！");
        }
    }
</script>

</body>
</html>
