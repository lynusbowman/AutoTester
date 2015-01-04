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

// SU services
SU = util.clf.getSU(MSISDN)
suSrv = util.clf.getSUServices(SU)
suSrv.each { srv -> util.log(srv.getID()) }

// service 897
srv = util.clf.getSUService(SU, 897)
assert util.checkNok("Instance", srv.getInstanceID(), 0) == 1
assert util.check("Status", srv.getStatus(), "a") == 1
assert util.checkNok("Param 1531", srv.getParam(1531), "0") == 1
assert util.checkLike("Param 1534", srv.getParam(1534), "Z1-2") == 1

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
