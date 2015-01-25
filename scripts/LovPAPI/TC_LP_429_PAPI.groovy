import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
              Data
 **********************************/
lov = "PAPI"
ID = 429

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
attrIDs = [1, 1465, 1466, 1467, 1468, 1469, 1470, 1471, 1473, 1474, 1475, 1476, 1477, 1624, 1733, 1734, 1735] 

for (i in 0 .. attrIDs.size()-1) {  
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  
  cardinality = (i == 1) ? "Mandatory" : "Optional"
  datatype = (i == 0) ? "Number" : "String"
        
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()], 
                                          [attrIDs[i], "IN", cardinality, datatype]) == 1
}   

/**********************************
        Deactivation interface
 **********************************/
util.log("Get action d");
action = papi.getAction("d")
assert action != null   
attr = action.getAttr(1)
assert util.check("Attr 1", [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()], 
                            [1, "IN", "Optional", "Number"]) == 1

/**********************************
        Get interface
 **********************************/                            
util.log("Get action G");
action = papi.getAction("G")
assert action != null  
attrIDs = [1, 1465, 1466, 1467, 1468, 1469, 1470, 1471, 1473, 1474, 1475, 1476, 1477, 1485, 1624, 1733, 1734, 1735] 

for (i in 0 .. attrIDs.size()-1) {  
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  
  direction = (i >= 8 && i <= 12) ? "IN OUT" : "OUT"
  datatype = (i == 0) ? "Number" : "String"
        
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()], 
                                          [attrIDs[i], direction, "Optional", datatype]) == 1
}   

/**********************************
        Modification interface
 **********************************/
util.log("Get action m");
action = papi.getAction("m")
assert action != null  
attrIDs = [1, 1465, 1466, 1467, 1468, 1469, 1470, 1471, 1473, 1474, 1475, 1476, 1477, 1624, 1733, 1734, 1735] 

for (i in 0 .. attrIDs.size()-1) {  
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  
  datatype = (i == 0) ? "Number" : "String"
        
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()], 
                                          [attrIDs[i], "IN", "Optional", datatype]) == 1
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