import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/ 
DBs = ["CLF", "DWH"]
level = "SU"
SU = util.getTestData(env)
ID = 430
attrIDs = [1478, 1479, 1480, 1481, 1482, 1483, 1484, 1625, 1736, 1737, 1738]
attrValues = ["25000", "Z1=100,Z1-3=200,Z1-9=300,", "INT=100,CZ=200,", "Z1=100,Z1-2=200,Z1-3=300,", "Z1=100,Z1-2=200,Z1-3=300,",
              "Z1=100,Z1-2=200,Z1-3=300,", "1000", "8000MB", "2000", "3000", "CZ=100,"]
srvID = 897
paramIDs = [1531, 1532, 1533, 1534, 1535, 1536, 1537, 1676, 1807, 1808, 1809]
paramValues = attrValues
offerID = 983

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
assert util.check("Get after activation", papi.getOutAttributes(), attrIDs, attrValues) == 1                                            

for (i in 0 .. 1) {
    
  if (i == 0) {
    srv = util.clf.getSUService(SU, srvID)  
  }
  else {
     util.pause(120)
     srv = util.dwh.getMTXService(SU, srvID) 
  }
  
  assert srv != null  
  assert util.check(DBs[i] + " service " + srvID, srv.getParameters(), paramIDs, paramValues) == 1
} 

offer = util.amx.getSUOffer(SU, offerID)
assert offer != null
assert util.check("Offer", offer.getStatus(), "A")

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
     util.pause(120)
     srv = util.dwh.getMTXService(SU, srvID) 
  }
  
  assert srv != null  
  assert util.check(DBs[i] + " service " + srvID, srv.getStatus(), "d")
}      

offer = util.amx.getSUOffer(SU, offerID)
assert offer != null
assert util.check("Offer", offer.getStatus(), "C")    
    
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
