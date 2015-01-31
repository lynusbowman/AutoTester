// initialization
import com.bowman.autotester.Utilities

result = 0
util = new Utilities(logID)

try {

// start
util.log("START")

// setup
MSISDN = "603847346"
util.amx.connect(env)

// SU
SU = util.amx.getSU(MSISDN)
assert util.check("SU", SU, 41999702)

// CU
CU = util.amx.getCU(MSISDN)
assert util.check("CU", CU, 100005344)

// teardown
util.amx.disconnect()

// finish, test passed
util.log("FINISH")
result = 1

}
catch (Error e) {

// teardown
util.amx.disconnect()

// error, test failed
util.err("ERR")
result = 0

}
