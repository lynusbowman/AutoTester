import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/
lov = "TARIFF"
ID = 502
title = "T-Mobile Autopark 48 mìs."
offer = "500240623"
mf = 419f
group = "INTERNET"
type = "DATA"

try {

/**********************************
           START
 **********************************/
util.log("START")    
assert util.clf.connect(env) == 1
assert util.dwh.connect(env) == 1 
assert util.ap.connect(env) == 1 
assert util.amx.connect(env) == 1

/**********************************
           Tariff in CLF
 **********************************/
util.log("Get TARIFF:" + ID + " from CLF")
tariff = util.clf.getLov(lov, ID)
assert tariff != null
assert util.check("Check CLF", [tariff.getID(), tariff.getTitle(), tariff.getOffer(), tariff.getMonthlyFee(), tariff.getGroup(), tariff.getType()], 
                               [ID, title, offer, mf, group, type]) == 1
  
/**********************************
           Tariff in DWH
 **********************************/    
util.log("Get TARIFF:" + ID + " from DWH")
tariff = util.dwh.getLov(lov, ID)
assert tariff != null
assert util.check("Check DWH", [tariff.getID(), tariff.getTitle(), tariff.getOffer(), tariff.getMonthlyFee(), tariff.getGroup(), tariff.getType()], 
                               [ID, title, offer, mf, group, type]) == 1  
         
/**********************************
           Tariff in AP
 **********************************/                    
util.log("Get TARIFF:" + ID + " from AP")
tariff = util.ap.getLov(lov, ID)
assert tariff != null
assert util.check("Check AP", [tariff.getID(), tariff.getTitle(), tariff.getOffer(), tariff.getMonthlyFee(), tariff.getGroup(), tariff.getType()], 
                              [ID, title, offer, mf, group, type]) == 1
         
/**********************************
           Tariff in AMX
 **********************************/                   
util.log("Get OFFER:" + offer + " from AMX")
offer = util.amx.getLov("OFFER", 500240623)
assert offer != null
assert util.check("Check AMX", [offer.getID(), offer.getTitle(), offer.getServiceLevel(), offer.getSocType()], 
                               [500240623, "T-mobile Autopark 48M", "C", "P"]) == 1    
    
/**********************************
           FINISH
 **********************************/    
assert util.clf.disconnect() == 1
assert util.dwh.disconnect() == 1 
assert util.ap.disconnect() == 1 
assert util.amx.disconnect() == 1
util.log("FINISH")
result = 1
    
}
catch (Error e) {
    
util.err("ERROR")
util.clf.disconnect()
util.dwh.disconnect()
util.ap.disconnect()
util.amx.disconnect()
result = 0
    
}