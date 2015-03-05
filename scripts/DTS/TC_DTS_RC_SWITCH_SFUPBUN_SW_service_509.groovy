import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
              Data
 **********************************/
level = "SU"
SU = util.getTestData(env)
ID = 509
DTSCode = "RC_SWITCH"
DTSParams = ["CHANNEL", "NEW_TARIFF"]
DTSParamValues = ["WEB_INT", "451"]

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
        Compatible tariff
 **********************************/
util.log("Activation")  
papi = util.clf.callPAPI(level, SU, ID, "a")
assert papi != null
papi = util.clf.callPAPI(level, SU, ID, "G")
assert papi != null
assert util.check("Get after activation", papi.getStatus(), "a") == 1  

util.log("DTS " + DTSCode)    
reason = "SMART_FUP_BUNDLE_ACTIVE_LESS_THEN_30_DAYS"
dts = util.ap.callDTS("SU", SU, DTSCode, DTSParams, DTSParamValues)    
assert dts != null
util.log("Compatible tariff")
assert dts.getAction(reason) == null 

/**********************************
        Incompatible tariff active less
 **********************************/
DTSParamValues[1] = "318"
dts = util.ap.callDTS("SU", SU, DTSCode, DTSParams, DTSParamValues)    
assert dts != null
assert util.check("Incompatible tariff less", [dts.getAction(reason)[0], dts.getAction(reason)[1], dts.getAction(reason)[2]], 
                                              [reason, "BLOCKER", "UnlimitedVideo"]) == 1  
    
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
