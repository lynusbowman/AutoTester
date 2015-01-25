import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
              Data
 **********************************/
level = "SU"
SU = 41999702
ID = 430
attrIDs = [1478, 1479, 1480]
attrValues = [0, "Z1-3=1000,", "INT=1000,CZ=1000,"]
DTSCode = "VNGINTCL_D"

try {

/**********************************
           START
 **********************************/   
util.log("START")    
assert util.clf.connect(env) == 1
assert util.ap.connect(env) == 1
   
/**********************************
        Initial data check
 **********************************/     
util.log("Get")      
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get before activation", papi.getStatus(), "d") == 1

/**********************************
        International calls
 **********************************/ 
util.log("Activation")  
papi = util.clf.callPAPI(level, SU, ID, "a", attrIDs, attrValues)
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after activation", [papi.getStatus(), papi.getAttr(attrIDs[1]), papi.getAttr(attrIDs[2])],
                                          ["a", attrValues[1], attrValues[2]]) == 1  

util.log("DTS " + DTSCode)    
reason = "WARN_POSSIBLE_INTERNATIONAL_CALLS_FUOM_DEACTIVATION"
dts = util.ap.callDTS("SU", SU, DTSCode)    
assert dts != null
assert util.check("International calls", dts.getAction(reason)[0], reason) == 1

/**********************************
        International SMS
 **********************************/ 
util.log("Modification")
attrValues[1] = ""
papi = util.clf.callPAPI(level, SU, ID, "m", [attrIDs[1]], [attrValues[1]])
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after modification", [papi.getStatus(), papi.getAttr(attrIDs[1]), papi.getAttr(attrIDs[2])],
                                            ["a", attrValues[1], attrValues[2]]) == 1    
                                         
dts = util.ap.callDTS("SU", SU, DTSCode)    
assert dts != null
assert util.check("International SMS", dts.getAction(reason)[0], reason) == 1  

/**********************************
        National SMS
 **********************************/ 
util.log("Modification")  
attrValues[2] = "CZ=1000,"
papi = util.clf.callPAPI(level, SU, ID, "m", [attrIDs[2]], [attrValues[2]])
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after modification", [papi.getStatus(), papi.getAttr(attrIDs[2])],
                                            ["a", attrValues[2]]) == 1    
                                         
dts = util.ap.callDTS("SU", SU, DTSCode)    
assert dts != null
util.log("National SMS")   
assert dts.getAction(reason) == null    
    
/**********************************
        Final data cleansing
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
assert util.ap.disconnect() == 1
util.log("FINISH")
result = 1
    
}
catch (Error e) {
    
util.err("ERROR")
util.clf.callPAPI(level, SU, ID, "d")
util.clf.disconnect()
util.ap.disconnect()
result = 0
    
}