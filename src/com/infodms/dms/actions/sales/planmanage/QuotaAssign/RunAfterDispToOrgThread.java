package com.infodms.dms.actions.sales.planmanage.QuotaAssign;

import com.infodms.dms.dao.sales.planmanage.QuotaAssignDao;

import java.util.List;


public class RunAfterDispToOrgThread implements Runnable {
    private final QuotaAssignDao dao = QuotaAssignDao.getInstance();
    private String procName="";
    private List<Object> ins=null;
    private List<Integer> outs=null;

    public RunAfterDispToOrgThread(String procName, List<Object> ins, List<Integer> outs){
           this.procName=procName;
           this.ins=ins;
           this.outs=outs;
    }
    public void run() {
        dao.callProcedure(procName,ins,outs);
    }
}