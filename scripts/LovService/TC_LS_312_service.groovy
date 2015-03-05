import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/
DBs = ["CLF", "DWH", "AP"]
lov = "SERVICE"
ID = 312
title = "Podmíněná sleva na HW"
paramIDs = [1810, 1811, 1812, 1813, 1814]
paramTitles = ["IMEI", "Voucher type", "Sleva", "Tarif", "Datum aktivace"]
paramDataTypes = [7, 7, 0, 0, 7]
paramRequired = [1, 1, 1, 1, 0]
param1811Values = ["10020", "10021", "10022"]
param1812Values = ["1150", "2300", "3000"]
param1812ValueTitles = ["1150 Kč s DPH", "2300 Kč s DPH", "3000 Kč s DPH"]
param1813Values = ["437", "433"]
param1813ValueTitles = ["Mobilní internet 3GB", "S námi síť nesíť"]

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
    assert util.check("Service " + ID, [srv.getID(), srv.getTitle(), srv.getSuspendable(), srv.getSchedulable(), srv.getSU(), srv.getNetworkIndicator()],
                                       [ID, title, 1, 1, 1, 0]) == 1      
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
    if (j == 1) {
      assert util.check("Values for param " + paramIDs[j], values, param1811Values, ["", "", ""]) == 1
    }
    else if (j == 2) {
      assert util.check("Values for param " + paramIDs[j], values, param1812Values, param1812ValueTitles)  == 1
    }
    else if (j == 3) {
      assert util.check("Values for param " + paramIDs[j], values, param1813Values, param1813ValueTitles) == 1
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
