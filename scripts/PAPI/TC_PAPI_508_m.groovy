import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/ 
DBs = ["CLF", "DWH", "AMX"]
level = "SU"
SU = util.getTestData(env)
ID = 508
attrIDs = [103, 1750]
attrValues = [24, "100%"]
srvID = 316
paramIDs = [1823]
paramValues = [attrValues[1]]
offerIDs = [500244343, 500244423]

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
papi = util.clf.callPAPI(level, SU, ID, "a")
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after activation", papi.getStatus(), "a") == 1  

/**********************************
           Modification - check via PAPI, CLF, DWH, AMX
 **********************************/    
util.log("Modification")  
papi = util.clf.callPAPI(level, SU, ID, "m", attrIDs, attrValues)
assert papi != null    
    
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
  assert util.check(DBs[2] + " offer " + offerIDs[i], offer.getStatus(), "A") == 1
        
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
