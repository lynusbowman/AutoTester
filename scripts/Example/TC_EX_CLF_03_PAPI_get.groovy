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

// PAPI 506, service deactive
util.log("PAPI 506")
SU = util.clf.getSU(MSISDN)
PAPI = util.clf.callPAPI("SU", SU, 506, "G")
assert util.check("Status", PAPI.getStatus(), "d") == 1

// PAPI 430, service active
util.log("PAPI 430")
PAPI = util.clf.callPAPI("SU", SU, 430, "G")
assert util.check("Status", PAPI.getStatus(), "a") == 1
assert util.check("Attr 1 reason_status", PAPI.getAttr(1), "1") == 1
assert util.check("Attr 1738 national_calls", PAPI.getAttr(1738), "CZ=1000,") == 1

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
