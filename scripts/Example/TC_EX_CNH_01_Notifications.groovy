// initialization
import com.bowman.autotester.Utilities

result = 0
util = new Utilities(logID)

try {

// start
util.log("START")

// setup
util.cnh.connect(env)

// SMS
util.log("SMS")
MSISDN = "603713546"
notif = util.cnh.getNotification(MSISDN)
assert util.check("Template", notif.getTemplate(), "pactum2_insurance_deactivate_sms") == 1
assert util.check("Status", notif.getStatus(), 108) == 1

params = notif.getParameters()
params.each {key, value -> util.log(key + ":" + value)}

assert util.check("SERVICE", notif.getParam("SERVICE"), "T-Mobile pro jistotu") == 1

// EMAIL
util.log("EMAIL")
EMAIL = "martin.zima@t-mobile.cz"
notif = util.cnh.getNotification(EMAIL)
assert util.check("Template", notif.getTemplate(), "pactum2_insurance_deactivate_email") == 1
assert util.check("Status", notif.getStatus(), 103) == 1

params = notif.getParameters()
params.each {key, value -> util.log(key + ":" + value)}

assert util.checkNok("CODE", notif.getParam("CODE"), "0") == 1

// teardown
util.cnh.disconnect()

// finish, test passed
util.log("FINISH")
result = 1

}
catch (AssertionError e) {

// teardown
util.cnh.disconnect()

// error, test failed
util.err("ERR")
result = 0

}
