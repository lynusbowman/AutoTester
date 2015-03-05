import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
              Data
 **********************************/
level = "SU"
SU = util.getTestData(env)
IDs = [509, 508]
DTSCode = "VNGUVIDEOA"

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
papi = util.clf.callPAPI(level, SU, IDs[0], "G")
assert papi != null
assert util.check("Get before activation", papi.getStatus(), "d") == 1
papi = util.clf.callPAPI(level, SU, IDs[1], "G")
assert papi != null
assert util.check("Get before activation", papi.getStatus(), "d") == 1
    
/**********************************
        Ativation allowed
 **********************************/ 

util.log("DTS " + DTSCode)    
dts = util.ap.callDTS("SU", SU, DTSCode)    
assert dts != null
assert util.check("Activation allowed", dts.getResult(), "Y") == 1  

/**********************************
        Unlimited surfing
 **********************************/ 
util.log("Activation")  
papi = util.clf.callPAPI(level, SU, IDs[1], "a")
assert papi != null
papi = util.clf.callPAPI(level, SU, IDs[1], "G")
assert papi != null
assert util.check("Get after activation", papi.getStatus(), "a") == 1  

util.log("DTS " + DTSCode)    
reason = "UNLIMITED_SURFING_ACTIVE"
dts = util.ap.callDTS("SU", SU, DTSCode)    
assert dts != null
assert util.check("Unlimited surfing", [dts.getResult(), dts.getAction(reason)[0]], ["N", reason]) == 1    
    
/**********************************
        Final data cleansing
 **********************************/     
util.log("Deactivation")      
papi = util.clf.callPAPI(level, SU, IDs[1], "d")
assert papi != null
papi = util.clf.callPAPI(level, SU, IDs[1], "G")
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
util.clf.callPAPI(level, SU, IDs[1], "d")
util.clf.disconnect()
util.ap.disconnect()
result = 0
    
}
