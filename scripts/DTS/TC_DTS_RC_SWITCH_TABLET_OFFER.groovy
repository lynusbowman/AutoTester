import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
              Data
 **********************************/
level = "SU"
SU = util.getTestData(env)
ID = 506
attrIDs = [9, 1740, 1741, 1742]
attrValues = [24, "000201412030830002", "10020", "433"]
DTSCode = "RC_SWITCH"
DTSParams = ["CHANNEL", "NEW_TARIFF"]
DTSParamValues = ["WEB_INT", "433"]

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
        Higher tariff
 **********************************/
util.log("Activation")  
papi = util.clf.callPAPI(level, SU, ID, "a", attrIDs, attrValues)
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after activation", [papi.getStatus(), papi.getAttr(attrIDs[3])],
                                          ["a", attrValues[3]]) == 1  

util.log("DTS " + DTSCode)    
reason = "DEACT_TABLET_OFFER"
dts = util.ap.callDTS("SU", SU, DTSCode, DTSParams, DTSParamValues)    
assert dts != null
util.log("Higher tariff")
assert dts.getAction(reason) == null 

/**********************************
        Lower tariff in INT channel
 **********************************/
DTSParamValues[1] = "417"
dts = util.ap.callDTS("SU", SU, DTSCode, DTSParams, DTSParamValues)    
assert dts != null
assert util.check("Lower tariff INT channel", dts.getAction(reason)[0], reason) == 1

/**********************************
        Lower tariff in SMS channel
 **********************************/
DTSParamValues[0] = "WEB_SMS"
reason = "TABLET_OFFER_ACTIVE"
dts = util.ap.callDTS("SU", SU, DTSCode, DTSParams, DTSParamValues)    
assert dts != null
assert util.check("Lower tariff SMS channel", [dts.getAction(reason)[0], dts.getAction(reason)[1]], [reason, "BLOCKER"]) == 1  
    
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
