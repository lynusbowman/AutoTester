import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/
lov = "PAPI"
ID = 430

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
attrIDs = [1, 1478, 1479, 1480, 1481, 1482, 1483, 1484, 1625, 1736, 1737, 1738] 

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
attrIDs = [1, 1478, 1479, 1480, 1481, 1482, 1483, 1484, 1625, 1736, 1737, 1738] 

for (i in 0 .. attrIDs.size()-1) {  
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  
  datatype = (i == 0) ? "Number" : "String"
        
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()], 
                                          [attrIDs[i], "OUT", "Optional", datatype]) == 1
}   
              
/**********************************
        Modification interface
 **********************************/              
util.log("Get action m");
action = papi.getAction("m")
assert action != null  
attrIDs = [4, 1478, 1479, 1480, 1481, 1482, 1483, 1484, 1625, 1736, 1737, 1738] 

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