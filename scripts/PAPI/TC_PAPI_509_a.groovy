import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/ 
DBs = ["CLF", "DWH", "AMX"]
level = "SU"
SU = util.getTestData(env)
ID = 509
attrIDs = [103, 1751]
attrValues = [24, "50%"]
srvID = 317
paramIDs = [1824]
paramValues = [attrValues[1]]
offerIDs = [500244353, 500244433]
offerStats = ["A", "A"]

try {

/**********************************
           START
 **********************************/ 
util.log("START")    
assert util.clf.connect(env) == 1
assert util.dwh.connect(env) == 1
assert util.amx.connect(env) == 1
   
/**********************************
           Initial data check
 **********************************/    
util.log("Get")      
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get before activation", papi.getStatus(), "d") == 1

/**********************************
           Activation - check via PAPI, CLF, DWH, AMX
 **********************************/
util.log("Activation")  
papi = util.clf.callPAPI(level, SU, ID, "a", attrIDs, attrValues)
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null

now = new GregorianCalendar()
month = now.get(Calendar.MONTH)+1
if (month < 10) {month = "0" + month}
actDate = now.get(Calendar.YEAR) + "" + month + "" + now.get(Calendar.DATE)
assert util.check("Get after activation", [papi.getStatus(), papi.getAttr(attrIDs[1]), papi.getAttr(509)[0..7]], 
                                          ["a", attrValues[1], actDate]) == 1  

for (i in 0 .. 1) {
    
  if (i == 0) {
    srv = util.clf.getSUService(SU, srvID)  
  }
  else {
     util.pause(60)
     srv = util.dwh.getMTXService(SU, srvID) 
  }
  
  assert srv != null  
  assert util.check(DBs[i] + " service " + srvID, 
                    [srv.getStatus(), srv.getParam(paramIDs[0])],
                    ["a", paramValues[0]])
}    

for (i in 0 .. offerIDs.size()-1) {
    
  offer = util.amx.getSUOffer(SU, offerIDs[i])
  assert offer != null      
  assert util.check(DBs[2] + " offer " + offerIDs[i], offer.getStatus(), offerStats[i]) == 1
        
}    

/**********************************
           Deactivation - check via PAPI, CLF, DWH, AMX
 **********************************/
util.log("Deactivation")      
papi = util.clf.callPAPI(level, SU, ID, "d")
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after deactivation", papi.getStatus(), "d") == 1    

for (i in 0 .. 1) {
    
  if (i == 0) {
    srv = util.clf.getSUService(SU, srvID)  
  }
  else {     
     util.pause(60)
     srv = util.dwh.getMTXService(SU, srvID) 
  }
  
  assert srv != null  
  assert util.check(DBs[i] + " service " + srvID, srv.getStatus(), "d") == 1
  
} 

offerStats[0] = "C"
for (i in 0 .. offerIDs.size()-1) {
    
  offer = util.amx.getSUOffer(SU, offerIDs[i])
  assert offer != null      
  assert util.check(DBs[2] + " offer " + offerIDs[i], offer.getStatus(), offerStats[i]) == 1
        
}      
    
/**********************************
           FINISH
 **********************************/     
assert util.clf.disconnect() == 1
assert util.dwh.disconnect() == 1
assert util.amx.disconnect() == 1
util.log("FINISH")
result = 1
    
}
catch (Error e) {
    
util.err("ERROR")
util.clf.callPAPI(level, SU, ID, "d")
util.clf.disconnect()
util.dwh.disconnect()
util.amx.disconnect()
result = 0
    
}
