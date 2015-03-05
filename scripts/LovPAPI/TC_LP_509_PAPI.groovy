import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/
lov = "PAPI"
ID = 509

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
attrIDs = [1, 5, 102, 103, 1751]
datatypes = ["Number", "Number", "Date", "Number", "String"]
    
for (i in 0 .. attrIDs.size()-1) {
    
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()],
                                          [attrIDs[i], "IN", "Optional", datatypes[i]]) == 1
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
attrIDs = [509, 1751]

for (i in 0 .. attrIDs.size()-1) {
    
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()],
                                          [attrIDs[i], "OUT", "Optional", "String"]) == 1
}

/**********************************
        Modification interface
 **********************************/
util.log("Get action m");
action = papi.getAction("m")
assert action != null
attrIDs = [1, 5, 102, 103, 1751]
datatypes = ["Number", "Number", "Date", "Number", "String"]
    
for (i in 0 .. attrIDs.size()-1) {
    
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()],
                                          [attrIDs[i], "IN", "Optional", datatypes[i]]) == 1
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

