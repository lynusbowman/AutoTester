import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
              Data
 **********************************/
level = "SU"
SU = util.getTestData(env)
ID = 430
attrIDs = [1478, 1484, 1736, 1737, 1481, 1482, 1483]
attrValues = [0, "1000", "2000", "3000", "Z1=100,Z1-2=100,Z1-3=300,", "Z1=100,Z1-2=100,Z1-3=300,", "Z1=100,Z1-2=100,Z1-3=300,"]
DTSCode = "VNGROAM_D"

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
        Roaming data zone 1
 **********************************/    
util.log("Activation")  
papi = util.clf.callPAPI(level, SU, ID, "a", attrIDs, attrValues)
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after activation", [papi.getStatus(), papi.getAttr(attrIDs[1])],
                                          ["a", attrValues[1], attrValues[1]]) == 1  

util.log("DTS " + DTSCode)    
reason = "WARN_POSSIBLE_ROAMING_FUOM_DEACTIVATION"
dts = util.ap.callDTS("SU", SU, DTSCode)    
assert dts != null
assert util.check("Roaming data zone 1", dts.getAction(reason)[0], reason) == 1

/**********************************
        Roaming data zone 1-2
 **********************************/       
util.log("Modification")
attrValues[1] = "0"
papi = util.clf.callPAPI(level, SU, ID, "m", [attrIDs[1]], [attrValues[1]])
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after modification", [papi.getStatus(), papi.getAttr(attrIDs[1]), papi.getAttr(attrIDs[2])],
                                            ["a", attrValues[1], attrValues[2]]) == 1    
                                         
dts = util.ap.callDTS("SU", SU, DTSCode)    
assert dts != null
assert util.check("Roaming data zone 1-2", dts.getAction(reason)[0], reason) == 1  

/**********************************
        Roaming data zone 1-3
 **********************************/       
util.log("Modification")
attrValues[2] = "0"
papi = util.clf.callPAPI(level, SU, ID, "m", [attrIDs[2]], [attrValues[2]])
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after modification", [papi.getStatus(), papi.getAttr(attrIDs[2]), papi.getAttr(attrIDs[3])],
                                            ["a", attrValues[2], attrValues[3]]) == 1    
                                         
dts = util.ap.callDTS("SU", SU, DTSCode)    
assert dts != null
assert util.check("Roaming data zone 1-3", dts.getAction(reason)[0], reason) == 1         

/**********************************
        Roaming incoming calls
 **********************************/       
util.log("Modification")
attrValues[3] = ""
papi = util.clf.callPAPI(level, SU, ID, "m", [attrIDs[3]], [attrValues[3]])
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after modification", [papi.getStatus(), papi.getAttr(attrIDs[3]), papi.getAttr(attrIDs[4])],
                                            ["a", attrValues[3], attrValues[4]]) == 1    
                                         
dts = util.ap.callDTS("SU", SU, DTSCode)    
assert dts != null
assert util.check("Roaming incoming calls", dts.getAction(reason)[0], reason) == 1    

/**********************************
        Roaming outgoing calls
 **********************************/       
util.log("Modification")
attrValues[4] = ""
papi = util.clf.callPAPI(level, SU, ID, "m", [attrIDs[4]], [attrValues[4]])
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after modification", [papi.getStatus(), papi.getAttr(attrIDs[4]), papi.getAttr(attrIDs[5])],
                                            ["a", attrValues[4], attrValues[5]]) == 1    
                                         
dts = util.ap.callDTS("SU", SU, DTSCode)    
assert dts != null
assert util.check("Roaming outgoing calls", dts.getAction(reason)[0], reason) == 1   

/**********************************
        Roaming SMS
 **********************************/       
util.log("Modification")
attrValues[5] = ""
papi = util.clf.callPAPI(level, SU, ID, "m", [attrIDs[5]], [attrValues[5]])
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after modification", [papi.getStatus(), papi.getAttr(attrIDs[4]), papi.getAttr(attrIDs[5])],
                                            ["a", attrValues[4], attrValues[5]]) == 1    
                                         
dts = util.ap.callDTS("SU", SU, DTSCode)    
assert dts != null
assert util.check("Roaming SMS", dts.getAction(reason)[0], reason) == 1  

/**********************************
        No roaming bundle
 **********************************/       
util.log("Modification")
attrValues[6] = ""
papi = util.clf.callPAPI(level, SU, ID, "m", [attrIDs[6]], [attrValues[6]])
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after modification", [papi.getStatus(), papi.getAttr(attrIDs[4]), papi.getAttr(attrIDs[5])],
                                            ["a", attrValues[4], attrValues[5]]) == 1    
                                         
dts = util.ap.callDTS("SU", SU, DTSCode)    
assert dts != null
util.log("No roaming bundle")
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
