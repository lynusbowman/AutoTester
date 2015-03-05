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
* Test case entity
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-11-01 
*/ 

@Entity
@Table(name = "TEST_CASE")
@SequenceGenerator(name = "TEST_CASE_SEQ", allocationSize = 1)
@NamedQueries({
    @NamedQuery(name = "getAllTestCases",
                query = "SELECT a FROM TestCase a ORDER BY a.sTitle"),
    @NamedQuery(name = "getTestCase",
                query = "SELECT a FROM TestCase a WHERE a.sTitle = :title"),
    @NamedQuery(name = "getTestCasesInGroup",
                query = "SELECT a FROM TestCase a WHERE a.testCaseGroup.iID = :group_id"),
    @NamedQuery(name = "getAutoTestCases",
                query = "SELECT a FROM TestCase a WHERE a.bAuto = true ORDER BY a.testCaseGroup, a.iID"),
    @NamedQuery(name = "getAutoTestCasesInGroup",
                query = "SELECT a FROM TestCase a WHERE a.testCaseGroup.iID = :group_id AND a.bAuto = true ORDER BY a.iID")       
})
public class TestCase {   
    
    /**********************************
              attributes
    **********************************/ 
    
    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_CASE_SEQ")
    @Column(name = "ID")
    private int iID;
    
    @Column(name = "TITLE", nullable = false, length = 100, unique = true)
    private String sTitle;
    
    @Column(name = "DESCRIPTION", nullable = false, length = 400)
    private String sDescription;
    
    @Column(name = "FILENAME", nullable = false, length = 100, unique = true)
    private String sFilename;
    
    @OneToOne
    @JoinColumn(name = "GROUP_FK", nullable = false)
    private TestCaseGroup testCaseGroup;     
    
    @Column(name = "AUTO", nullable = false)
    private boolean bAuto;    
    
    @Column(name = "STATUS", nullable = false)
    private String sStatus;
    
    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    @Column(name = "MODIFY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;
    
    @Column(name = "NOTE", length = 400)
    private String sNote;
    
    /**********************************
              constructors
    **********************************/ 
    
    public TestCase() {
        
        this.iID = -1;
        this.sTitle = null;
        this.sDescription = null;
        this.sFilename = null;
        this.testCaseGroup = null;
        this.bAuto = false;
        this.sStatus = null;
        this.createDate = null;
        this.modifyDate = null;
        this.sNote = null;
        
    }
    
    /**    
    * table TEST_CASE
    * column ID - integer, primary key, autogenerated
    * query - getAllTestCases   
    * query - getTestCase
    * query - getTestCasesInGroup
    * @param sTitle TITLE - varchar(100), not null, unique
    * @param sDescription DESCRIPTION - varchar(400), not null
    * @param sFilename FILENAME - varchar(100), not null, unique
    * @param testCaseGroup GROUP_FK - integer, foreign key to TEST_CASE_GROUP.ID, not null
    * @param bAuto AUTO - boolean, not null
    * @param sStatus STATUS - varchar, not null
    * @param createDate CREATE_DATE - date, not null
    * @param modifyDate MODIFY_DATE - date
    * @param sNote NOTE - varchar
    * */     
    public TestCase(String sTitle, String sDescription, String sFilename, TestCaseGroup testCaseGroup,
                    boolean bAuto, String sStatus, Date createDate, Date modifyDate, String sNote) {
        
        this.sTitle = sTitle;
        this.sDescription = sDescription;
        this.sFilename = sFilename;
        this.testCaseGroup = testCaseGroup;
        this.bAuto = bAuto;
        this.sStatus = sStatus;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
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
    public String getDescription() {
        
        return this.sDescription;
        
    }
    
    /** 
    * @param sDescription description
    */      
    public void setDescription(String sDescription) {
        
        this.sDescription = sDescription;
        
    }      
    
    /**   
    * @return String 
    */
    public String getFilename() {
        
        return this.sFilename;
        
    }
    
    /**  
    * @param sFilename filename
    */      
    public void setFilename(String sFilename) {
        
        this.sFilename = sFilename;
        
    }   
    
    /**  
    * @return boolean
    */
    public boolean getAuto() {
        
        return this.bAuto;
        
    }
    
    /**  
    * @param bAuto auto
    */      
    public void setAuto(boolean bAuto) {
        
        this.bAuto = bAuto;
        
    }      
    
    /**  
    * @return String 
    */
    public String getStatus() {
        
        return this.sStatus;
        
    }
    
    /**  
    * @param sStatus status
    */      
    public void setStatus(String sStatus) {
        
        this.sStatus = sStatus;
        
    }      
    
    /**  
    * @return TestCaseGroup 
    */
    public TestCaseGroup getTestCaseGroup() {
        
        return this.testCaseGroup;
        
    }
    
    /** 
    * @param testCaseGroup test case group
    */      
    public void setTestCaseGroup(TestCaseGroup testCaseGroup) {
        
        this.testCaseGroup = testCaseGroup;
        
    }         
    
    /**   
    * @return Date 
    */
    public Date getCreateDate() {
        
        return this.createDate;
        
    }
    
    /**  
    * @param createDate create date
    */      
    public void setCreateDate(Date createDate) {
        
        this.createDate = createDate;
        
    }    
    
    /**   
    * @return Date 
    */
    public Date getModifyDate() {
        
        return this.modifyDate;
        
    }
    
    /**  
    * @param modifyDate modify date
    */      
    public void setModifyDate(Date modifyDate) {
        
        this.modifyDate = modifyDate;
        
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
        
        StringBuilder sbTestCase = new StringBuilder();
        sbTestCase.append("ID:").append(iID); 
        sbTestCase.append("|Title:").append(sTitle);
        sbTestCase.append("|Description:").append(sDescription);
        sbTestCase.append("|Filename:").append(sFilename);
        sbTestCase.append("|TestCaseGroup:").append(testCaseGroup.getTitle());
        sbTestCase.append("|Auto:").append(bAuto);
        sbTestCase.append("|Status:").append(sStatus);
        sbTestCase.append("|CreateDate:").append(createDate);
        sbTestCase.append("|ModifyDate:").append(modifyDate);
        sbTestCase.append("|Note:").append(sNote);
        
        return sbTestCase.toString();
        
    }     
    
}
