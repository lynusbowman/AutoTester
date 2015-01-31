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

// validateDeactivate
util.log("ValidateDeactivate");
WS = util.ws.callWS("SU", SU, "MyBill", "validateDeactivate", 
                    "action", ["reason_status"], ["22"])
assert util.check("OrderStatus", WS.getOrderStatus(), "VALIDATED") == 1

// modify
util.log("deactivate");
WS = util.ws.callWS("SU", SU, "MyBill", "deactivate", 
                    "action", ["reason_status"], ["22"])
assert util.check("OrderStatus", WS.getOrderStatus(), "COMPLETED") == 1

// getStatus - service deactivated
util.log("GetStatus");
WS = util.ws.callWS("SU", SU, "MyBill", "getStatus")  
assert util.check("Status", WS.getStatus(), "d") == 1 

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
