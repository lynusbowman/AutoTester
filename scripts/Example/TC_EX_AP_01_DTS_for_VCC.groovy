// initialization
import com.bowman.autotester.Utilities

result = 0
util = new Utilities(logID)

try {

// start
util.log("START")

// setup
SU = 41999702
util.ap.connect(env)

// RC_SWITCH
util.log("RC_SWITCH")
dts = util.ap.callDTS("SU", SU, "RC_SWITCH", ["NEW_TARIFF"], ["433"])
assert util.check("Result", dts.getResult(), "RC_T8") == 1
assert util.check("Action 1", dts.getAction("WARN_ABOUT_UNUSED_FUOMS_LOST")[0], "WARN_ABOUT_UNUSED_FUOMS_LOST") == 1
assert util.check("Action 2", dts.getAction("ROAMING_DAILY_PASS_ACTIVATE")[0], "ROAMING_DAILY_PASS_ACTIVATE") == 1

params = dts.getParameters()
params.each { key, value -> util.log(key + ":" + value) }

// teardown
util.ap.disconnect()

// finish, test passed
util.log("FINISH")
result = 1

}
catch (AssertionError e) {

// teardown
util.ap.disconnect()

// error, test failed
util.err("ERR")
result = 0

}
