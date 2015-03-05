import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/ 
DBs = ["CLF", "DWH", "AP"]
lov = "SERVICE"
ID = 897
title = "Bez hranic na míru FUOM – SU"
paramIDs = [1531, 1532, 1533, 1534, 1535, 1536, 1537, 1676, 1807, 1808, 1809]
paramTitles = ["Cena", "Mezinárodní volání podle zóny", "Mezinárodní a národní SMS", "T-Mobile roaming -  příchozí, podle zóny",
               "T-Mobile roaming – odchozí, podle zóny", "T-Mobile roaming  - SMS, podle zóny", "T-Mobile roaming – data (MB), zóna 1",
               "Mobilní internet – národní data", "T-Mobile roaming – data (MB), zóny 1 a 2", "T-Mobile roaming – data (MB), zóny 1, 2 a 3", \
               "Národní hovory"]
paramDataTypes = [11, 7, 7, 7, 7, 7, 0, 7, 0, 0, 7]
paramRequired = [1] + [0]*10
paramsNetwork = [0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0]
param1676Values = ["0MB", "150MB", "400MB", "1000MB", "3000MB", "8000MB", "10000MB", "30000MB", "NMB", "1500MB"]
param1676ValueTitles = ["Ne", "150MB", "400MB", "1GB", "3GB", "8GB", "10GB", "30GB", "Neomezený", "1.5GB"]
offerID = 983

try {

/**********************************
           START
 **********************************/ 
util.log("START")    
assert util.clf.connect(env) == 1
assert util.dwh.connect(env) == 1 
assert util.ap.connect(env) == 1 
assert util.amx.connect(env) == 1 
    
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
    assert util.check("Service " + ID, [srv.getID(), srv.getTitle(), srv.getSuspendable(), srv.getSchedulable(), 
                                        srv.getNetworkIndicator(), srv.getSU()],
                                       [ID, title, 0, 1, 1, 1]) == 1  
    offer = srv.getOffer(offerID)                               
    assert util.check("Offer " + offerID, [offer.getID(), offer.getTitle()], [offerID, "Parametric FUOM"]) == 1
    assert util.check("Offer params ", offer.getParams(), ["ICZ", "NC", "RC amount", "ROIZ", "ROOZ", "ROSZ", "SMS"]) == 1
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
    if (paramIDs[j] == 1676) {
      assert util.check("Values for param " + paramIDs[j], values, param1676Values, param1676ValueTitles) == 1
    }
        
  }
  
}

/**********************************
           Offers in AMX
 **********************************/ 
util.log("Get OFFER:" + offerID + " from AMX")
offer = util.amx.getLov("OFFER", offerID)
assert offer != null
assert util.check("Check AMX", [offer.getID(), offer.getTitle(), offer.getServiceLevel(), offer.getSocType()], 
                               [offerID, "FUOM Bez hranic na miru", "C", "U"]) == 1        
    
/**********************************
           FINISH
 **********************************/     
assert util.clf.disconnect() == 1
assert util.dwh.disconnect() == 1 
assert util.ap.disconnect() == 1 
assert util.amx.disconnect() == 1
util.log("FINISH")
result = 1
    
}
catch (Error e) {
    
util.err("ERROR")
util.clf.disconnect()
util.dwh.disconnect()
util.ap.disconnect()
util.amx.disconnect()
result = 0
    
}
