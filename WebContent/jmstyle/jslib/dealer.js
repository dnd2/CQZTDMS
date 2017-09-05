/**
 * 经销商相关方法类
 * 
 * @author Michael
 * @date 2017-08-08
 * @desc 重构原经销商选择树所包含的方法
 */
var Dealer = {
    global: {
        varr: null,
        ajax: null,
        selType: null
    },
    /**
     * 为了获取创建树的全局对象，在调用dealerPos等方法前需调用此函数
     * 
     * @param {String} globalVar  设定树的全局变量名
     * @param {Function} callback 执行ajax的方法
     * @param {Boolean} selType   选中项, true  Checkbox, false Radio 
     */
    init: function (globalVar, callback, selType) {
        if (window[globalVar]) {
            this.global.varr = window[globalVar];
        }

        if (callback && typeof callback == 'function') {
            this.global.ajax = callback;
        }

        window.columns = [
            { header: "序号", align: 'center', renderer: getIndex, width: '7%' },
            { header: "选择", dataIndex: 'dealerId', align: 'center', width: '33px', renderer: Dealer.seled },
            { header: "经销商代码", dataIndex: 'dealerCode', align: 'center' },
            { header: "经销商全称", dataIndex: 'dealerName', align: 'center' }
        ];

        this.global.selType = selType || false;

        return this.global;
    },

    doConfirmAddRelation: function (table) {
        table = table || 'myTable';
        var tab = document.getElementById(table);
        if (!tab) {
            return false;
        }
        var codes = "";
        var ids = "";
        var names = "";
        if (tab.rows.length > 1) {
            for (var i = 1; i < tab.rows.length; i++) {
                var checkObj = tab.rows[i].cells[1].firstChild;
                if (checkObj.checked == true) {
                    var code = tab.rows[i].cells[2].innerText;
                    if (codes)
                        codes += "," + code;
                    else
                        codes = code;
                    var id = tab.rows[i].cells[1].firstChild.id;
                    if (ids.length > 0)
                        ids += "," + id;
                    else
                        ids = id;
                    var name = tab.rows[i].cells[3].innerText;
                    if (names) {
                        names += "," + name;
                    } else {
                        names = name;
                    }
                }
            }
        }
        if (codes && codes.length > 0) {
            $('#DEALER_CODE')[0].value = codes;
        } else {
            return false;
        }
        if (ids && ids.length > 0) {
            $('#DEALER_IDS')[0].value = ids;
        } else {
            return false;
        }
        if (names && names.length > 0) {
            $('#DEALER_NAME')[0].value = names;
        } else {
            return false;
        }
        return true;
    },

    dealerPos: function (id) {
        if (!Dealer.global.varr || !Dealer.global.ajax) {
            return;
        }

        var orgid = Dealer.global.varr.aNodes[id].id;
        var orgname = Dealer.global.varr.aNodes[id].name;
        $('#DEPT_ID')[0].value = orgid;

        Dealer.global.ajax(1);
    },

    seled: function (value, meta, record) {
    	var sType = Dealer.global.selType ;
    	var sIsMulti = typeof sType == 'undefined' ? true : false;
    	
    	if(sType == "false") {
    		sIsMulti = false ;
    	} else if(sType == "") {
    		sIsMulti = false ;
    	} else {
    		sIsMulti = true ;
    	}

        if (sIsMulti == false)
            return "<input type='radio' onclick='Dealer.singleSelect(" + value + ",\"" + record.data.dealerCode + "\",\"" + record.data.dealerShortname + "\")' name='de' id='" + value + "' />";
        else
            return "<input type='checkbox' value='" + value + "' name='chkbox' id='" + value + "' /><input type='hidden' id=\"c" + value + "\"  value=\"" + record.data.dealerCode + "\" /><input type='hidden' id=\"n" + value + "\"  value=\"" + record.data.dealerName + "\" />";
    },

    singleSelect: function (did, code, sname) {
        $('#DEALER_ID')[0].value = did;
        $('#DEALER_IDS')[0].value = did;
        $('#DEALER_NAME')[0].value = sname;

        if ($('#DEPT_NAME')[0] != null) {
            $('#DEPT_NAME')[0].value = "";
        }

        if ($('#DEPT_ID')[0] != null) {
            $('#DEPT_ID')[0].value = "";
        }
        
        try{
        	if(inputId!=undefined && inputId!='' && inputId != null && inputId != 'null'){
        		 parentContainer.document.getElementById(inputId).value = did;
        	}
        	if(inputCode!=undefined && inputCode!='' && inputCode != null && inputCode != 'null'){
	       		 parentContainer.document.getElementById(inputCode).value = code;
	       	}
        	if(inputName!=undefined && inputName!='' && inputName != null && inputName != 'null'){
	       		 parentContainer.document.getElementById(inputName).value = sname;
	       	}
            _hide();
        }catch(e){}

    }
};