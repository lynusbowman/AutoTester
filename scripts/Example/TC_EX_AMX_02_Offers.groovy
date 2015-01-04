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

// SU offers
SU = util.amx.getSU(MSISDN)
suOffer = util.amx.getSUOffers(SU)
suOffer.each { offer -> util.log(offer.getID() + ":" + offer.getStatus()) }

// offer 457028096
offer = util.amx.getSUOffer(SU, 457028096)
assert util.check("Status", offer.getStatus(), "A") == 1
assert util.check("Title", offer.getTitle(), "Tariff Profi (60+1)") == 1

// offer 983
offer = util.amx.getSUOffer(SU, 983)
assert util.check("Status", offer.getStatus(), "C") == 1
assert util.check("Title", offer.getTitle(), "FUOM Bez hranic na miru") == 1

// teardown
util.amx.disconnect()

// finish, test passed
util.log("FINISH")
result = 1

}
catch (AssertionError e) {

// teardown
util.amx.disconnect()

// error, test failed
util.err("ERR")
result = 0

}
