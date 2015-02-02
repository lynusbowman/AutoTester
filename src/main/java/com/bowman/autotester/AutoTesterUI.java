package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.Item;
import com.vaadin.ui.UI;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TabSheet;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.UserError;

import java.util.ConcurrentModificationException;

/**
* GUI
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-11-01 
*/

@Theme("autotester")
@SuppressWarnings("serial")
public class AutoTesterUI extends UI
{   
    /**********************************
              attributes
    **********************************/
   
    // logger
    private final static Logger logger = LogManager.getLogger(AutoTesterUI.class.getName());
    
    // date format
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
    // beans
    TestCaseBean testCaseBean;  
    CLFBean clfBean;
    DWHBean dwhBean;
    AMXBean amxBean;
    APBean apBean;
    
    // properties
    Properties properties;
    
    // layout
    TabSheet tabs;
    HorizontalLayout layout;
    HorizontalLayout layTestCases;    
    HorizontalLayout layTestData;
    
    // test cases    
    FormLayout frmTestCases;
    HorizontalLayout frmTestCasesHeader;
    Label lblTestCases;
    Button btnLoadTestCases;
    TextField txtTestCasesTotal;
    TextField txtTestCasesPassed;
    TextField txtTestCasesFailed;
    TextField txtTestCasesNotrun;    
    TreeTable tabTestCases;
    IndexedContainer idxTestCases;
    
    // test case group detail
    VerticalLayout layDetail;
    FormLayout frmTestCaseGroupDetail;
    Label lblTestCaseGroup;
    TextField txtTestCaseGroupID;
    TextField txtTestCaseGroupTitle;
    HorizontalLayout frmTestCaseGroupDates;
    TextField txtTestCaseGroupCreateDate;
    TextField txtTestCaseGroupModifyDate;
    TextArea txtTestCaseGroupDescription;
    TextField txtTestCaseGroupDirectory;
    HorizontalLayout frmTestCaseGroupButtons;
    Button btnCreateTestCaseGroup;
    Button btnUpdateTestCaseGroup;
    Button btnDeleteTestCaseGroup;
    
    // test case detail
    FormLayout frmTestCaseDetail;
    Label lblTestCase;
    TextField txtTestCaseID;
    TextField txtTestCaseTitle;
    TextField txtTestCaseStatus;       
    HorizontalLayout frmTestCaseDates;
    TextField txtTestCaseCreateDate;
    TextField txtTestCaseModifyDate;
    TextArea txtTestCaseDescription;
    TextField txtTestCaseFilename;    
    HorizontalLayout frmTestCaseButtons;
    CheckBox chkTestCaseAuto;     
    Button btnCreateTestCase;
    Button btnUpdateTestCase;
    Button btnDeleteTestCase;    
    
    // test runs
    VerticalLayout layRuns;
    FormLayout frmTestRuns;
    HorizontalLayout frmTestRunsHeader;
    Label lblTestRuns;
    Button btnRunTests;
    TextField txtTestRunsTotal;
    TextField txtTestRunsPassed;
    TextField txtTestRunsFailed;
    TextField txtTestRunsNotrun;    
    ListSelect lstEnvironments;
    Table tabTestRuns;
    IndexedContainer idxTestRuns;     
    
    // test case runs
    Table tabTestCaseRuns;
    IndexedContainer idxTestCaseRuns;
    
    // search criteria
    FormLayout frmSearchCriteria;
    Label lblSearchCriteria;
    ListSelect lstSearchEnvironments;
    ListSelect lstSegment;
    ListSelect lstMarket;
    ListSelect lstTariff;
    ListSelect lstService;
    CheckBox chkOwnData;
    HorizontalLayout frmSearchCriteriaButtons;
    Button btnLoadLov;
    Button btnSearchData;
    
    // found data
    FormLayout frmFoundData;
    Label lblFoundData;
    Table tabFoundData;
    IndexedContainer idxFoundData;     
    
    // data repository
    FormLayout frmDataRep;
    Label lblDataRep;
    Table tabDataRep;
    IndexedContainer idxDataRep;
    Button btnLoadData;
    
    // data detail
    FormLayout frmDataDetail;
    Label lblDataDetail;
    TextField txtDataDetailID;
    TextField txtDataDetailData;
    TextField txtDataDetailTitle;
    ListSelect lstDataDetailStatus;
    ListSelect lstDataDetailEnvironment;
    ListSelect lstDataDetailTestCase;
    CheckBox chkDataDetailReserve;
    HorizontalLayout frmTestDataButtons;
    Button btnCreateTestData;
    Button btnUpdateTestData;
    Button btnDeleteTestData;     
    
    /**********************************
              servlet
    **********************************/
    
    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = AutoTesterUI.class, widgetset = "com.bowman.autotester.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    /**********************************
              methods
    **********************************/
    
    /**
    * Lookup beans, initialize GUI
    */
    @Override
    protected void init(VaadinRequest request) {
        
        try {
            
            logger.debug("init() - start GUI initialization");            
            
            // lookup beans
            lookupBeans();
            
            // load config
            loadConfig();            
            
            // layout
            setLayout();
        
            // test cases tab
            setTestCasesTab(); 
            
            // test data tab
            setTestDataTab();
            
            // events listeners
            registerEvents();            
            
            logger.debug("init() - finish GUI initialization");
            
        }
        catch (Exception ex) {
            
            logger.error("init()", ex);
            
        } 

    }
    
    /**
    * Lookup testCaseBean
    */
    protected void lookupBeans() {
        
        try {
            
            logger.debug("lookupBeans() - java:global/AutoTester/TestCaseBean");
            
            // test case bean
            testCaseBean = null;
            Context ctx = new InitialContext();
            testCaseBean = (TestCaseBean) ctx.lookup("java:global/AutoTester/TestCaseBean");
            
            // CLF bean
            clfBean = null;
            clfBean = (CLFBean) ctx.lookup("java:global/AutoTester/CLFBean");     
            
            // DWH bean
            dwhBean = null;
            dwhBean = (DWHBean) ctx.lookup("java:global/AutoTester/DWHBean");       
            
            // AMX bean
            amxBean = null;
            amxBean = (AMXBean) ctx.lookup("java:global/AutoTester/AMXBean");   
            
            // AP bean
            apBean = null;
            apBean = (APBean) ctx.lookup("java:global/AutoTester/APBean");               
            
        }
        catch (Exception ex) {
            
            logger.error("lookupBeans()", ex);
            
        }
    }
    
    /**
    * Load configuration
    */
    protected void loadConfig() {
        
        try {
            
            logger.debug("loadConfig()");
            properties = testCaseBean.loadConfig();
            
        }
        catch (Exception ex) {
            
            logger.error("loadConfig()", ex);
            
        }
    }    
    
    /**
    * Set main horizontal layout
    */
    protected void setLayout() {
        
        try {
            
            logger.debug("setLayout()");                        
            
            layout = new HorizontalLayout();
            layout.setSizeUndefined();
            layout.setSpacing(true);
            layout.setMargin(true);
            tabs = new TabSheet();
            layout.addComponent(tabs);
            
            setContent(layout);            
            
        }
        catch (Exception ex) {
            
            logger.error("setLayout()", ex);
            
        }
    }
    
    /**
    * Set test cases tab
    */
    protected void setTestCasesTab() {
        
        try {
            
            logger.debug("setTestCasesTab()");
            
            // tab
            layTestCases = new HorizontalLayout();
            tabs.addTab(layTestCases, "Test cases");            
            
            // test cases 
            setTestCases(); 
            
            // test case group detail
            setTestCaseGroupDetail();
            
            // test case detail
            setTestCaseDetail();
            
            // test runs
            setTestRuns();
            
            // test case runs
            setTestCaseRuns();            
            
        }
        catch (Exception ex) {
            
            logger.error("setTestCasesTab()", ex);
            
        }
    }    
    
    /**
    * Set test cases form
    */
    protected void setTestCases() {
        
        try {
            
            logger.debug("setTestCases()");            
            
            // form
            frmTestCases = new FormLayout();
            layTestCases.addComponent(frmTestCases);              
            
            // label
            lblTestCases = new Label("Test cases");
            frmTestCases.addComponent(lblTestCases); 
            
           // header layout
            frmTestCasesHeader = new HorizontalLayout();
            frmTestCasesHeader.setSpacing(true);
            frmTestCases.addComponent(frmTestCasesHeader);            
            
            // load button
            btnLoadTestCases = new Button("Load");
            btnLoadTestCases.setDescription("Load test cases");
            frmTestCasesHeader.addComponent(btnLoadTestCases);              
            
            // total test cases
            txtTestCasesTotal = new TextField();
            txtTestCasesTotal.setDescription("Total");
            txtTestCasesTotal.setWidth("80px");
            frmTestCasesHeader.addComponent(txtTestCasesTotal);
            
            // passed test cases
            txtTestCasesPassed = new TextField();
            txtTestCasesPassed.setDescription("Passed");
            txtTestCasesPassed.setWidth("80px");
            frmTestCasesHeader.addComponent(txtTestCasesPassed);
            
            // failed test cases
            txtTestCasesFailed = new TextField();
            txtTestCasesFailed.setDescription("Failed");
            txtTestCasesFailed.setWidth("80px");
            frmTestCasesHeader.addComponent(txtTestCasesFailed);
            
            // not run test cases
            txtTestCasesNotrun = new TextField();
            txtTestCasesNotrun.setDescription("Not run");
            txtTestCasesNotrun.setWidth("80px");
            frmTestCasesHeader.addComponent(txtTestCasesNotrun);             

            // test case hierarchy
            idxTestCases = new IndexedContainer();
            tabTestCases = new TreeTable();            
            idxTestCases.addContainerProperty("ID", Integer.class, null);            
            idxTestCases.addContainerProperty("Group", String.class, null);
            idxTestCases.addContainerProperty("Title", String.class, null); 
            idxTestCases.addContainerProperty("Status", String.class, null);
            idxTestCases.addContainerProperty("Run", CheckBox.class, null);            
            tabTestCases.setContainerDataSource(idxTestCases);  
            tabTestCases.setImmediate(true);
            frmTestCases.addComponent(tabTestCases);                      
            
            // styling
            tabTestCases.setHeight("565px");
            tabTestCases.setWidth("520px");        
            tabTestCases.setColumnWidth("Group", 80);
            tabTestCases.setColumnWidth("Status", 60);
            tabTestCases.setColumnAlignment("Status", Table.Align.CENTER);
            tabTestCases.setColumnWidth("Run", 40);
            tabTestCases.setColumnAlignment("Run", Table.Align.CENTER);                                                
            
        }
        catch (Exception ex) {
            
            logger.error("setTestCases()", ex);
            
        }
        
    }

    /**
    * Set test case group form
    */
    protected void setTestCaseGroupDetail() {
        
        try {
            
            logger.debug("setTaseCaseGroupDetail()");

            // group and test case forms layout
            layDetail = new VerticalLayout();
            layDetail.setSpacing(true);
            layTestCases.addComponent(layDetail);
            
            // label
            lblTestCaseGroup = new Label("Test case group");
            layDetail.addComponent(lblTestCaseGroup);            
        
            // form
            frmTestCaseGroupDetail = new FormLayout();
            frmTestCaseGroupDetail.setMargin(true);
            frmTestCaseGroupDetail.setSpacing(true);
            layDetail.addComponent(frmTestCaseGroupDetail);       
        
            // ID field
            txtTestCaseGroupID = new TextField("ID");
            txtTestCaseGroupID.setVisible(false);
            frmTestCaseGroupDetail.addComponent(txtTestCaseGroupID);
        
            // title field           
            txtTestCaseGroupTitle = new TextField("Title");
            txtTestCaseGroupTitle.setWidth("250px");
            txtTestCaseGroupTitle.setRequired(true);
            frmTestCaseGroupDetail.addComponent(txtTestCaseGroupTitle);
            
            // dates layout
            frmTestCaseGroupDates = new HorizontalLayout();
            frmTestCaseGroupDetail.addComponent(frmTestCaseGroupDates);               
            
            // create date field         
            txtTestCaseGroupCreateDate = new TextField();
            txtTestCaseGroupCreateDate.setWidth("125px");
            txtTestCaseGroupCreateDate.setEnabled(false);
            txtTestCaseGroupCreateDate.setDescription("Create date");
            frmTestCaseGroupDates.addComponent(txtTestCaseGroupCreateDate);
            
            // modify date field           
            txtTestCaseGroupModifyDate = new TextField();
            txtTestCaseGroupModifyDate.setWidth("125px");
            txtTestCaseGroupModifyDate.setEnabled(false);
            txtTestCaseGroupModifyDate.setDescription("Modify date");
            frmTestCaseGroupDates.addComponent(txtTestCaseGroupModifyDate);            
        
            // description field            
            txtTestCaseGroupDescription = new TextArea("Description");
            txtTestCaseGroupDescription.setWidth("250px");
            txtTestCaseGroupDescription.setRows(3);
            txtTestCaseGroupDescription.setRequired(true);
            frmTestCaseGroupDetail.addComponent(txtTestCaseGroupDescription);  
            
            // directory field
            txtTestCaseGroupDirectory = new TextField("Directory");
            txtTestCaseGroupDirectory.setWidth("250px");
            txtTestCaseGroupDirectory.setRequired(true);
            txtTestCaseGroupDirectory.setValue(properties.getProperty("testCasesPath"));
            frmTestCaseGroupDetail.addComponent(txtTestCaseGroupDirectory); 
            
            // buttons layout
            frmTestCaseGroupButtons = new HorizontalLayout();
            frmTestCaseGroupDetail.addComponent(frmTestCaseGroupButtons);              
        
            // create button
            btnCreateTestCaseGroup = new Button("Create");
            btnCreateTestCaseGroup.setDescription("Create test case group");
            frmTestCaseGroupButtons.addComponent(btnCreateTestCaseGroup);
        
            // update button
            btnUpdateTestCaseGroup = new Button("Update");
            btnUpdateTestCaseGroup.setDescription("Update test case group");
            btnUpdateTestCaseGroup.setEnabled(false);
            frmTestCaseGroupButtons.addComponent(btnUpdateTestCaseGroup);
        
            // delete button
            btnDeleteTestCaseGroup = new Button("Delete");
            btnDeleteTestCaseGroup.setDescription("Delete test case group");
            btnDeleteTestCaseGroup.setEnabled(false);
            frmTestCaseGroupButtons.addComponent(btnDeleteTestCaseGroup);
        
        }
        catch (Exception ex) {
            
            logger.error("setTestCaseGroupDetail()", ex);
            
        }
        
    }    
    
    /**
    * Set test case form
    */
    protected void setTestCaseDetail() {
        
        try {
            
            logger.debug("setTestCaseDetail()");
        
            // label
            lblTestCase = new Label("Test case");
            layDetail.addComponent(lblTestCase);            
            
            // form
            frmTestCaseDetail = new FormLayout();
            layDetail.addComponent(frmTestCaseDetail);       
        
            // ID field
            txtTestCaseID = new TextField("ID");
            txtTestCaseID.setVisible(false);
            frmTestCaseDetail.addComponent(txtTestCaseID);        
        
            // title field
            txtTestCaseTitle = new TextField("Title");
            txtTestCaseTitle.setWidth("250px");
            txtTestCaseTitle.setRequired(true);
            frmTestCaseDetail.addComponent(txtTestCaseTitle);         
            
            // status field
            txtTestCaseStatus = new TextField("Status");
            txtTestCaseStatus.setWidth("125px");
            txtTestCaseStatus.setEnabled(false);
            frmTestCaseDetail.addComponent(txtTestCaseStatus);                      
            
            // dates layout
            frmTestCaseDates = new HorizontalLayout();
            frmTestCaseDetail.addComponent(frmTestCaseDates);
            
            // create date field
            txtTestCaseCreateDate = new TextField();
            txtTestCaseCreateDate.setWidth("125px");
            txtTestCaseCreateDate.setDescription("Create date");
            txtTestCaseCreateDate.setEnabled(false);
            frmTestCaseDates.addComponent(txtTestCaseCreateDate);
            
            // modify date field
            txtTestCaseModifyDate = new TextField();
            txtTestCaseModifyDate.setWidth("125px");
            txtTestCaseModifyDate.setDescription("Modify date");            
            txtTestCaseModifyDate.setEnabled(false);
            frmTestCaseDates.addComponent(txtTestCaseModifyDate);            
        
            // description
            txtTestCaseDescription = new TextArea("Description");
            txtTestCaseDescription.setRows(3);
            txtTestCaseDescription.setWidth("250px");
            txtTestCaseDescription.setRequired(true);            
            frmTestCaseDetail.addComponent(txtTestCaseDescription);
            
            // filename
            txtTestCaseFilename = new TextField("Filename");
            txtTestCaseFilename.setWidth("250px");
            txtTestCaseFilename.setRequired(true);
            txtTestCaseFilename.setValue(properties.getProperty("testCaseSuffix"));
            frmTestCaseDetail.addComponent(txtTestCaseFilename);                        
        
            // buttons layout
            frmTestCaseButtons = new HorizontalLayout();
            frmTestCaseDetail.addComponent(frmTestCaseButtons);     
            
            // auto
            chkTestCaseAuto = new CheckBox();
            chkTestCaseAuto.setDescription("Auto test");
            frmTestCaseButtons.addComponent(chkTestCaseAuto);             
            
            // create button
            btnCreateTestCase = new Button("Create");
            btnCreateTestCase.setDescription("Create test case");
            frmTestCaseButtons.addComponent(btnCreateTestCase);        
        
            // update button
            btnUpdateTestCase = new Button("Update");
            btnUpdateTestCase.setDescription("Update test case");
            btnUpdateTestCase.setEnabled(false);
            frmTestCaseButtons.addComponent(btnUpdateTestCase);     
        
            // delete button
            btnDeleteTestCase = new Button("Delete");
            btnDeleteTestCase.setDescription("Delete test case");
            btnDeleteTestCase.setEnabled(false);
            frmTestCaseButtons.addComponent(btnDeleteTestCase); 
            
        }
        catch (Exception ex) {
            
            logger.error("setTestCasesDetail()", ex);
            
        }
        
    }
    
    /**
    * Set test runs form
    */
    protected void setTestRuns() {
        
        try {
            
            logger.debug("setTestRuns()");
            
            // test runs and test case runs forms layout
            layRuns = new VerticalLayout();
            layRuns.setSpacing(true);
            layTestCases.addComponent(layRuns);                         
            
            // form
            frmTestRuns = new FormLayout();
            layRuns.addComponent(frmTestRuns);
            
            // label
            lblTestRuns = new Label("Test runs");
            frmTestRuns.addComponent(lblTestRuns);            
        
            // header layout 
            frmTestRunsHeader = new HorizontalLayout();
            frmTestRunsHeader.setSpacing(true);
            frmTestRuns.addComponent(frmTestRunsHeader);            
            
            // run button
            btnRunTests = new Button("Run");
            btnRunTests.setDescription("Run tests");
            frmTestRunsHeader.addComponent(btnRunTests);   
            
            // total test runs
            txtTestRunsTotal = new TextField();
            txtTestRunsTotal.setDescription("Total");
            txtTestRunsTotal.setWidth("60px");
            frmTestRunsHeader.addComponent(txtTestRunsTotal);    
            
            // passed test runs
            txtTestRunsPassed = new TextField();
            txtTestRunsPassed.setDescription("Passed");
            txtTestRunsPassed.setWidth("60px");
            frmTestRunsHeader.addComponent(txtTestRunsPassed);       
            
            // failed test runs
            txtTestRunsFailed = new TextField();
            txtTestRunsFailed.setDescription("Failed");
            txtTestRunsFailed.setWidth("60px");
            frmTestRunsHeader.addComponent(txtTestRunsFailed);       
            
            // not run test runs
            txtTestRunsNotrun = new TextField();
            txtTestRunsNotrun.setDescription("Not run");
            txtTestRunsNotrun.setWidth("60px");
            frmTestRunsHeader.addComponent(txtTestRunsNotrun);  
            
            // environment list
            lstEnvironments = new ListSelect();
            lstEnvironments.setDescription("Environment");
            lstEnvironments.addItem(properties.getProperty("test1"));
            lstEnvironments.addItem(properties.getProperty("test2"));
            lstEnvironments.setNullSelectionAllowed(false);
            lstEnvironments.setRows(1);
            lstEnvironments.setValue(properties.getProperty("test1"));
            frmTestRunsHeader.addComponent(lstEnvironments); 
            
            // test runs
            idxTestRuns = new IndexedContainer();
            tabTestRuns = new Table();   
            idxTestRuns.addContainerProperty("ID", Integer.class, null);
            idxTestRuns.addContainerProperty("Title", String.class, null); 
            idxTestRuns.addContainerProperty("Status", String.class, null);            
            tabTestRuns.setContainerDataSource(idxTestRuns);
            tabTestRuns.setImmediate(true);
            tabTestRuns.setVisibleColumns("Title", "Status");
            frmTestRuns.addComponent(tabTestRuns);
            
            // styling
            tabTestRuns.setHeight("315px");
            tabTestRuns.setWidth("440px");
            tabTestRuns.setColumnWidth("Status", 100);
            tabTestRuns.setColumnAlignment("Status", Table.Align.CENTER);            
        
        }
        catch (Exception ex) {
            
            logger.error("setTestRuns()", ex);
            
        }
        
    }
    
    /**
    * Set test case runs table
    */
    protected void setTestCaseRuns() {
        
        try {
            
            logger.debug("setTestCaseRuns()");
            
            // test runs
            idxTestCaseRuns = new IndexedContainer();
            tabTestCaseRuns = new Table();    
            idxTestCaseRuns.addContainerProperty("Run date", String.class, null);
            idxTestCaseRuns.addContainerProperty("Status", String.class, null); 
            idxTestCaseRuns.addContainerProperty("Env", String.class, null); 
            idxTestCaseRuns.addContainerProperty("Note", String.class, null);
            tabTestCaseRuns.setContainerDataSource(idxTestCaseRuns);
            tabTestCaseRuns.setImmediate(true);
            frmTestRuns.addComponent(tabTestCaseRuns);
            
            // styling
            tabTestCaseRuns.setHeight("240px");
            tabTestCaseRuns.setWidth("440px");
            tabTestCaseRuns.setColumnWidth("Run date", 145);
            tabTestCaseRuns.setColumnWidth("Status", 80);
            tabTestCaseRuns.setColumnWidth("Env", 60);
            tabTestCaseRuns.setColumnAlignment("Status", Table.Align.CENTER);
        
        }
        catch (Exception ex) {
            
            logger.error("setTestCaseRuns()", ex);
            
        }
        
    }    
    
    /**
    * Register GUI generated events
    */
    protected void registerEvents() {
        
        try {
           
            logger.debug("registerEvents()");
            
            // button LoadTestCases
            btnLoadTestCases.addClickListener(
                    new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {   
                            
                            loadTestCases();
                            
                        }                        
                    }
            );
            
            // row TestCases
            tabTestCases.addItemClickListener(
                    new ItemClickListener() {
                        @Override
                        public void itemClick(ItemClickEvent event) {
                            
                            // group clicked
                            if (event.getItem().getItemProperty("Group").getValue() != null)
                                loadTestCaseGroup((Integer) event.getItem().getItemProperty("ID").getValue());
                            // test case clicked
                            else 
                               loadTestCase((Integer) event.getItem().getItemProperty("ID").getValue()); 
                            
                        }
                    }
            );
            
            // button CreateTestCaseGroup
            btnCreateTestCaseGroup.addClickListener(
                    new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {   
                            
                            createTestCaseGroup(txtTestCaseGroupTitle.getValue(),
                                                txtTestCaseGroupDescription.getValue(),
                                                txtTestCaseGroupDirectory.getValue());
                            
                        }                        
                    }
            );  
            
            // button UpdateTestCaseGroup
            btnUpdateTestCaseGroup.addClickListener(
                    new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {   
                            
                            updateTestCaseGroup(Integer.parseInt(txtTestCaseGroupID.getValue()),
                                                txtTestCaseGroupTitle.getValue(), 
                                                txtTestCaseGroupDescription.getValue(),
                                                txtTestCaseGroupDirectory.getValue());
                            
                        }                        
                    }
            );                
            
            // button DeleteTestCaseGroup
            btnDeleteTestCaseGroup.addClickListener(
                    new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {   
                            
                            deleteTestCaseGroup(Integer.parseInt(txtTestCaseGroupID.getValue()));
                            
                        }                        
                    }
            );  
            
            // button CreateTestCase
            btnCreateTestCase.addClickListener(
                    new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {   
                            
                            createTestCase(txtTestCaseTitle.getValue(),
                                           txtTestCaseDescription.getValue(),
                                           txtTestCaseFilename.getValue(),
                                           Integer.parseInt(txtTestCaseGroupID.getValue()),
                                           chkTestCaseAuto.getValue());
                            
                        }                        
                    }
            );    
            
            // button UpdateTestCase
            btnUpdateTestCase.addClickListener(
                    new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {   
                            
                            updateTestCase(Integer.parseInt(txtTestCaseID.getValue()),
                                           txtTestCaseTitle.getValue(), 
                                           txtTestCaseDescription.getValue(),
                                           txtTestCaseFilename.getValue(),
                                           Integer.parseInt(txtTestCaseGroupID.getValue()),
                                           chkTestCaseAuto.getValue());
                            
                        }                        
                    }
            ); 
            
            // button DeleteTestCase
            btnDeleteTestCase.addClickListener(
                    new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {   
                            
                            deleteTestCase(Integer.parseInt(txtTestCaseID.getValue()));
                            
                        }                        
                    }
            );  
            
            // button RunTests
            btnRunTests.addClickListener(
                new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {   
                            
                        runTests();
                            
                    }                        
                }
            ); 
            
            // button LoadLov
            btnLoadLov.addClickListener(
                new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {   
                            
                        loadLov();
                            
                    }                        
                }
            );             
            
            // button SearchData
            btnSearchData.addClickListener(
                new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {   
                            
                        searchData();
                            
                    }                        
                }
            );   
            
            // button LoadData
            btnLoadData.addClickListener(
                new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {   
                            
                        loadData();
                            
                    }                        
                }
            );  
            
            // row DataRep
            tabDataRep.addItemClickListener(
                    new ItemClickListener() {
                        @Override
                        public void itemClick(ItemClickEvent event) {
                            
                            loadDataDetail((Integer) event.getItem().getItemProperty("ID").getValue()); 
                            
                        }
                    }
            );            
            
            // button CreateTestData
            btnCreateTestData.addClickListener(
                    new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {   
                            
                            createTestData((txtDataDetailData.getValue().isEmpty()) ? -1 : Integer.valueOf(txtDataDetailData.getValue()),
                                           txtDataDetailTitle.getValue(),
                                           Integer.valueOf(lstDataDetailStatus.getValue().toString()),
                                           lstDataDetailEnvironment.getValue().toString(),
                                           (lstDataDetailTestCase.getValue() == null) ? "" : lstDataDetailTestCase.getValue().toString(),
                                           chkDataDetailReserve.getValue());
                            
                        }                        
                    }
            );  
            
            // button UpdateTestCaseData
            btnUpdateTestData.addClickListener(
                    new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {   
                            
                            updateTestData(Integer.valueOf(txtDataDetailID.getValue()),
                                           (txtDataDetailData.getValue().isEmpty()) ? -1 : Integer.valueOf(txtDataDetailData.getValue()),
                                           txtDataDetailTitle.getValue(),
                                           Integer.valueOf(lstDataDetailStatus.getValue().toString()),
                                           lstDataDetailEnvironment.getValue().toString(),
                                           lstDataDetailTestCase.getValue().toString());
                            
                        }                        
                    }
            );                
            
            // button DeleteTestData
            btnDeleteTestData.addClickListener(
                    new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {   
                            
                            deleteTestData(Integer.valueOf(txtDataDetailID.getValue()));
                            
                        }                        
                    }
            );              
            
        }
        catch (Exception ex) {
            
            logger.error("registerEvents()", ex);
            
        }
        
    }
    
    /**
    * Load all groups and test cases via testCaseBean
    * Bean methods - getAllTestCaseGroups, getAllTestCases
    */
    protected void loadTestCases() {
        
        try {
            
            logger.debug("loadTestCases()");
            
            // reset test case table and status counters
            idxTestCases.removeAllItems();
            txtTestCasesTotal.setValue("");
            txtTestCasesPassed.setValue("");
            txtTestCasesFailed.setValue("");
            txtTestCasesNotrun.setValue("");
            
            // reset run table and status counters
            if (idxTestRuns != null) {
            
                idxTestRuns.removeAllItems();
                txtTestRunsTotal.setValue("");
                txtTestRunsPassed.setValue("");
                txtTestRunsFailed.setValue("");
                txtTestRunsNotrun.setValue("");
            
            }
                    
            // status counters
            int iTestCasesTotal = 0;
            int iTestCasesPassed = 0;
            int iTestCasesFailed = 0;
            int iTestCasesNotrun = 0;
            
            int iTestCaseGroupTotal;
            int iTestCaseGroupPassed;
            int iTestCaseGroupFailed;
            int iTestCaseGroupNotrun;
            
            // get groups and test cases
            StringBuilder sb = new StringBuilder();
            List<TestCaseGroup> lstTestCaseGroups = testCaseBean.getAllTestCaseGroups();
            sb.append(lstTestCaseGroups.size()).append(" test case groups loaded");
            logger.info(sb);
            
            sb = new StringBuilder();
            List<TestCase> lstTestCases = testCaseBean.getAllTestCases();
            iTestCasesTotal = lstTestCases.size();
            sb.append(iTestCasesTotal).append(" test cases loaded");
            logger.info(sb);
            
            Object item;
            Object parent; 
            String sStatus;
            String sGroupCounters;
            
            // fill test case groups
            logger.debug("Start to fill container");
            
            for (TestCaseGroup testCaseGroup : lstTestCaseGroups) {                               
                                
                item = idxTestCases.addItem();
                final CheckBox chkRunTestCaseGroup = new CheckBox();
                chkRunTestCaseGroup.setData(item);
                idxTestCases.getContainerProperty(item, "ID").setValue(testCaseGroup.getID());
                idxTestCases.getContainerProperty(item, "Group").setValue(testCaseGroup.getTitle());
                idxTestCases.getContainerProperty(item, "Run").setValue(chkRunTestCaseGroup);                
                parent = item; 
                
                // register checbox event
                chkRunTestCaseGroup.addValueChangeListener(
                    new ValueChangeListener() {
                        @Override
                        public void valueChange(ValueChangeEvent event) {                               
                            
                            setTestCaseGroupRun((Integer) chkRunTestCaseGroup.getData(), 
                                                (Boolean) event.getProperty().getValue());

                        }                        
                    }                        
                ); 
                
                sb = new StringBuilder();
                sb.append("loadTestCases() - test case group ").append(testCaseGroup.getTitle()).append(" added to container");
                logger.debug(sb);
                
                // reset group status counters
                iTestCaseGroupTotal = 0;
                iTestCaseGroupPassed = 0;
                iTestCaseGroupFailed = 0;
                iTestCaseGroupNotrun = 0;
                
                // fill test cases
                for (TestCase testCase : lstTestCases) {
                    
                    // test case in current group
                    if (testCase.getTestCaseGroup().getID() == testCaseGroup.getID()) {
                        
                        sStatus = testCase.getStatus();
                        item = idxTestCases.addItem();
                        final CheckBox chkRunTestCase = new CheckBox();
                        chkRunTestCase.setData(item);
                        idxTestCases.getContainerProperty(item, "ID").setValue(testCase.getID());
                        idxTestCases.getContainerProperty(item, "Title").setValue(testCase.getTitle());
                        idxTestCases.getContainerProperty(item, "Status").setValue(sStatus);
                        idxTestCases.getContainerProperty(item, "Run").setValue(chkRunTestCase);  
                        tabTestCases.setParent(item, parent);
                        tabTestCases.setChildrenAllowed(item, false);
                        
                        // set status counters
                        iTestCaseGroupTotal++;
                        
                        if (sStatus.equals("NOTRUN")) {
                            
                            iTestCasesNotrun++;
                            iTestCaseGroupNotrun++;
                            
                        }
                        else if (sStatus.equals("PASSED")) {
                            
                            iTestCasesPassed++;
                            iTestCaseGroupPassed++;
                            
                        }
                        else if (sStatus.equals("FAILED")) {
                            
                            iTestCasesFailed++;
                            iTestCaseGroupFailed++;
                            
                        }                        
                            
                        // register checbox event
                        chkRunTestCase.addValueChangeListener(
                            new ValueChangeListener() {
                                @Override
                                public void valueChange(ValueChangeEvent event) { 

                                    
                                    setTestCaseRun((Integer) chkRunTestCase.getData(), 
                                                   (Boolean) event.getProperty().getValue());
                                    
                                }                        
                            }                        
                        );                        
                        
                        sb = new StringBuilder();
                        sb.append("loadTestCases() - test case ").append(testCase.getTitle()).append(" added to container");
                        logger.debug(sb);
                    
                    }
                    
                }
                
                // fill group status counters
                if (iTestCaseGroupTotal > 0) {
                    
                    StringBuilder sbGroupCounters = new StringBuilder();                   
                    sbGroupCounters.append(iTestCaseGroupTotal).append("/");
                    sbGroupCounters.append(iTestCaseGroupPassed).append("/");
                    sbGroupCounters.append(iTestCaseGroupFailed).append("/");
                    sbGroupCounters.append(iTestCaseGroupNotrun);
                    idxTestCases.getContainerProperty(parent, "Status").setValue(sbGroupCounters.toString());
                    
                }
            }                      
            
            logger.debug("Finish to fill container");
            tabTestCases.setVisibleColumns("Group", "Title", "Status", "Run");
            
            // fill status counters
            if (iTestCasesTotal > 0) {
                                
                txtTestCasesTotal.setValue(String.valueOf(iTestCasesTotal));
                
                StringBuilder sbText = new StringBuilder();
                sbText.append(iTestCasesPassed).append("/").append(100 * iTestCasesPassed / iTestCasesTotal).append("%");
                txtTestCasesPassed.setValue(sbText.toString());
                
                sbText = new StringBuilder();
                sbText.append(iTestCasesFailed).append("/").append(100 * iTestCasesFailed / iTestCasesTotal).append("%");
                txtTestCasesFailed.setValue(sbText.toString());
                
                sbText = new StringBuilder();
                sbText.append(iTestCasesNotrun).append("/").append(100 * iTestCasesNotrun / iTestCasesTotal).append("%");
                txtTestCasesNotrun.setValue(sbText.toString());
            
            }
            
        }
        catch (Exception ex) {            
            
            logger.error("loadTestCases()", ex);
            
        }
        
    }   
    
    /**
    * Load requested test case group via testCaseBean
    * Bean methods - getTestCaseGroup
    * @param iID group PK
    */
    protected void loadTestCaseGroup(int iID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("loadTestCaseGroup() - params iID:").append(iID);
            logger.debug(sb);
            
            // get group
            TestCaseGroup testCaseGroup = testCaseBean.getTestCaseGroup(iID);
            
            if (testCaseGroup != null) {
                
                sb = new StringBuilder();
                sb.append("Test case group ").append(iID).append(" loaded");
                logger.info(sb);
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Test case group ").append(iID).append(" not found");                
                logger.error(sb);
                
            }
            
            // reset detail forms
            resetTestCaseGroupDetail();
            resetTestCaseDetail();
            idxTestCaseRuns.removeAllItems();
            
            // fill group
            txtTestCaseGroupID.setValue(String.valueOf(testCaseGroup.getID()));
            txtTestCaseGroupTitle.setValue(testCaseGroup.getTitle());
            txtTestCaseGroupCreateDate.setValue(dateFormat.format(testCaseGroup.getCreateDate()));            
            txtTestCaseGroupDescription.setValue(testCaseGroup.getDescription());
            txtTestCaseGroupDirectory.setValue(testCaseGroup.getDirectory());
            
            if (testCaseGroup.getModifyDate() != null)
                txtTestCaseGroupModifyDate.setValue(dateFormat.format(testCaseGroup.getModifyDate()));
            
            // enable update/delete
            btnUpdateTestCaseGroup.setEnabled(true);
            btnDeleteTestCaseGroup.setEnabled(true);            
           
        }
        catch (Exception ex) {
            
            logger.error("loadTestCaseGroup()", ex);
            
        }
        
    }
    
    /**
    * Load requested test case via testCaseBean
    * Bean methods - getTestCase, getTestCaseRuns
    * @param iID test case PK
    */
    protected void loadTestCase(int iID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("loadTestCase() - params iID:").append(iID);
            logger.debug(sb);
            
            // get test case
            TestCase testCase = testCaseBean.getTestCase(iID);
            
            if (testCase != null) {
                
                sb = new StringBuilder();
                sb.append("Test case ").append(iID).append(" loaded");
                logger.info(sb);
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Test case ").append(iID).append(" not found");
                logger.info(sb);
                
            }
            
            // reset detail forms
            resetTestCaseGroupDetail();
            resetTestCaseDetail();
            idxTestCaseRuns.removeAllItems();
            
            // fill test case
            txtTestCaseID.setValue(String.valueOf(testCase.getID()));
            txtTestCaseTitle.setValue(testCase.getTitle());
            txtTestCaseStatus.setValue(testCase.getStatus());
            txtTestCaseCreateDate.setValue(dateFormat.format(testCase.getCreateDate()));            
            txtTestCaseDescription.setValue(testCase.getDescription());
            txtTestCaseFilename.setValue(testCase.getFilename());
            chkTestCaseAuto.setValue(testCase.getAuto());
            
            if (testCase.getModifyDate() != null)
                txtTestCaseModifyDate.setValue(dateFormat.format(testCase.getModifyDate()));
            
            // fill group
            TestCaseGroup testCaseGroup = testCase.getTestCaseGroup();
            txtTestCaseGroupID.setValue(String.valueOf(testCaseGroup.getID()));
            txtTestCaseGroupTitle.setValue(testCaseGroup.getTitle());
            txtTestCaseGroupCreateDate.setValue(dateFormat.format(testCaseGroup.getCreateDate()));            
            txtTestCaseGroupDescription.setValue(testCaseGroup.getDescription());
            txtTestCaseGroupDirectory.setValue(testCaseGroup.getDirectory());
            
            if (testCaseGroup.getModifyDate() != null)
                txtTestCaseGroupModifyDate.setValue(dateFormat.format(testCaseGroup.getModifyDate()));
            
            // enable update/delete
            btnUpdateTestCase.setEnabled(true);
            btnDeleteTestCase.setEnabled(true);
            
            // get test case runs
            List<TestRun> lstTestRuns = testCaseBean.getTestCaseRuns(iID);
            
            // fill table
            Object item;
            
            if (lstTestRuns != null) {
                
                for (TestRun testRun : lstTestRuns) {
                    
                    item = idxTestCaseRuns.addItem();
                    idxTestCaseRuns.getContainerProperty(item, "Run date").setValue(dateFormat.format(testRun.getRunDate()));
                    idxTestCaseRuns.getContainerProperty(item, "Status").setValue(testRun.getStatus());
                    idxTestCaseRuns.getContainerProperty(item, "Env").setValue(testRun.getEnvironment());
                    idxTestCaseRuns.getContainerProperty(item, "Note").setValue(testRun.getNote());
                    
                }
                
            }
           
        }
        catch (Exception ex) {
            
            logger.error("loadTestCase()", ex);
            
        }
        
    }    
    
    /**
    * Reset test case group form
    */
    protected void resetTestCaseGroupDetail() {
        
        try {
            
            logger.debug("resetTestCaseGroupDetail()");
            
            txtTestCaseGroupID.setValue("");
            txtTestCaseGroupTitle.setValue("");
            txtTestCaseGroupCreateDate.setValue("");
            txtTestCaseGroupModifyDate.setValue("");
            txtTestCaseGroupDescription.setValue("");
            txtTestCaseGroupDirectory.setValue(properties.getProperty("testCasesPath"));
            
            txtTestCaseGroupID.setComponentError(null);
            txtTestCaseGroupTitle.setComponentError(null);
            txtTestCaseGroupDescription.setComponentError(null);
            txtTestCaseGroupDirectory.setComponentError(null);
            btnCreateTestCaseGroup.setComponentError(null);
            btnUpdateTestCaseGroup.setComponentError(null);
            btnDeleteTestCaseGroup.setComponentError(null);
            
            btnUpdateTestCaseGroup.setEnabled(false);
            btnDeleteTestCaseGroup.setEnabled(false);
            
        }
        catch (Exception ex) {
            
            logger.error("resetTestCaseGroupDetail()", ex);
            
        }
        
    }
    
    /**
    * Reset test case form  
    */
    protected void resetTestCaseDetail() {
        
        try {
            
            logger.debug("resetTestCaseDetail()");
            
            txtTestCaseID.setValue("");
            txtTestCaseTitle.setValue("");
            txtTestCaseStatus.setValue("");
            txtTestCaseCreateDate.setValue("");
            txtTestCaseModifyDate.setValue("");
            txtTestCaseDescription.setValue("");
            txtTestCaseFilename.setValue(properties.getProperty("testCaseSuffix"));
            chkTestCaseAuto.setValue(false);
            
            txtTestCaseID.setComponentError(null);
            txtTestCaseTitle.setComponentError(null);
            txtTestCaseDescription.setComponentError(null);
            txtTestCaseFilename.setComponentError(null);
            btnCreateTestCase.setComponentError(null);
            btnUpdateTestCase.setComponentError(null);
            btnDeleteTestCase.setComponentError(null);
            
            btnUpdateTestCase.setEnabled(false);
            btnDeleteTestCase.setEnabled(false);            
            
        }
        catch (Exception ex) {
           
            logger.error("resetTestCaseDetail()", ex);
            
        }
        
    }
    
    /**
    * Create requested test case group via testCaseBean
    * Bean methods - createTestCaseGroup
    * @param sTitle title
    * @param sDescription description
    * @param sDirectory directory
    */
    protected void createTestCaseGroup(String sTitle, String sDescription, String sDirectory) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("createTestCaseGroup() - params sTitle:").append(sTitle);
            sb.append(", sDescription:").append(sDescription).append(", sDirectory:").append(sDirectory);
            logger.debug(sb);
            
            // form validation
            txtTestCaseGroupTitle.setComponentError(null);
            txtTestCaseGroupDescription.setComponentError(null);
            txtTestCaseGroupDirectory.setComponentError(null);
            btnCreateTestCaseGroup.setComponentError(null);
            
            if (sTitle.isEmpty()) {
                
                txtTestCaseGroupTitle.setComponentError(new UserError("Title is mandatory"));
                return;
                
            }
            else if (sDescription.isEmpty()) {
                
                txtTestCaseGroupDescription.setComponentError(new UserError("Description is mandatory"));
                return;
                
            }   
            else if (sDirectory.isEmpty() || sDirectory.equals(properties.getProperty("testCasesPath"))) {
                
                txtTestCaseGroupDirectory.setComponentError(new UserError("Directory is mandatory"));
                return;
                
            }
            
            // create group
            TestCaseGroup testCaseGroup = testCaseBean.createTestCaseGroup(sTitle, sDescription, sDirectory);
            
            if (testCaseGroup != null) {
                
                // fill group form
                txtTestCaseGroupID.setValue(String.valueOf(testCaseGroup.getID()));
                txtTestCaseGroupCreateDate.setValue(dateFormat.format(testCaseGroup.getCreateDate()));
            
                // reload test cases
                //loadTestCases();
                
                // enable update/delete
                btnUpdateTestCaseGroup.setEnabled(true);
                btnDeleteTestCaseGroup.setEnabled(true);
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("createTestCaseGroup() - failed to create test case group ").append(sTitle);
                logger.error(sb); 
                btnCreateTestCaseGroup.setComponentError(new UserError("Failed to create test case group"));
                
            }
            
        }
        catch (Exception ex) {
            
            btnCreateTestCaseGroup.setComponentError(new UserError("Failed to create test case group"));
            logger.error("createTestCaseGroup()", ex);
            
        }
        
    }
    
    /**
    * Update requested test case group via testCaseBean
    * Bean methods - updateTestCaseGroup
    * @param iID ID
    * @param sTitle title
    * @param sDescription description 
    * @param sDirectory directory
    */
    protected void updateTestCaseGroup(int iID, String sTitle, String sDescription, String sDirectory) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("updateTestCaseGroup() - params iID:").append(iID).append(", sTitle:").append(sTitle);
            sb.append(", sDescription:").append(sDescription).append(", sDirectory:").append(sDirectory);
            logger.debug(sb);
            
            // form validation
            txtTestCaseGroupID.setComponentError(null);
            txtTestCaseGroupTitle.setComponentError(null);
            txtTestCaseGroupDescription.setComponentError(null);
            txtTestCaseGroupDirectory.setComponentError(null);
            
            if (!(iID > 0)) {
                
                txtTestCaseGroupID.setComponentError(new UserError("Group ID is missing"));
                return;
                
            }
            else if (sTitle.isEmpty()) {
                
                txtTestCaseGroupTitle.setComponentError(new UserError("Title is mandatory"));
                return;
            }
            else if (sDescription.isEmpty()) {
                
                txtTestCaseGroupDescription.setComponentError(new UserError("Description is mandatory"));
                return;
                
            }
            else if (sDirectory.isEmpty() || sDirectory.equals(properties.getProperty("testCasesPath"))) {
                
                txtTestCaseGroupDirectory.setComponentError(new UserError("Directory is mandatory"));
                
            }
            
            // update group
            boolean bUpdated = testCaseBean.updateTestCaseGroup(iID, sTitle, sDescription, sDirectory);
            
            // reload test cases
            //if (bUpdated)
            //    loadTestCases();           
            
        }             
        catch (Exception ex) {
            
            logger.error("updateTestCaseGroup()", ex);
            
        }
        
    }
    
    /**
    * Delete requested test case group via testCaseBean
    * Bean methods - deleteTestCaseGroup
    * @param iID group PK
    */
    protected void deleteTestCaseGroup(int iID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("deleteTestCaseGroup() - params iID:").append(iID);
            logger.debug(sb);
            
            // form validation
            txtTestCaseGroupID.setComponentError(null);
            
            if (!(iID > 0)) {
                
                txtTestCaseGroupID.setComponentError(new UserError("Group ID is missing"));
                return;
                
            }
            
            // delete group
            boolean bDeleted = testCaseBean.deleteTestCaseGroup(iID); 
            
            // reload test cases
            if (bDeleted) {
            
                resetTestCaseGroupDetail();
                //loadTestCases();
            
            }
            
        }
        catch (Exception ex) {
            logger.error("deleteTestCaseGroup()", ex);
        }
        
    }
    
    /**
    * Load requested test case via testCaseBean
    * Bean methods - createTestCase
    * @param sTitle title
    * @param sDescription description
    * @param sFilename filename
    * @param iGroupID group PK
    * @param bAuto auto
    */
    protected void createTestCase(String sTitle, String sDescription, String sFilename, int iGroupID, boolean bAuto) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("createTestCase() - params sTitle:").append(sTitle).append(", sDescription:").append(sDescription);
            sb.append(", sFilename:").append(sFilename).append(", iGroupID:").append(iGroupID).append(", bAuto:").append(bAuto);
            logger.debug(sb);
            
            // form validation
            txtTestCaseGroupID.setComponentError(null);
            txtTestCaseTitle.setComponentError(null);
            txtTestCaseDescription.setComponentError(null); 
            txtTestCaseFilename.setComponentError(null);
            
            if (sTitle.isEmpty()) {
                
                txtTestCaseTitle.setComponentError(new UserError("Title is mandatory"));
                return;
                
            }
            else if (sDescription.isEmpty()) {
                
                txtTestCaseDescription.setComponentError(new UserError("Description is mandatory"));
                return;
                
            }
            else if (sFilename.isEmpty() || sFilename.equals(properties.getProperty("testCaseSuffix"))) {
                
                txtTestCaseFilename.setComponentError(new UserError("Filename is mandatory"));
                return;
                
            }
            else if (!(iGroupID > 0)) {
                
                txtTestCaseGroupID.setComponentError(new UserError("Test case must be in some group"));
                return;
                
            }
            
            // create test case
            TestCase testCase = testCaseBean.createTestCase(sTitle, sDescription, sFilename, iGroupID, bAuto);
            
            if (testCase != null) {                                
                
                // fill test case form
                txtTestCaseID.setValue(String.valueOf(testCase.getID()));
                txtTestCaseStatus.setValue(testCase.getStatus());
                txtTestCaseCreateDate.setValue(dateFormat.format(testCase.getCreateDate()));
            
                // reload test cases
                //loadTestCases();
                
                // enable update/delete
                btnUpdateTestCase.setEnabled(true);
                btnDeleteTestCase.setEnabled(true);
            
            }
            else {
                
                sb = new StringBuilder();
                sb.append("createTestCase() - failed to create test case ").append(sTitle);
                logger.error(sb); 
                btnCreateTestCase.setComponentError(new UserError("Failed to create test case"));
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("createTestCase()", ex);
            
        }
        
    }
    
    /**
    * Update requested test case via testCaseBean
    * Bean methods - updateTestCase
    * @param iID test case PK
    * @param sTitle title
    * @param sDescription description
    * @param sFilename filename
    * @param iGroupID group FK
    * @param bAuto auto
    */
    protected void updateTestCase(int iID, String sTitle, String sDescription, String sFilename, int iGroupID, boolean bAuto) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("updateTestCase() - params iID:").append(iID).append(", sTitle:").append(sTitle).append(", sDescription:").append(sDescription);
            sb.append(", sFilename:").append(sFilename).append(", iGroupID:").append(iGroupID).append(", bAuto:").append(bAuto);
            logger.debug(sb);
            
            // form validation
            txtTestCaseID.setComponentError(null);
            txtTestCaseTitle.setComponentError(null);
            txtTestCaseDescription.setComponentError(null);
            txtTestCaseFilename.setComponentError(null);
            txtTestCaseGroupID.setComponentError(null);
            
            if (!(iID > 0)) {
                
                txtTestCaseID.setComponentError(new UserError("Test case ID is missing"));
                return;
                
            }
            else if (sTitle.isEmpty()) {
                
                txtTestCaseTitle.setComponentError(new UserError("Title is mandatory"));
                return;
                
            }
            else if (sDescription.isEmpty()) {
                
                txtTestCaseDescription.setComponentError(new UserError("Description is mandatory"));
                return;
                
            }
            else if (sFilename.isEmpty() || sFilename.equals(properties.getProperty("testCaseSuffix"))) {
                
                txtTestCaseFilename.setComponentError(new UserError("Filename is mandatory"));
                return;
                
            }
            else if (!(iGroupID > 0)) {
                
                txtTestCaseGroupID.setComponentError(new UserError("Test case must be is some group"));
                return;
                
            }
            
            // update test case
            boolean bUpdated = testCaseBean.updateTestCase(iID, sTitle, sDescription, sFilename, iGroupID, bAuto);
            
            // reload test cases
            //if (bUpdated)
            //    loadTestCases();
            
        }             
        catch (Exception ex) {
            
            logger.error("createTestCase()", ex);
            
        }
        
    }
    
    /**
    * Delete requested test case via testCaseBean
    * Bean methods - deleteTestCaseGroup
    * @param iID test case PK 
    */
    protected void deleteTestCase(int iID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("deleteTestCase() - params iID:").append(iID);
            logger.debug(sb);
            
            // form validation
            txtTestCaseID.setComponentError(null);
            
            if (!(iID > 0)) {
                
                txtTestCaseID.setComponentError(new UserError("Test case ID is missing"));
                return;
                
            }
            
            // delete test case
            boolean bDeleted = testCaseBean.deleteTestCase(iID); 
            
            // reload test cases
            if (bDeleted) {
                
                resetTestCaseGroupDetail();
                resetTestCaseDetail();
                idxTestCaseRuns.removeAllItems();
                //loadTestCases();
                
            }
            
        }
        catch (Exception ex) {
            logger.error("deleteTestCase()", ex);
        }
        
    }  
    
    /**
    * set chosen test case group to run
    * @param iRow row index
    * @param bChkStatus checkbox status
    */   
    protected void setTestCaseGroupRun(int iRow, boolean bChkStatus) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("setTestCaseGroupRun() - params iRow:").append(iRow).append(", bChkStatus:").append(bChkStatus);
            logger.debug(sb);
            
            
            Collection<?> lstTestCasesInGroup = tabTestCases.getChildren(iRow);
            Item testCase;
            Object item;
            boolean bFound;
                
            if (lstTestCasesInGroup == null) 
                return;
            
            // get test cases in group
            for (Iterator i = lstTestCasesInGroup.iterator(); i.hasNext(); ) {
                    
                testCase = idxTestCases.getItem(i.next());
                bFound = false;
                
                for (Iterator j = idxTestRuns.getItemIds().iterator(); j.hasNext(); ) {
                
                    item = j.next();
                    
                    if (idxTestRuns.getItem(item).getItemProperty("ID").getValue() == 
                        testCase.getItemProperty("ID").getValue()) {
                        
                        // remove test case from run
                        if (!bChkStatus) {
                            
                            idxTestRuns.removeItem(item);
                            break; 
                            
                        }
                        else {
                            
                            bFound = true;
                            break;
                            
                        }
                        
                    }                    
                
                }
                
                // add test case to run if not present
                if (bChkStatus && !bFound) {
                    
                    item = idxTestRuns.addItem();
                    idxTestRuns.getContainerProperty(item, "ID").setValue((Integer) testCase.getItemProperty("ID").getValue());
                    idxTestRuns.getContainerProperty(item, "Title").setValue(testCase.getItemProperty("Title").getValue()); 
                    
                }
                    
            }            
            
        }
        catch (ConcurrentModificationException ex) {
            
        }
        catch (Exception ex) {
            
            logger.error("setTestCaseGroupRun()", ex);
            
        }
        
    }    
    
    /**
    * Method setTestCaseRun
    * set chosen test case to run
    * @param iRow row index
    * @param bChkStatus checkbox status
    */   
    protected void setTestCaseRun(int iRow, boolean bChkStatus) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("setTestCaseRun() - params iRow:").append(iRow).append(", bChkStatus:").append(bChkStatus);
            logger.debug(sb);
            
            
            Item testCase = idxTestCases.getItem(iRow);
            Object item;
            boolean bFound = false;
            
            for (Iterator i = idxTestRuns.getItemIds().iterator(); i.hasNext(); ) {
                    
                item = i.next();
                    
                if (idxTestRuns.getItem(item).getItemProperty("ID").getValue() == 
                    testCase.getItemProperty("ID").getValue()) {
                     
                    // remove test case from run
                    if (!bChkStatus) {
                    
                        idxTestRuns.removeItem(item);
                        break;
                    
                    }
                    else {
                        
                        bFound = true;
                        break;
                        
                    }
                        
                }
                    
            }            
            
            // add test case to run
            if (bChkStatus && !bFound) {                               
                
                item = idxTestRuns.addItem();
                idxTestRuns.getContainerProperty(item, "ID").setValue((Integer) testCase.getItemProperty("ID").getValue());
                idxTestRuns.getContainerProperty(item, "Title").setValue(testCase.getItemProperty("Title").getValue());                
                
            }
            
        }
        catch (ConcurrentModificationException ex) {
            
        }
        catch (Exception ex) {
            
            logger.error("setTestCaseRun()", ex);
            
        }
        
    }
    
    /**
    * Run chosen test cases
    * Bean methods - runTestCase
    */      
    protected void runTests() {
        
        try {
            
            logger.debug("runTests()");
            logger.info("Run test cases");
            
            // reset statuses
            Object item;
            txtTestRunsTotal.setValue("");
            txtTestRunsPassed.setValue("");
            txtTestRunsFailed.setValue("");
            txtTestRunsNotrun.setValue("");
            
            for (Iterator i = idxTestRuns.getItemIds().iterator(); i.hasNext(); ) {
                
                item = i.next();
                idxTestRuns.getContainerProperty(item, "Status").setValue(null);
                
            }
            
            // run chosen test cases            
            String sStatus;
            int iTestRunsTotal = idxTestRuns.size();
            int iTestRunsPassed = 0;
            int iTestRunsFailed = 0;
            int iTestRunsNotrun = 0;
            
            for (Iterator i = idxTestRuns.getItemIds().iterator(); i.hasNext(); ) {
                
                item = i.next();
                 
                // run test case
                sStatus = testCaseBean.runTestCase((Integer) idxTestRuns.getContainerProperty(item, "ID").getValue(),
                                                    lstEnvironments.getValue().toString());
                 
                // store status
                idxTestRuns.getContainerProperty(item, "Status").setValue(sStatus);

                if (sStatus.equals("NOTRUN"))
                    iTestRunsNotrun++;
                else if (sStatus.equals("PASSED"))
                    iTestRunsPassed++;
                else if (sStatus.equals("FAILED"))
                    iTestRunsFailed++;  
                
            }
            
            // fill status counters
            if (iTestRunsTotal > 0) {
                
                txtTestRunsTotal.setValue(String.valueOf(iTestRunsTotal));
                
                StringBuilder sbText = new StringBuilder();
                sbText.append(iTestRunsPassed).append("/").append(100 * iTestRunsPassed / iTestRunsTotal).append("%");
                txtTestRunsPassed.setValue(sbText.toString());
                
                sbText = new StringBuilder();
                sbText.append(iTestRunsFailed).append("/").append(100 * iTestRunsFailed / iTestRunsTotal).append("%");
                txtTestRunsFailed.setValue(sbText.toString());
                
                sbText = new StringBuilder();
                sbText.append(iTestRunsNotrun).append("/").append(100 * iTestRunsNotrun / iTestRunsTotal).append("%");
                txtTestRunsNotrun.setValue(sbText.toString());
            
            }            
            
        }
        catch (Exception ex) {
            
            logger.error("runTests()", ex);
            
        }
        
    }   
    
    /**
    * Set test data tab
    */
    protected void setTestDataTab() {
        
        try {
            
            logger.debug("setTestDataTab()");
            
            // tab
            layTestData = new HorizontalLayout();
            tabs.addTab(layTestData, "Test data"); 
            
            // search criteria
            setSearchCriteria();
            
            // found data
            setFoundData();
            
            // data repository
            setDataRep();
            
            // deta detail
            setDataDetail();
            
        }
        catch (Exception ex) {
            
            logger.error("setTestDataTab()", ex);
            
        }
        
    }  
    
    /**
    * Set search criteria form
    */
    protected void setSearchCriteria() {
        
        try {
            
            logger.debug("setSearchCriteria()");

            // form
            frmSearchCriteria = new FormLayout();
            layTestData.addComponent(frmSearchCriteria);
            
            // label
            lblSearchCriteria = new Label("Search criteria");
            frmSearchCriteria.addComponent(lblSearchCriteria);     
            
            // environment
            lstSearchEnvironments = new ListSelect("Environment");
            lstSearchEnvironments.addItem(properties.getProperty("test1"));
            lstSearchEnvironments.addItem(properties.getProperty("test2"));
            lstSearchEnvironments.setNullSelectionAllowed(false);
            lstSearchEnvironments.setRows(1);
            lstSearchEnvironments.setValue(properties.getProperty("test1"));
            frmSearchCriteria.addComponent(lstSearchEnvironments);             
            
            // segment
            lstSegment = new ListSelect("Segment");
            lstSegment.setWidth("300px");
            lstSegment.setNullSelectionAllowed(true);
            lstSegment.setRows(1);            
            frmSearchCriteria.addComponent(lstSegment);   
            
            // market
            lstMarket = new ListSelect("Market");
            lstMarket.setWidth("300px");
            lstMarket.setNullSelectionAllowed(true);
            lstMarket.setRows(1);            
            frmSearchCriteria.addComponent(lstMarket);               
            
            // tariff
            lstTariff = new ListSelect("Tariff");
            lstTariff.setWidth("300px");
            lstTariff.setNullSelectionAllowed(true);
            lstTariff.setRows(1);            
            frmSearchCriteria.addComponent(lstTariff);    
            
            // service
            lstService = new ListSelect("Service");
            lstService.setWidth("300px");
            lstService.setNullSelectionAllowed(true);
            lstService.setRows(1);            
            frmSearchCriteria.addComponent(lstService);   
            
            // own data in reservation DB
            chkOwnData = new CheckBox("Own data");
            frmSearchCriteria.addComponent(chkOwnData);
            
            // load button
            frmSearchCriteriaButtons = new HorizontalLayout();
            frmSearchCriteria.addComponent(frmSearchCriteriaButtons);
            btnLoadLov = new Button("Load");
            btnLoadLov.setDescription("Load Lov");
            frmSearchCriteriaButtons.addComponent(btnLoadLov);  
            
            // search button
            btnSearchData = new Button("Search");
            btnSearchData.setDescription("Search test data");
            frmSearchCriteriaButtons.addComponent(btnSearchData);     
        
        }
        catch (Exception ex) {
            
            logger.error("setSearchCriteria()", ex);
            
        }
        
    }      
    
    /**
    * Set found data form
    */
    protected void setFoundData() {
        
        try {
            
            logger.debug("setFoundData()");

            // form
            frmFoundData = new FormLayout();
            layTestData.addComponent(frmFoundData);
            
            // label
            lblFoundData = new Label("Found data");
            frmFoundData.addComponent(lblFoundData);                     
            
            // data
            idxFoundData = new IndexedContainer();
            tabFoundData = new Table();   
            idxFoundData.addContainerProperty("MSISDN", String.class, null);
            idxFoundData.addContainerProperty("EXT_ID", String.class, null);
            idxFoundData.addContainerProperty("CLF", String.class, null);  
            idxFoundData.addContainerProperty("DWH", String.class, null); 
            idxFoundData.addContainerProperty("AMX", String.class, null); 
            tabFoundData.setContainerDataSource(idxFoundData);
            tabFoundData.setImmediate(true);
            frmFoundData.addComponent(tabFoundData);   
        
        }
        catch (Exception ex) {
            
            logger.error("setFoundData()", ex);
            
        }
        
    } 
    
    /**
    * Set data repository form
    */
    protected void setDataRep() {
        
        try {
            
            logger.debug("setDataRep()");

            // form
            frmDataRep = new FormLayout();
            layTestData.addComponent(frmDataRep);
            
            // label
            lblDataRep = new Label("Data repository");
            frmDataRep.addComponent(lblDataRep);     
            
            // data
            idxDataRep = new IndexedContainer();
            tabDataRep = new Table();  
            idxDataRep.addContainerProperty("ID", Integer.class, null);
            idxDataRep.addContainerProperty("Data", String.class, null);
            idxDataRep.addContainerProperty("Title", String.class, null);
            idxDataRep.addContainerProperty("Status", Integer.class, null);
            idxDataRep.addContainerProperty("Environment", String.class, null);
            tabDataRep.setContainerDataSource(idxDataRep);
            tabDataRep.setImmediate(true);
            frmDataRep.addComponent(tabDataRep);    
            
            // load button
            btnLoadData = new Button("Load");
            btnLoadData.setDescription("Load data");
            frmDataRep.addComponent(btnLoadData);     
            
            // styling   
            tabDataRep.setWidth("450px");
            tabDataRep.setColumnWidth("Title", 80);
            tabDataRep.setColumnAlignment("Status", Table.Align.CENTER);
            tabDataRep.setColumnAlignment("Environment", Table.Align.CENTER);               
        
        }
        catch (Exception ex) {
            
            logger.error("setDataRep()", ex);
            
        }
        
    }  
    
    /**
    * Set data detail form
    */
    protected void setDataDetail() {
        
        try {
            
            logger.debug("setDataDetail()");

            // form
            frmDataDetail = new FormLayout();
            layTestData.addComponent(frmDataDetail);
            
            // label
            lblDataDetail = new Label("Data detail");
            frmDataDetail.addComponent(lblDataDetail);    
            
            // ID
            txtDataDetailID = new TextField();
            txtDataDetailID.setVisible(false);
            frmDataDetail.addComponent(txtDataDetailID);                
            
            // data
            txtDataDetailData = new TextField("Data");
            txtDataDetailData.setWidth("150px");
            txtDataDetailData.setRequired(true);
            frmDataDetail.addComponent(txtDataDetailData);    
            
            // title
            txtDataDetailTitle = new TextField("Title");
            txtDataDetailTitle.setWidth("150px");
            frmDataDetail.addComponent(txtDataDetailTitle);   
            
            // status
            lstDataDetailStatus = new ListSelect("Status");
            lstDataDetailStatus.setRequired(true);
            lstDataDetailStatus.addItem(0);
            lstDataDetailStatus.addItem(1);
            lstDataDetailStatus.setNullSelectionAllowed(false);
            lstDataDetailStatus.setRows(1);
            lstDataDetailStatus.setValue(1);
            frmDataDetail.addComponent(lstDataDetailStatus);                 
            
            // environment
            lstDataDetailEnvironment = new ListSelect("Environment");
            lstDataDetailEnvironment.setRequired(true);
            lstDataDetailEnvironment.addItem(properties.getProperty("test1"));
            lstDataDetailEnvironment.addItem(properties.getProperty("test2"));
            lstDataDetailEnvironment.setNullSelectionAllowed(false);
            lstDataDetailEnvironment.setRows(1);
            lstDataDetailEnvironment.setValue(properties.getProperty("test1"));
            frmDataDetail.addComponent(lstDataDetailEnvironment);   
            
            // test case
            lstDataDetailTestCase = new ListSelect("Test case");
            lstDataDetailTestCase.setRequired(true);
            lstDataDetailTestCase.setWidth("250px");
            lstDataDetailTestCase.setNullSelectionAllowed(true);
            lstDataDetailTestCase.setRows(1);
            frmDataDetail.addComponent(lstDataDetailTestCase);  
            
            // reserve data in DB
            chkDataDetailReserve = new CheckBox("Reserve");
            frmDataDetail.addComponent(chkDataDetailReserve);            
            
            // buttons layout
            frmTestDataButtons = new HorizontalLayout();
            frmDataDetail.addComponent(frmTestDataButtons);              
        
            // create button
            btnCreateTestData = new Button("Create");
            btnCreateTestData.setDescription("Create test data");
            frmTestDataButtons.addComponent(btnCreateTestData);
        
            // update button
            btnUpdateTestData = new Button("Update");
            btnUpdateTestData.setDescription("Update test data");
            btnUpdateTestData.setEnabled(false);
            frmTestDataButtons.addComponent(btnUpdateTestData);
        
            // delete button
            btnDeleteTestData = new Button("Delete");
            btnDeleteTestData.setDescription("Delete test data");
            btnDeleteTestData.setEnabled(false);
            frmTestDataButtons.addComponent(btnDeleteTestData);            
        
        }
        catch (Exception ex) {
            
            logger.error("setDataDetail()", ex);
            
        }
        
    }   
    
    /**
    * Reset data detail form
    */
    protected void resetDataDetail() {
        
        try {
            
            logger.debug("resetDataDetail()");
            
            txtDataDetailID.setValue("");
            txtDataDetailData.setValue("");
            txtDataDetailTitle.setValue("");
            chkDataDetailReserve.setValue(false);
            
            txtDataDetailID.setComponentError(null);
            txtDataDetailData.setComponentError(null);
            btnCreateTestData.setComponentError(null);
            btnUpdateTestData.setComponentError(null);
            btnDeleteTestData.setComponentError(null);
            
            btnUpdateTestData.setEnabled(false);
            btnDeleteTestData.setEnabled(false);
            
        }
        catch (Exception ex) {
            
            logger.error("resetDataDetail()", ex);
            
        }
        
    }    
    
    /**
    * Load Lov for search criteria
    */
    protected void loadLov() {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("loadLov()");
            logger.debug(sb);                    
            
            // connect DB
            clfBean.connect(lstSearchEnvironments.getValue().toString());                  
            
            // reset lists
            lstSegment.removeAllItems();
            lstMarket.removeAllItems();
            lstTariff.removeAllItems();
            lstService.removeAllItems();
            
            // load lists
            List<String> lstLov = null;
            ListSelect lstLovUI = null;
            
            for (int i = 0; i < 4; i++) {
                
                // get Lov from CLF
                if (i == 0) {
                    
                    lstLov = clfBean.getLov("MKT_SEGMENT");
                    lstLovUI = lstSegment;
                    
                }
                else if (i == 1) {
                    
                    lstLov = clfBean.getLov("SU_MARKET");
                    lstLovUI = lstMarket;
                    
                }
                else if (i == 2) {
                    
                    lstLov = clfBean.getLov("TARIFF");
                    lstLovUI = lstTariff;
                    
                }
                else if (i == 3) {
                    
                    lstLov = clfBean.getLov("SERVICE");  
                    lstLovUI = lstService;
                    
                }
            
                // fill list
                if (lstLov != null) {
                
                    for (String sData : lstLov)
                        lstLovUI.addItem(sData);
                
                }
            
            }
            
            // disconnect DB
            clfBean.disconnect();
           
        }
        catch (Exception ex) {
            
            logger.error("loadLov()", ex);
            
        }
        
    }       
    
    /**
    * Search test data according to criteria
    */
    protected void searchData() {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("searchData()");
            logger.debug(sb);                    
            
            // segment
            String sValue;
            int iIdx;
            int iSegment = -1;
            
            if (lstSegment.getValue() != null) {
                
                sValue = lstSegment.getValue().toString();
                iIdx = sValue.indexOf(" ");
                iSegment  = Integer.valueOf(sValue.substring(0, iIdx));
                
            }
            
            // market
            int iMarket = -1;
            
            if (lstMarket.getValue() != null) {
                
                sValue = lstMarket.getValue().toString();
                iIdx = sValue.indexOf(" ");
                iMarket  = Integer.valueOf(sValue.substring(0, iIdx));
                
            }
            
            // tariff
            int iTariff = -1;
            
            if (lstTariff.getValue() != null) {
                
                sValue = lstTariff.getValue().toString();
                iIdx = sValue.indexOf(" ");
                iTariff  = Integer.valueOf(sValue.substring(0, iIdx));
                
            }
            
            // service
            int iService = -1;
            
            if (lstService.getValue() != null) {
                
                sValue = lstService.getValue().toString();
                iIdx = sValue.indexOf(" ");
                iService  = Integer.valueOf(sValue.substring(0, iIdx));
                
            }            
            
            if (iSegment == -1 && iMarket == -1 && iTariff == -1 && iService == -1)
                return;
            
            // connect DB
            String sEnvironment = lstSearchEnvironments.getValue().toString();
            clfBean.connect(sEnvironment);  
            dwhBean.connect(sEnvironment); 
            amxBean.connect(sEnvironment); 
            
            // get data
            idxFoundData.removeAllItems();
            List<String> lstData = clfBean.searchData(iSegment, iMarket, iTariff, iService, chkOwnData.getValue());
            
            // fill table            
            Object item;
            int iExtIDCLF;
            int iExtIDDB;
            StringBuilder sbStatus;
            String sStatusDB;
            String sStatusDWH;
            String sStatusAMX;
            
            if (lstData != null) {
                
                for (String sData : lstData) {
                    
                    // get CLF ExtID
                    iExtIDCLF = clfBean.getSU(sData);
                    
                    // check DWH
                    iExtIDDB = dwhBean.getSU(sData);
                    sbStatus = new StringBuilder();

                    if (iExtIDCLF == iExtIDDB) {
                        
                        sStatusDB = dwhBean.getStatus(sData);
                        
                        if (sStatusDB.equals("a"))
                            sbStatus.append("OK - ");
                        else
                            sbStatus.append("NOK - ");
                        
                        sbStatus.append(sStatusDB);
                        
                    }
                    else if (iExtIDDB == -1)
                        sbStatus.append("NOK - N/A");
                    else 
                        sbStatus.append("NOK - EXT_ID:").append(iExtIDDB);
                    
                    sStatusDWH = sbStatus.toString();
                    
                    // check AMX
                    iExtIDDB = amxBean.getSU(sData);
                    sbStatus = new StringBuilder();

                    if (iExtIDCLF == iExtIDDB) {
                        
                        sStatusDB = amxBean.getStatus(sData);
                        
                        if (sStatusDB.equals("A"))
                            sbStatus.append("OK - ");
                        else
                            sbStatus.append("NOK - ");
                        
                        sbStatus.append(sStatusDB);
                        
                    }
                    else if (iExtIDDB == -1)
                        sbStatus.append("NOK - N/A");                    
                    else
                        sbStatus.append("NOK - EXT_ID:").append(iExtIDDB);
                    
                    sStatusAMX = sbStatus.toString();
                    
                    // add record
                    item = idxFoundData.addItem();
                    idxFoundData.getContainerProperty(item, "MSISDN").setValue(sData);
                    idxFoundData.getContainerProperty(item, "EXT_ID").setValue(String.valueOf(iExtIDCLF));
                    idxFoundData.getContainerProperty(item, "CLF").setValue("OK - a");
                    idxFoundData.getContainerProperty(item, "DWH").setValue(sStatusDWH);
                    idxFoundData.getContainerProperty(item, "AMX").setValue(sStatusAMX);
                    
                }
                
            }
            
            // disconnect DB
            clfBean.disconnect();
            dwhBean.disconnect();
            amxBean.disconnect();
           
        }
        catch (Exception ex) {
            
            logger.error("searchData()", ex);
            
        }
        
    }     
    
    /**
    * Load data repository
    */
    protected void loadData() {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("loadData()");
            logger.debug(sb);                    
            
            // get data
            idxDataRep.removeAllItems();
            List<TestData> lstTestData = testCaseBean.getAllTestData();
            
            // fill table            
            Object item;
                
            for (TestData testData : lstTestData) {
                    
                // add record
                item = idxDataRep.addItem();
                idxDataRep.getContainerProperty(item, "ID").setValue(testData.getID());
                idxDataRep.getContainerProperty(item, "Data").setValue(String.valueOf(testData.getData()));
                idxDataRep.getContainerProperty(item, "Title").setValue(testData.getTitle());
                idxDataRep.getContainerProperty(item, "Status").setValue(testData.getStatus());
                idxDataRep.getContainerProperty(item, "Environment").setValue(testData.getEnvironment());
                    
            }  
            
            tabDataRep.setVisibleColumns("Data", "Title", "Status", "Environment");
            
            // get test cases
            lstDataDetailTestCase.removeAllItems();
            List<TestCase> lstTestCases = testCaseBean.getAllTestCases();
            
            // fill list
            for (TestCase testCase : lstTestCases)
                lstDataDetailTestCase.addItem(testCase.getTitle());
           
        }
        catch (Exception ex) {
            
            logger.error("loadData()", ex);
            
        }
        
    } 
    
    /**
    * Load requested test data via testCaseBean
    * Bean methods - getTestData
    * @param iID group PK
    */
    protected void loadDataDetail(int iID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("loadDataDetail() - params iID:").append(iID);
            logger.debug(sb);
            
            // get data
            TestData testData = testCaseBean.getTestData(iID);
            
            if (testData != null) {
                
                sb = new StringBuilder();
                sb.append("Test data ").append(iID).append(" loaded");
                logger.info(sb);
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Test data ").append(iID).append(" not found");                
                logger.error(sb);
                
            }
            
            // reset detail forms
            resetDataDetail();
            
            // fill form
            txtDataDetailID.setValue(String.valueOf(testData.getID()));
            txtDataDetailData.setValue(String.valueOf(testData.getData()));
            txtDataDetailTitle.setValue((testData.getTitle() == null) ? "" : testData.getTitle());            
            lstDataDetailStatus.setValue(testData.getStatus());
            lstDataDetailEnvironment.setValue(testData.getEnvironment());
            lstDataDetailTestCase.setValue(testData.getTestCase().getTitle());
            
            // enable update/delete
            btnUpdateTestData.setEnabled(true);
            btnDeleteTestData.setEnabled(true);            
           
        }
        catch (Exception ex) {
            
            logger.error("loadTestCaseGroup()", ex);
            
        }
        
    }    
    
    /**
    * Create requested test data via testCaseBean
    * Bean methods - createTestData
    * @param iData data
    * @param sTitle title
    * @param iStatus status
    * @param sEnvironment environment
    * @param sTestCase test case
    * @param bReserve reserve
    */
    protected void createTestData(int iData, String sTitle, int iStatus, String sEnvironment, String sTestCase, Boolean bReserve) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("createTestData() - params iData:").append(iData).append(", sTitle:").append(sTitle);
            sb.append(", iStatus:").append(iStatus).append(", sEnvironment:").append(sEnvironment).append(", sTestCase:").append(sTestCase);
            sb.append(", bReserve:").append(bReserve);
            logger.debug(sb);
            
            // form validation
            txtDataDetailData.setComponentError(null);
            btnCreateTestData.setComponentError(null);
            
            if (iData == -1) {
                
                txtDataDetailData.setComponentError(new UserError("Data is mandatory"));
                return;
                
            }
            else if (sTestCase.isEmpty()) {
                
                lstDataDetailTestCase.setComponentError(new UserError("Test case is mandatory"));
                return;                
                
            }
            
            // create data
            TestData testData = testCaseBean.createTestData(iData, sTitle, iStatus, sEnvironment, sTestCase);
            
            if (testData != null) {
                
                // fill ID
                txtDataDetailID.setValue(String.valueOf(testData.getID()));
                
                // enable update/delete
                btnUpdateTestData.setEnabled(true);
                btnDeleteTestData.setEnabled(true);
                
                txtDataDetailData.setComponentError(null);
                btnCreateTestData.setComponentError(null); 
                
                // reserve data
                if (bReserve) 
                    apBean.reserveData(iData);
                
                // reload data
                loadData();
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("createTestData() - failed to create data ").append(sTitle);
                logger.error(sb); 
                btnCreateTestData.setComponentError(new UserError("Failed to create test data"));
                
            }
            
        }
        catch (Exception ex) {
            
            btnCreateTestData.setComponentError(new UserError("Failed to create test data"));
            logger.error("createTestData()", ex);
            
        }
        
    }  
    
    /**
    * Update requested test ata via testCaseBean
    * Bean methods - updateTestData
    * @param iID ID
    * @param iData data
    * @param sTitle title
    * @param iStatus status
    * @param sEnvironment environment
    * @param sTestCase test case
    */
    protected void updateTestData(int iID, int iData, String sTitle, int iStatus, String sEnvironment, String sTestCase) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("updateTestData() - params iID:").append(iID).append(", iData:").append(iData).append(", sTitle:").append(sTitle);
            sb.append(", iStatus:").append(iStatus).append(", sEnvironment:").append(sEnvironment).append(", sTestCase:").append(sTestCase);
            logger.debug(sb);
            
            // form validation
            txtDataDetailID.setComponentError(null);
            txtDataDetailData.setComponentError(null);
            
            if (!(iID > 0)) {
                
                txtDataDetailID.setComponentError(new UserError("Data ID is missing"));
                return;
                
            }
            else if (iData == -1) {
                
                txtDataDetailData.setComponentError(new UserError("Data is mandatory"));
                return;
            }
            
            // update group
            boolean bUpdated = testCaseBean.updateTestData(iID, iData, sTitle, iStatus, sEnvironment, sTestCase);
            
            // reload test data
            if (bUpdated) {
                
                txtDataDetailID.setComponentError(null);
                txtDataDetailData.setComponentError(null);
                
                loadData();
                
            }          
            
        }             
        catch (Exception ex) {
            
            logger.error("updateTestData()", ex);
            
        }
        
    }  
    
    /**
    * Delete requested test data via testCaseBean
    * Bean methods - deleteTestData
    * @param iID data PK
    */
    protected void deleteTestData(int iID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("deleteTestData() - params iID:").append(iID);
            logger.debug(sb);
            
            // form validation
            txtDataDetailID.setComponentError(null);
            
            if (!(iID > 0)) {
                
                txtDataDetailID.setComponentError(new UserError("Data ID is missing"));
                return;
                
            }
            
            // delete group
            boolean bDeleted = testCaseBean.deleteTestData(iID); 
            
            // reload test data
            if (bDeleted) {
            
                resetDataDetail();
                loadData();
            
            }
            
        }
        catch (Exception ex) {
            logger.error("deleteTestData()", ex);
        }
        
    }      

}
