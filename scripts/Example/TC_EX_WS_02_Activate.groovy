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

// getStatus - service deactive
util.log("GetStatus");
WS = util.ws.callWS("SU", SU, "MyBill", "getStatus")  
assert util.check("Status", WS.getStatus(), "d") == 1

// validateActivate
util.log("ValidateActivate");
WS = util.ws.callWS("SU", SU, "MyBill", "validateActivate", 
                    "service", ["notificationType", "messageType", "emailAddress", "directDebit"], ["OBE", "OBE", "lynus@seznam.cz", "A"], 
                    "action", ["reason_status"], [21])
assert util.check("OrderStatus", WS.getOrderStatus(), "VALIDATED") == 1

// activate
util.log("activate");
WS = util.ws.callWS("SU", SU, "MyBill", "activate", 
                    "service", ["notificationType", "messageType", "emailAddress", "directDebit"], ["OBE", "OBE", "lynus@seznam.cz", "A"], 
                    "action", ["reason_status"], [21])
assert util.check("OrderStatus", WS.getOrderStatus(), "COMPLETED") == 1

// getStatus - service active
util.log("GetStatus");
WS = util.ws.callWS("SU", SU, "MyBill", "getStatus")  
assert util.check("Status", WS.getStatus(), "a") == 1 
assert util.check("notificationType", WS.getServiceParam("notificationType"), "OBE") == 1   
assert util.check("messageType", WS.getServiceParam("messageType"), "OBE") == 1 
assert util.check("emailAddress", WS.getServiceParam("emailAddress"), "lynus@seznam.cz") == 1 
assert util.check("directDebit", WS.getServiceParam("directDebit"), "A") == 1 
assert util.check("reason_status", WS.getActionParam("reason_status"), "21") == 1 

// teardown
util.ws.disconnect()

// finish, test passed
util.log("FINISH")
result = 1

}
catch (AssertionError e) {

// teardown
util.ws.disconnect()

// error, test failed
util.err("ERR")
result = 0

}
