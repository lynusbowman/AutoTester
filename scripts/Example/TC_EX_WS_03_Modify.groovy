// initialization
import com.bowman.autotester.Utilities

result = 0
util = new Utilities(logID)

try {

// start
util.log("START")

// setup
SU = 41999702
util.ws.connect(env)

// getStatus - service active
util.log("GetStatus");
WS = util.ws.callWS("SU", SU, "MyBill", "getStatus")  
assert util.check("Status", WS.getStatus(), "a") == 1
assert util.check("notificationType", WS.getServiceParam("notificationType"), "OBE") == 1   
assert util.check("messageType", WS.getServiceParam("messageType"), "OBE") == 1 
assert util.check("emailAddress", WS.getServiceParam("emailAddress"), "lynus@seznam.cz") == 1 
assert util.check("directDebit", WS.getServiceParam("directDebit"), "A") == 1 

// validateModify
util.log("ValidateModify");
WS = util.ws.callWS("SU", SU, "MyBill", "validateModify", 
                    "service", ["notificationType", "messageType", "emailAddress", "directDebit"], ["UHRADA", "EMAIL", "lynus@centrum.cz", "N"])
assert util.check("OrderStatus", WS.getOrderStatus(), "VALIDATED") == 1

// modify
util.log("modify");
WS = util.ws.callWS("SU", SU, "MyBill", "modify", 
                    "service", ["notificationType", "messageType", "emailAddress", "directDebit"], ["UHRADA", "EMAIL", "lynus@centrum.cz", "N"])
assert util.check("OrderStatus", WS.getOrderStatus(), "COMPLETED") == 1

// getStatus - service modified
util.log("GetStatus");
WS = util.ws.callWS("SU", SU, "MyBill", "getStatus")  
assert util.check("Status", WS.getStatus(), "a") == 1 
assert util.check("notificationType", WS.getServiceParam("notificationType"), "UHRADA") == 1   
assert util.check("messageType", WS.getServiceParam("messageType"), "EMAIL") == 1 
assert util.check("emailAddress", WS.getServiceParam("emailAddress"), "lynus@centrum.cz") == 1 
assert util.check("directDebit", WS.getServiceParam("directDebit"), "N") == 1 

// teardown
util.ws.disconnect()

// finish, test passed
util.log("FINISH")
result = 1

}
catch (Error e) {

// teardown
util.ws.disconnect()

// error, test failed
util.err("ERR")
result = 0

}
