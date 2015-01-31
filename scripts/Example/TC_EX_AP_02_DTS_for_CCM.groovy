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

// TO_SU
util.log("TO_SU")
dts = util.ap.callDTS("SU", SU, "TO_SU")
assert util.check("Result", dts.getResult(), "2") == 1
assert util.checkLike("Warning", dts.getWarnings()[0], "vod je mo")

params = dts.getParameters()
params.each { key, value -> util.log(key + ":" + value) }

// teardown
util.ap.disconnect()

// finish, test passed
util.log("FINISH")
result = 1

}
catch (Error e) {

// teardown
util.ap.disconnect()

// error, test failed
util.err("ERR")
result = 0

}
