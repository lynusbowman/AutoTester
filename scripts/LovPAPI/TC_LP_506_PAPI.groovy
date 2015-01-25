import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/
lov = "PAPI"
ID = 506

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
attrIDs = [1, 5, 9, 1740, 1741, 1742] 
    
for (i in 0 .. attrIDs.size()-1) {  
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  
  cardinality = (i <= 1) ? "Optional" : "Mandatory"
  datatype = (i == 3 || i == 4) ? "String" : "Number" 
        
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()], 
                                          [attrIDs[i], "IN", cardinality, datatype]) == 1
}   

/**********************************
        Deactivation interface
 **********************************/
util.log("Get action d");
action = papi.getAction("d")
assert action != null  
attrIDs = [1, 5] 
    
for (i in 0 .. attrIDs.size()-1) {  
  attr = action.getAttr(attrIDs[i])
  assert attr != null
        
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()], 
                                          [attrIDs[i], "IN", "Optional", "Number"]) == 1
}  

/**********************************
        Get interface
 **********************************/
util.log("Get action G");
action = papi.getAction("G")
assert action != null  
attrIDs = [1740, 1741, 1742, 1743, 1744] 

for (i in 0 .. attrIDs.size()-1) {  
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  
  datatype = (i == 2 || i == 3) ? "Number" : "String"
        
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()], 
                                          [attrIDs[i], "OUT", "Mandatory", datatype]) == 1
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