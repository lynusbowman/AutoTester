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

// CASE C1878
cas = util.clf.getCase(MSISDN, "C1878")
assert cas != null
util.log(cas.getID())
assert util.check("Process", cas.getProcess(), "ZM�NY Z�KAZNICK�HO PROFILU") == 1
assert util.check("Subject", cas.getSubject(), "Tarif") == 1
assert util.check("Subsubject", cas.getSubsubject(), "��dost z�kazn�ka") == 1
assert util.check("Step", cas.getStep(), "...") == 1
assert util.check("Status", cas.getStatus(), "OPEN") == 1
assert util.check("MFFT1 Predchozi tarif", cas.getMFFT()[0], "Profi na m�ru 1") == 1
assert util.check("MFFT2 Novy tarif", cas.getMFFT()[1], "Profi na m�ru 2") == 1

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
