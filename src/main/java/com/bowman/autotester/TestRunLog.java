package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

/**
* Test run log entity
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-02 
*/ 

@Entity
@Table(name = "TEST_RUN_LOG")
@SequenceGenerator(name = "TEST_RUN_LOG_SEQ", allocationSize = 1)
@NamedQueries({
    @NamedQuery(name = "getTestRunLogs",
                query = "SELECT a FROM TestRunLog a WHERE a.testRun.iID = :test_run_id "),
})
public class TestRunLog {   
    
    /**********************************
              attributes
    **********************************/ 
    
    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_RUN_LOG_SEQ")
    @Column(name = "ID")
    private int iID;    
    
    @OneToOne
    @JoinColumn(name = "TEST_RUN_FK", nullable = false)
    private TestRun testRun;    
    
    @Column(name = "LOG_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date logDate;
    
    @Column(name = "RESULT", length = 10)
    private String sResult;
    
    @Column(name = "LOG", length = 4000)
    private String sLog;
    
    /**********************************
              constructors
    **********************************/ 
    
    public TestRunLog() {
        
        this.iID = -1;
        this.testRun = null;
        this.logDate = null;
        this.sResult = null;
        this.sLog = null;
        
    }
    
    /**    
    * table TEST_RUN_LOG
    * column ID - integer, primary key, autogenerated
    * @param testRun TEST_RUN_FK - integer, foreign key to TEST_RUN.ID, not null
    * @param logDate LOG_DATE - date
    * @param sResult RESULT - varchar, result
    * @param sLog LOG - varchar
    * */     
    public TestRunLog(TestRun testRun, Date logDate, String sResult, String sLog) {
        
        this.testRun = testRun;
        this.logDate = logDate;
        this.sResult = sResult;
        this.sLog = sLog;
        
    }
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return int 
    */
    public int getID() {
        
        return this.iID;
        
    }
    
    /**  
    * @return TestRun
    */
    public TestRun getTestRun() {
        
        return this.testRun;
        
    }
    
    /**  
    * @param testRun test run
    */      
    public void setTestRun(TestRun testRun) {
        
        this.testRun = testRun;
        
    }    
    
    /**   
    * @return Date 
    */
    public Date getLogDate() {
        
        return this.logDate;
        
    }
    
    /**  
    * @param logDate log date
    */      
    public void setLogDate(Date logDate) {
        
        this.logDate = logDate;
        
    }
    
    /**   
    * @return String 
    */
    public String getResult() {
        
        return this.sResult;
        
    }
    
    /**  
    * @param sResult result
    */      
    public void setResult(String sResult) {
        
        this.sResult = sResult;
        
    }    
    
    /**   
    * @return String 
    */
    public String getLog() {
        
        return this.sLog;
        
    }
    
    /**  
    * @param sLog log
    */      
    public void setLog(String sLog) {
        
        this.sLog = sLog;
        
    }    
    
    /**********************************
              methods
    **********************************/     
    
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbTestRunLog = new StringBuilder();
        sbTestRunLog.append("ID:").append(iID); 
        sbTestRunLog.append("|TestRun:").append(testRun.getID());
        sbTestRunLog.append("|LogDate:").append(logDate);
        sbTestRunLog.append("|Result:").append(sResult);
        sbTestRunLog.append("|Log:").append(sLog);
        
        return sbTestRunLog.toString();
        
    }     
    
}
