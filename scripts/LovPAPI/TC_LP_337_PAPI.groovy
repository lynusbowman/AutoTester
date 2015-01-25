import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
              Data
 **********************************/
lov = "PAPI"
ID = 337

try {

/**********************************
           START
 **********************************/ 
util.log("START")    
assert util.clf.connect(env) == 1

/**********************************
        Activation interface
 **********************************/
util.log("Get PAPI:" + ID + " from CLF")
papi = util.clf.getLov(lov, ID)

util.log("Get action a");
action = papi.getAction("a")
assert action != null  
attrIDs = [1, 8, 363, 1097, 1098] 
attrDataTypes = ["Number", "Date", "Date", "String", "String"]
    
for (i in 0 .. attrIDs.size()-1) {  
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  
  cardinality = (i == 4) ? "Mandatory" : "Optional"
        
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()], 
                                          [attrIDs[i], "IN", cardinality, attrDataTypes[i]]) == 1
}   

/**********************************
        Deactivation interface
 **********************************/
util.log("Get action d");
action = papi.getAction("d")
assert action != null  
attrIDs = [1, 1098] 
    
for (i in 0 .. attrIDs.size()-1) {  
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  
  cardinality = (i == 0) ? "Optional" : "Mandatory"
  datatype = (i == 0) ? "Number" : "String"
        
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()], 
                                          [attrIDs[i], "IN", cardinality, datatype]) == 1
}  

/**********************************
        Get interface
 **********************************/
util.log("Get action G");
action = papi.getAction("G")
assert action != null  
attrIDs = [1, 1097, 1098] 
attrCardinalities = ["Optional Multiple", "Optional", "Mandatory"]

for (i in 0 .. attrIDs.size()-1) {  
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  
  datatype = (i == 0) ? "Number" : "String"
  direction = (i == 0) ? "OUT" : "IN OUT" 
        
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()], 
                                          [attrIDs[i], direction, attrCardinalities[i], datatype]) == 1
}   

/**********************************
        Modification interface
 **********************************/
util.log("Get action m");
action = papi.getAction("m")
assert action != null  
attrIDs = [4, 363, 1097, 1098] 
attrDataTypes = ["Number", "Date", "String", "String"]

for (i in 0 .. attrIDs.size()-1) {  
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  
  cardinality = (i == 3) ? "Mandatory" : "Optional"
        
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()], 
                                          [attrIDs[i], "IN", cardinality, attrDataTypes[i]]) == 1
}       

/**********************************
           FINISH
 **********************************/ 
assert util.clf.disconnect() == 1
util.log("FINISH")
result = 1
    
}
catch (Error e) {
    
util.err("ERROR")
util.clf.disconnect()
result = 0
    
}