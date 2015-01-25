import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/  
lov = "SERVICE"
srvID = 794
paramID = 510
paramValues = ["500240693", "500240703", "500240713", "500240723", "500240733"]
offerIDs = [500240693, 500240703, 500240713, 500240723, 500240733]
offerCLFTitles = ["Mesicni pausal - tarif Autopark bez zavazku",
                  "Mesicni pausal - tarif Autopark 24 mes.",
                  "Mesicni pausal - tarif Autopark 36 mes.",
                  "Mesicni pausal - tarif Autopark 48 mes.",
                  "Mesicni pausal - tarif Autopark 60 mes."]
offerAMXTitles = ["DISCO2_367", "DISCO2_368", "DISCO2_369", "DISCO2_370", "DISCO2_371"]

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
           Service in CLF
 **********************************/  
util.log("Get SERVICE:" + srvID + " from CLF")
srv = util.clf.getLov(lov, srvID)
param = srv.getParam(paramID)
assert param != null
    
for (i in 0..4) {  
  offer = srv.getOffer(offerIDs[i])  
  assert util.check("Check CLF", [param.getValue(paramValues[i]), offer.getID(), offer.getTitle(), offer.getParams()[0]], 
                                 [offerCLFTitles[i], offerIDs[i], offerCLFTitles[i], "Discount Percentage"]) == 1
}                            
    
/**********************************
           Service in DWH
 **********************************/      
util.log("Get SERVICE:" + srvID + " from DWH")
srv = util.dwh.getLov(lov, srvID)
param = srv.getParam(paramID)
assert param != null

for (i in 0..4) {
  assert util.check("Check DWH", param.getValue(paramValues[i]), offerCLFTitles[i]) == 1 
}
      
/**********************************
           Service in AP
 **********************************/                      
util.log("Get SERVICE:" + srvID + " from AP")
srv = util.ap.getLov(lov, srvID)
param = srv.getParam(paramID)
assert param != null

for (i in 0..4) {
  assert util.check("Check AP", param.getValue(paramValues[i]), offerCLFTitles[i]) == 1 
} 
  
/**********************************
           Offer in AMX
 **********************************/       
for (i in 0..4) {
  util.log("Get OFFER:" + offerIDs[i] + " from AMX")
  offer = util.amx.getLov("OFFER", offerIDs[i])
  assert offer != null
  assert util.check("Check AMX", [offer.getID(), offer.getTitle(), offer.getServiceLevel(), offer.getSocType()], 
                                 [offerIDs[i], offerAMXTitles[i], "G", "D"]) == 1    
}                           
    
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