package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

/**
* Test data
* 
* @author  Petr Rasek
* @version 1.0
* @since   2015-01-24 
*/ 

@Entity
@Table(name = "TEST_DATA")
@SequenceGenerator(name = "TEST_DATA_SEQ", allocationSize = 1)
@NamedQueries({
    @NamedQuery(name = "getTestData",
                query = "SELECT a FROM TestData a WHERE a.testCase.iID = :test_case_id " +
                        "AND a.sEnvironment = :environment AND a.iStatus = 1"),   
})
public class TestData {   
    
    /**********************************
              attributes
    **********************************/ 
    
    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_DATA_SEQ")
    @Column(name = "ID")
    private int iID;   
    
    @Column(name = "DATA_ID", nullable = false)
    private int iData;  
    
    @Column(name = "TITLE", length = 40)
    private String sTitle;       
    
    @Column(name = "ENVIRONMENT", nullable = false, length = 10)
    private String sEnvironment;  
    
    @Column(name = "STATUS")
    private int iStatus;      
    
    @OneToOne
    @JoinColumn(name = "TEST_CASE_FK", nullable = false)
    private TestCase testCase;      
    
    @Column(name = "NOTE", length = 400)
    private String sNote;
    
    /**********************************
              constructors
    **********************************/ 
    
    public TestData() {
        
        this.iID = -1;
        this.iData = -1;
        this.sTitle = null;
        this.sEnvironment = null;
        this.iStatus = -1;
        this.testCase = null;
        this.sNote = null;
        
    }
    
    /**    
    * table TEST_DATA
    * column ID - integer, primary key, autogenerated
    * query - getTestData
    * @param iData DATA_ID - integer, not null
    * @param sTitle TITLE - varchar
    * @param sEnvironment ENVIRONMENT - varchar, not null
    * @param iStatus STATUS - integer
    * @param testCase TEST_CASE_FK - integer, foreign key to TEST_CASE.ID, not null
    * @param sNote NOTE - varchar
    * */     
    public TestData(int iData, String sTitle, String sEnvironment, int iStatus, TestCase testCase, String sNote) {
        
        this.iData = iData;
        this.sTitle = sTitle;
        this.sEnvironment = sEnvironment;
        this.iStatus = iStatus;
        this.testCase = testCase;
        this.sNote = sNote;
        
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
    * @return int 
    */
    public int getData() {
        
        return this.iData;
        
    }
    
    /** 
    * @param iData data
    */      
    public void setData(int iData) {
        
        this.iData = iData;
        
    }
    
    /**  
    * @return String 
    */
    public String getTitle() {
        
        return this.sTitle;
        
    }
    
    /** 
    * @param sTitle title
    */      
    public void setTitle(String sTitle) {
        
        this.sTitle = sTitle;
        
    }    
    
    /**  
    * @return String 
    */
    public String getEnvironment() {
        
        return this.sEnvironment;
        
    }
    
    /** 
    * @param sEnvironment environment
    */      
    public void setEnvironment(String sEnvironment) {
        
        this.sEnvironment = sEnvironment;
        
    }  
    
    /**  
    * @return int 
    */
    public int getStatus() {
        
        return this.iStatus;
        
    }
    
    /** 
    * @param iStatus status
    */      
    public void setStatus(int iStatus) {
        
        this.iStatus = iStatus;
        
    }    
    
    /**  
    * @return TestCase
    */
    public TestCase getTestCase() {
        
        return this.testCase;
        
    }
    
    /**  
    * @param testCase test case
    */      
    public void setTestCase(TestCase testCase) {
        
        this.testCase = testCase;
        
    }    
    
    /**   
    * @return String 
    */
    public String getNote() {
        
        return this.sNote;
        
    }
    
    /**  
    * @param sNote note
    */      
    public void setNote(String sNote) {
        
        this.sNote = sNote;
        
    }    
    
    /**********************************
              methods
    **********************************/     
    
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbTestRun = new StringBuilder();
        sbTestRun.append("ID:").append(iID); 
        sbTestRun.append("|Data:").append(iData);
        sbTestRun.append("|Title:").append(sTitle);
        sbTestRun.append("|Environment:").append(sEnvironment);
        sbTestRun.append("|Status:").append(iStatus);
        sbTestRun.append("|TestCase:").append(testCase.getTitle());
        sbTestRun.append("|Note:").append(sNote);
        
        return sbTestRun.toString();
        
    }     
    
}
