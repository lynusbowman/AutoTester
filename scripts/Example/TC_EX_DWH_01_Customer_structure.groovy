// initialization
import com.bowman.autotester.Utilities

result = 0
util = new Utilities(logID)

try {

// start
util.log("START")

// setup
MSISDN = "603847346"
util.dwh.connect(env)

// get customer hierarchy
hier = util.dwh.getCustHierarchy(MSISDN)
CU = util.dwh.getCU(MSISDN)
LE = util.dwh.getLE(MSISDN)
OU = util.dwh.getOU(MSISDN)
EU = util.dwh.getEU(MSISDN)
BA = util.dwh.getBA(MSISDN)
SU = util.dwh.getSU(MSISDN)

// check hierarchy
assert util.check("CU", hier.getCU().getExtID(), CU) == 1
assert util.check("LE", hier.getLE().getExtID(), LE) == 1
assert util.check("OU", hier.getOU().getExtID(), OU) == 1
assert util.check("EU", hier.getEU().getExtID(), EU) == 1
assert util.check("BA", hier.getBA().getExtID(), BA) == 1
assert util.check("SU", hier.getSU().getExtID(), SU) == 1

// bilcycle
BC = util.dwh.getBillCycle(MSISDN)
assert util.check("BC", BC.getID(), 52) == 1

// segment
segment = util.dwh.getSegment(MSISDN)
assert util.check("Segment", segment.getTitle(), "LE") == 1

// market
market = util.dwh.getMarket(MSISDN)
assert util.check("Market", market.getTitle(), "GSM") == 1

// tariff
tariff = util.dwh.getTariff(MSISDN)
assert util.check("Tariff", tariff.getID(), 204) == 1

// teardown
util.clf.disconnect()

// finish, test passed
util.log("FINISH")
result = 1

}
catch (Error e) {

// teardown
util.dwh.disconnect()

// error, test failed
util.err("ERR")
result = 0

}
