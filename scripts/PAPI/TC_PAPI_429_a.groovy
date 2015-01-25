import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/
DBs = ["CLF", "DWH"]
level = "OU"
CU = 60045904
ID = 429
attrIDs = [1465, 1466, 1467, 1468, 1469, 1470, 1471, 1473, 1474, 1475, 1476, 1477, 1624, 1733, 1734, 1735]
attrValues = ["25000", "Z1=100,Z1-3=200,Z1-9=300,", "INT=100,CZ=200,", "Z1=100,Z1-2=200,Z1-3=300,", "Z1=100,Z1-2=200,Z1-3=300,",
              "Z1=100,Z1-2=200,Z1-3=300,", "1000", "A", "N", "A", "N", "A", "8000MB", "2000", "3000", "CZ=100,"]
srvID = 896
paramIDs = [1518, 1519, 1520, 1521, 1522, 1523, 1524, 1526, 1527, 1528, 1529, 1530, 1675, 1804, 1805, 1806]
paramValues = attrValues

try {

/**********************************
           START
 **********************************/
util.log("START")    
assert util.clf.connect(env) == 1
assert util.dwh.connect(env) == 1
   
/**********************************
           Initial data check
 **********************************/   
util.log("Get")      
papi = util.clf.callPAPI(level, CU, ID, "G")
assert papi != null
assert util.check("Get before activation", papi.getStatus(), "d") == 1

/**********************************
           Activation - check via PAPI, CLF, DWH
 **********************************/
util.log("Activation")  
papi = util.clf.callPAPI(level, CU, ID, "a", attrIDs, attrValues)
assert papi != null
papi = util.clf.callPAPI(level, CU, ID, "G", [attrIDs[7]], [attrValues[7]])
assert papi != null
assert util.check("Get after activation PNM1", papi.getOutAttributes(), attrIDs, 
                                               attrValues[0..7] + [""]*4 + attrValues[12..15]) == 1 
                                           
papi = util.clf.callPAPI(level, CU, ID, "G", [attrIDs[8]], ["A"])
assert papi != null    
assert util.check("Get after activation PNM2", papi.getStatus(), "d") == 1   

papi = util.clf.callPAPI(level, CU, ID, "G")
assert papi != null
attr1485Value = "<param_fuom_instances><param_fuom_instance><reason_status>1</reason_status><price>" + attrValues[0] +"</price>" +
                "<International_Calls_By_Zones>" + attrValues[1] + "</International_Calls_By_Zones><International_Sms>" +
                attrValues[2] + "</International_Sms><Roaming_Incoming>" + attrValues[3] + "</Roaming_Incoming>" +
                "<Roaming_Outgoing>" + attrValues[4] + "</Roaming_Outgoing><Roaming_Sms>" + attrValues[5] + "</Roaming_Sms>" +
                "<Roaming_Data>" + attrValues[6] +"</Roaming_Data><pnm1>" + attrValues[7] + "</pnm1><pnm2>" + attrValues[8] +
                "</pnm2><pnm3>" + attrValues[9] + "</pnm3><pnm4>" + attrValues[10] + "</pnm4><pnm5>" + attrValues[11] + "</pnm5>" +
                "<national_data>" + attrValues[12] + "</national_data><roaming_data_zones12>" + attrValues[13] +
                "</roaming_data_zones12><roaming_data_zones123>" + attrValues[14] + "</roaming_data_zones123><national_calls>" +
                attrValues[15] + "</national_calls></param_fuom_instance></param_fuom_instances>"
assert util.check("Get after activation all", [papi.getStatus(), papi.getAttr(1485)],
                                              ["a", attr1485Value])

for (i in 0 .. 1) {
    
  if (i == 0) {
    srv = util.clf.getOUService(CU, srvID)  
  }
  else {
     util.pause(30)
     srv = util.dwh.getMTXService(CU, srvID) 
  }
  
  assert srv != null  
  assert util.check(DBs[i] + " service " + srvID, srv.getParameters(), paramIDs, paramValues) == 1
}                                    

/**********************************
           Deactivation - check via PAPI, CLF, DWH
 **********************************/
util.log("Deactivation")      
papi = util.clf.callPAPI(level, CU, ID, "d")
assert papi != null
papi = util.clf.callPAPI(level, CU, ID, "G")
assert papi != null
assert util.check("Get after deactivation", papi.getStatus(), "d") == 1    

for (i in 0 .. 1) {
    
  if (i == 0) {
    srv = util.clf.getOUService(CU, srvID)  
  }
  else {
     util.pause(30)
     srv = util.dwh.getMTXService(CU, srvID) 
  }
  
  assert srv != null  
  assert util.check(DBs[i] + " service " + srvID, srv.getStatus(), "d")
}      
    
/**********************************
           FINISH
 **********************************/    
assert util.clf.disconnect() == 1
assert util.dwh.disconnect() == 1
util.log("FINISH")
result = 1
    
}
catch (Error e) {
    
util.err("ERROR")
util.clf.callPAPI(level, CU, ID, "d")
util.clf.disconnect()
util.dwh.disconnect()
result = 0
    
}