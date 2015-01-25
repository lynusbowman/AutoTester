import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/
level = "OU"
CU = 60045904
ID = 337
attrIDs = [1098, 1097]
attrValues = ["500240733", "40"]

try {

/**********************************
           START
 **********************************/
util.log("START")    
assert util.clf.connect(env) == 1
   
/**********************************
           Initial data check
 **********************************/   
util.log("Get")      
papi = util.clf.callPAPI(level, CU, ID, "G", [attrIDs[0]], [attrValues[0]])
assert papi != null
assert util.check("Get before activation", papi.getStatus(), "") == 1

/**********************************
           Activation
 **********************************/
util.log("Activation")      
papi = util.clf.callPAPI(level, CU, ID, "a", attrIDs, attrValues)
assert papi != null
papi = util.clf.callPAPI(level, CU, ID, "G", [attrIDs[0]], [attrValues[0]])
assert papi != null
assert util.check("Get after activation", [papi.getStatus(), papi.getAttr(attrIDs[0]), papi.getAttr(attrIDs[1])],
                                          ["a", attrValues[0], attrValues[1]]) == 1                                
                     
/**********************************
           Modification
 **********************************/                                      
util.log("Modification")      
attrValues[1] = "50"    
papi = util.clf.callPAPI(level, CU, ID, "m", attrIDs, attrValues)
assert papi != null 
papi = util.clf.callPAPI(level, CU, ID, "G", [attrIDs[0]], [attrValues[0]])
assert papi != null
assert util.check("Get after modification", [papi.getStatus(), papi.getAttr(attrIDs[0]), papi.getAttr(attrIDs[1])],
                                            ["a", attrValues[0], attrValues[1]]) == 1   

/**********************************
           Deactivation
 **********************************/
util.log("Deactivation")      
papi = util.clf.callPAPI(level, CU, ID, "d", [attrIDs[0]], [attrValues[0]])
assert papi != null
papi = util.clf.callPAPI(level, CU, ID, "G", [attrIDs[0]], [attrValues[0]])
assert papi != null
assert util.check("Get after deactivation", papi.getStatus(), "") == 1    
    
/**********************************
           FINISH
 **********************************/    
assert util.clf.disconnect() == 1
util.log("FINISH")
result = 1
    
}
catch (Error e) {
    
util.err("ERROR")
util.clf.callPAPI(level, CU, ID, "d", attrIDs[0], attrValues[0])
util.clf.disconnect()
result = 0
    
}