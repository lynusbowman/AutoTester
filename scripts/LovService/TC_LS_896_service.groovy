import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/
DBs = ["CLF", "DWH", "AP"]
lov = "SERVICE"
ID = 896
title = "Bez hranic na míru FUOM – CU/LE"
paramIDs = [1518, 1519, 1520, 1521, 1522, 1523, 1524, 1526, 1527, 1528, 1529, 1530, 1675, 1804, 1805, 1806]
paramTitles = ["Cena", "Mezinárodní volání podle zóny", "Mezinárodní a národní SMS", "T-Mobile roaming -  příchozí, podle zóny",
               "T-Mobile roaming – odchozí, podle zóny", "T-Mobile roaming  - SMS, podle zóny", "T-Mobile roaming – data (MB), zóna 1",
               "Profi na Míru 1", "Profi na Míru 2", "Profi na Míru 3", "Profi na Míru 4", "Profi na Míru 5", "Mobilní internet – národní data",
               "T-Mobile roaming – data (MB), zóny 1 a 2", "T-Mobile roaming – data (MB), zóny 1, 2 a 3", "Národní hovory"]
paramDataTypes = [11, 7, 7, 7, 7, 7, 0, 7, 7, 7, 7, 7, 7, 0, 0, 7]
paramRequired = [1] + [0]*15
param1675Values = ["0MB", "150MB", "400MB", "1000MB", "3000MB", "8000MB", "10000MB", "30000MB", "NMB", "1500MB"]
param1675ValueTitles = ["Ne", "150MB", "400MB", "1GB", "3GB", "8GB", "10GB", "30GB", "Neomezený", "1.5GB"]
paramPNMValues = ["A", "N"]
paramPNMValueTitles = ["ANO", "NE"]

try {

/**********************************
           START
 **********************************/
util.log("START")    
assert util.clf.connect(env) == 1
assert util.dwh.connect(env) == 1 
assert util.ap.connect(env) == 1 
    
/**********************************
           Service in CLF, DWH, AP
 **********************************/     
for (i in 0 .. 2) {
    
  util.log("Get SERVICE:" + ID + " from " + DBs[i])      
  if (i == 0) {
    srv = util.clf.getLov(lov, ID)    
  }
  else if (i == 1) {
    srv = util.dwh.getLov(lov, ID) 
  }
  else {
    srv = util.ap.getLov(lov, ID) 
  }  
  assert srv != null
  
  if (i == 0) {
    assert util.check("Service " + ID, [srv.getID(), srv.getTitle(), srv.getSuspendable(), srv.getSchedulable(), srv.getMultiple(),
                                        srv.getCU(), srv.getLE(), srv.getNetworkIndicator()],
                                       [ID, title, 0, 1, 1, 1, 1, 0]) == 1      
  }
  else {
    assert util.check("Service " + ID, [srv.getID(), srv.getTitle()], [ID, title]) == 1          
  }
        
/**********************************
           Parameters
 **********************************/         
  for (j in 0 .. paramIDs.size()-1) {  
    param = srv.getParam(paramIDs[j]) 
    
    if (i == 0) {
      assert util.check("Check param " + paramIDs[j], [param.getID(), param.getTitle(), param.getDataType(), param.getRequired()], 
                                                      [paramIDs[j], paramTitles[j], paramDataTypes[j], paramRequired[j]]) == 1
    }
    else {
      assert util.check("Check param " + paramIDs[j], [param.getID(), param.getTitle()], [paramIDs[j], paramTitles[j]]) == 1        
    }
                                              
    values = param.getValues()                  
    if (paramIDs[j] == 1675) {
      assert util.check("Values for param " + paramIDs[j], values, param1675Values, param1675ValueTitles) == 1
    }
    else if (paramIDs[j] >= 1526 && paramIDs[j] <= 1530) {
      assert util.check("Values for param " + paramIDs[j], values, paramPNMValues, paramPNMValueTitles)  == 1
    }
        
  }
  
}
    
/**********************************
           FINISH
 **********************************/    
assert util.clf.disconnect() == 1
assert util.dwh.disconnect() == 1 
assert util.ap.disconnect() == 1 
util.log("FINISH")
result = 1
    
}
catch (Error e) {
    
util.err("ERROR")
util.clf.disconnect()
util.dwh.disconnect()
util.ap.disconnect()
result = 0
    
}
