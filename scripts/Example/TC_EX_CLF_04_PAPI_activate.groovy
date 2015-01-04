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

// get, service deactive
util.log("Get")
SU = util.clf.getSU(MSISDN)
PAPI = util.clf.callPAPI("SU", SU, 1, "G")
assert util.check("Status", PAPI.getStatus(), "d") == 1

// activation
util.log("Activation")
PAPI = util.clf.callPAPI("SU", SU, 1, "a", [1, 735, 736, 813], [21, "OBE", "OBE", "lynus@seznam.cz"])

// get, service active
util.log("Get")
PAPI = util.clf.callPAPI("SU", SU, 1, "G")
assert util.check("Status", PAPI.getStatus(), "a") == 1
assert util.check("Attr 1 reason_status", PAPI.getAttr(1), "21") == 1
assert util.check("Attr 735 notification_type", PAPI.getAttr(735), "OBE") == 1
assert util.check("Attr 736 message_type", PAPI.getAttr(735), "OBE") == 1
assert util.check("Attr 813 email_address", PAPI.getAttr(813), "lynus@seznam.cz") == 1
assert util.check("Attr 1223 inkaso", PAPI.getAttr(1223), "N") == 1

// teardown
util.clf.disconnect()

// finish, test passed
util.log("FINISH")
result = 1

}
catch (AssertionError e) {

// teardown
util.clf.disconnect()

// error, test failed
util.err("ERR")
result = 0

}
