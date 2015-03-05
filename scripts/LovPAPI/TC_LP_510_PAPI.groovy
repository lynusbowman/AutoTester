import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/
lov = "PAPI"
ID = 510

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
attrIDs = [1, 5, 102, 103, 1752, 1753, 1754]
datatypes = ["Number", "Number", "Date", "Number", "String", "String", "String"]
    
for (i in 0 .. attrIDs.size()-1) {
    
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  cardinality = (attrIDs[i] == 1752 || attrIDs[i] == 1754) ? "Mandatory" : "Optional";
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()],
                                          [attrIDs[i], "IN", cardinality, datatypes[i]]) == 1
}

/**********************************
        Deactivation interface
 **********************************/
util.log("Get action d");
action = papi.getAction("d")
assert action != null
attrIDs = [1, 5, 1752]

for (i in 0 .. attrIDs.size()-1) {
    
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  cardinality = (attrIDs[i] == 1752) ? "Mandatory" : "Optional";
  datatype = (attrIDs[i] == 1752) ? "String" : "Number";
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()],
                                          [attrIDs[i], "IN", cardinality, datatype]) == 1
}

/**********************************
        Get interface
 **********************************/
util.log("Get action G");
action = papi.getAction("G")
assert action != null
attrIDs = [1755]

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
attrIDs = [1, 5, 102, 103, 1752, 1753, 1754]
datatypes = ["Number", "Number", "Date", "Number", "String", "String", "String"]
    
for (i in 0 .. attrIDs.size()-1) {
    
  attr = action.getAttr(attrIDs[i])
  assert attr != null
  cardinality = (attrIDs[i] == 1752) ? "Mandatory" : "Optional";
  assert util.check("Attr " + attrIDs[i], [attr.getID(), attr.getDirection(), attr.getCardinality(), attr.getDataType()],
                                          [attrIDs[i], "IN", cardinality, datatypes[i]]) == 1
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

