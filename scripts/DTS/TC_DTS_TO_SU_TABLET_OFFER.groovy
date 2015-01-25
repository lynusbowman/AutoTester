import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
              Data
 **********************************/
level = "SU"
SU = 60059247
ID = 506
attrIDs = [9, 1740, 1741, 1742]
attrValues = [24, "000201412030830002", "10020", "433"]
DTSName = "TO_SU"

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
        Takeover blocking
 **********************************/
util.log("Activation")  
papi = util.clf.callPAPI(level, SU, ID, "a", attrIDs, attrValues)
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after activation", [papi.getStatus(), papi.getAttr(attrIDs[3])],
                                          ["a", attrValues[3]]) == 1  
util.log("DTS " + DTSName)  
dts = util.ap.callDTS("SU", SU, DTSName)    
assert dts != null
assert util.check("Result", dts.result, "0") == 1
assert util.checkLike("Warning", dts.getWarnings()[0], "na SU je aktivní Podmínìná sleva na HW")
    
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