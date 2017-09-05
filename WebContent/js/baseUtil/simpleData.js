var ShowFund = function(){
    var dealerId;
    var tagName;
    this.init = function(){
        var url = g_webAppName + '/common/FundAll/showFund.json';
        oMakeCall(url, this, {
            dealerId: this.dealerId
        });
    }
    this.callBack = function(json){
        this.showFundTypeList(json);
    }
    //资金类型列表显示
    this.showFundTypeList = function(json){
        var obj = document.getElementById(this.tagName);
        obj.options.length = 0;
        for (var i = 0; i < json.fundTypeList.length; i++) {
            obj.options[i] = new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID + '');
        }
    }
}

var ShowWare = function(){
    var dealerId;
    var tagName;
    this.init = function(){
        var url = g_webAppName + '/common/SimpleData/showWare.json';
        oMakeCall(url, this, {
            dealerId: this.dealerId
        });
    }
    this.callBack = function(json){
        this.showWareList(json);
    }
    //资金类型列表显示
    this.showWareList = function(json){
        var obj = document.getElementById(this.tagName);
        resetSelectTag(json, obj);
    }
}

function resetSelectTag(json, obj){
    obj.options.length = 0;
    for (var i = 0; i < json.list.length; i++) {
        obj.options[i] = new Option(json.list[i].VALUE, json.list[i].KEY + '');
    }
}

var AvailableAmount = function(){
    var fundTypeId;
    var dealerId;
    var tagName;
    var url = g_webAppName + '/common/FundAll/getAvailableAmount.json';
    this.init = function(){
        if (this.fundTypeId == null || this.fundTypeId == '') {
            alert('ERP与DMS系统资金账户不匹配');
            this.fundTypeId = '0';
        }
        
        oMakeCall(url, this, {
            fundTypeId: this.fundTypeId,
            dealerId: this.dealerId
        });
        
        
    };
    //显示可用余额
    this.showAvailableAmount = function(json){
        $(this.tagName).innerHTML = amountFormat(parseFloat(json.returnValue, 10));
    }
    this.callBack = function(json){
        this.showAvailableAmount(json);
    }
}



