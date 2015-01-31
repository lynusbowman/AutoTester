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

// get customer hierarchy
hier = util.clf.getCustHierarchy(MSISDN)
CU = util.clf.getCU(MSISDN)
LE = util.clf.getLE(MSISDN)
OU = util.clf.getOU(MSISDN)
EU = util.clf.getEU(MSISDN)
BA = util.clf.getBA(MSISDN)
SU = util.clf.getSU(MSISDN)

// check hierarchy
assert util.check("CU", hier.getCU().getExtID(), CU) == 1
assert util.check("LE", hier.getLE().getExtID(), LE) == 1
assert util.check("OU", hier.getOU().getExtID(), OU) == 1
assert util.check("EU", hier.getEU().getExtID(), EU) == 1
assert util.check("BA", hier.getBA().getExtID(), BA) == 1
assert util.check("SU", hier.getSU().getExtID(), SU) == 1

// bilcycle
BC = util.clf.getBillCycle(MSISDN)
assert util.check("BC", BC.getID(), 52) == 1

// segment
segment = util.clf.getSegment(MSISDN)
assert util.check("Segment", segment.getTitle(), "LE") == 1

// market
market = util.clf.getMarket(MSISDN)
assert util.check("Market", market.getTitle(), "GSM") == 1

// tariff
tariff = util.clf.getTariff(MSISDN)
assert util.check("Tariff", tariff.getID(), 204) == 1

// contract duration
contr = util.clf.getContractDuration(MSISDN)
assert util.check("Contract duration", contr.getTitle(), "Indefinite") == 1

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
