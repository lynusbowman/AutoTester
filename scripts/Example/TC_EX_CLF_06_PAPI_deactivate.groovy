// initialization
import com.bowman.autotester.Utilities

result = 0
util = new Utilities(logID)

try {

// start
util.log("START")

// setup
MSISDN = "603847346"
util.clf.connect(env)

// get, service active
util.log("Get")
SU = util.clf.getSU(MSISDN)
PAPI = util.clf.callPAPI("SU", SU, 1, "G")
assert util.check("Status", PAPI.getStatus(), "a") == 1

// deactivation
util.log("Dectivation")
PAPI = util.clf.callPAPI("SU", SU, 1, "d", [1], [22])

// get, service deactive
util.log("Get")
PAPI = util.clf.callPAPI("SU", SU, 1, "G")
assert util.check("Status", PAPI.getStatus(), "d") == 1
assert util.check("Attr 1 reason_status", PAPI.getAttr(1), "22") == 1

// teardown
util.clf.disconnect()

// finish, test passed
util.log("FINISH")
result = 1

}
catch (Error e) {

// teardown
util.clf.disconnect()

// error, test failed
util.err("ERR")
result = 0

}
