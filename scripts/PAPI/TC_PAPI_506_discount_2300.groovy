import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/ 
DBs = ["CLF", "DWH"]
level = "SU"
SU = util.getTestData(env)
ID = 506
attrIDs = [9, 1740, 1741, 1742]
attrValues = [12, "000201412030830002", "10021", "433"]
srvID = 312
paramIDs = [1810, 1811, 1812, 1813, 1814]
paramValues = [attrValues[1], attrValues[2], "2300", attrValues[3]]

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
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get before activation", papi.getStatus(), "d") == 1

/**********************************
           Activation - check via PAPI, CLF, DWH
 **********************************/
util.log("Activation")  
papi = util.clf.callPAPI(level, SU, ID, "a", attrIDs, attrValues)
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null

now = new GregorianCalendar()
month = now.get(Calendar.MONTH)+1
if (month < 10) {month = "0" + month}
dctDate = now.get(Calendar.YEAR)+(attrValues[0]/12) + "" + month + "" + now.get(Calendar.DATE) + "000001"
assert util.check("Get after activation", [papi.getStatus(), papi.getAttr(attrIDs[1]), papi.getAttr(attrIDs[2]), 
                                           papi.getAttr(attrIDs[3]), papi.getAttr(1743), papi.getAttr(1744)],
                                          ["a", attrValues[1], attrValues[2], attrValues[3], "2300", dctDate]) == 1  

actDate = now.get(Calendar.DATE) + "." + month + "." + now.get(Calendar.YEAR)    
for (i in 0 .. 1) {
    
  if (i == 0) {
    srv = util.clf.getSUService(SU, srvID)  
  }
  else {
     util.pause(15)
     srv = util.dwh.getMTXService(SU, srvID) 
  }
  
  assert srv != null  
  assert util.check(DBs[i] + " service " + srvID, 
                    [srv.getStatus(), srv.getParam(paramIDs[0]), srv.getParam(paramIDs[1]), srv.getParam(paramIDs[2]), srv.getParam(paramIDs[3]), 
                     srv.getParam(paramIDs[4])],
                    ["a", paramValues[0], paramValues[1], paramValues[2], paramValues[3], actDate])
}                                      

/**********************************
           Deactivation - check via PAPI, CLF, DWH
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
     util.pause(15)
     srv = util.dwh.getMTXService(SU, srvID) 
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
util.clf.callPAPI(level, SU, ID, "d")
util.clf.disconnect()
util.dwh.disconnect()
result = 0
    
}
