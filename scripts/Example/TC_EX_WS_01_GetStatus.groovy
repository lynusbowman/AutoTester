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

// MissedCallsRegister - service deactive
WS = util.ws.callWS("SU", SU, "MissedCallsRegister", "getStatus")  
assert util.check("Status", WS.getStatus(), "d") == 1
assert util.checkNok("CorrelationId", WS.getCorrelationId(), "") == 1

// allowed action - MissedCallsRegister.ACTIVATE
util.log("AA MissedCallsRegister.ACTIVATE")
AA = WS.getAllowedAction("MissedCallsRegister.ACTIVATE")
assert util.check("Allowed", AA.getAllowed(), true) == 1

// allowed action - MissedCallsRegister.DEACTIVATE
util.log("AA MissedCallsRegister.DEACTIVATE")
AA = WS.getAllowedAction("MissedCallsRegister.DEACTIVATE")
assert util.check("Allowed", [AA.getAllowed(), AA.getBreData().get(0)], [false, "SERVICE_NOT_ACTIVE"]) == 1  

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
