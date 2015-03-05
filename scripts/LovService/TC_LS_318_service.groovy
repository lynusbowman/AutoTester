import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/
DBs = ["CLF", "DWH", "AP"]
lov = "SERVICE"
ID = 318
title = "Mobilní Internet – účtování pre-paidu"
paramIDs = [1825, 1826, 1827]
paramTitles = ["MSISDN podřízené (pre-paid)", "Discount", "Typ balíčku"]
paramDataTypes = [7, 7, 7]
paramRequired = [1, 1, 1]
param1826Values = ["0%", "25%", "50%", "75%", "100%"]
param1827Values = ["316", "317"]
param1827ValueTitles = ["Neomezené prohlížení", "Neomezené přehrávání"]
offerIDs = [500244343, 500244353, 500244423, 500244433]
offerTitles = ["Neomezene prohlizeni", "Neomezene prehravani", "D_Vodefend1", "D_Vodefend2"]
offerParams = ["", "", "Discount Percentage", "Discount Percentage"]
offerSocTypes = ["U", "U", "D", "D"]

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
           Service in CLF, DWH, AP
 **********************************/
for (i in 0 .. 2) {

  util.log("Get SERVICE:" + ID + " from " + DBs[i])
  if (i == 0) {
    srv = util.clf.getLov(lov, ID)
  }
  else if (i == 1) {
    srv = util.dwh.getLov(lov, ID)
  }
  else {
    srv = util.ap.getLov(lov, ID)
  }
  assert srv != null

  if (i == 0) {
    assert util.check("Service " + ID, [srv.getID(), srv.getTitle(), srv.getSuspendable(), srv.getSchedulable(),
                                        srv.getNetworkIndicator(), srv.getSU(), srv.getMultiple()],
                                       [ID, title, 1, 1, 0, 1, 1]) == 1
    for (k in 0 .. offerIDs.size()-1) {
      offer = srv.getOffer(offerIDs[k])
      assert util.check("Offer " + offerIDs[k], [offer.getID(), offer.getTitle()], [offerIDs[k], offerTitles[k]]) == 1
      assert util.check("Offer params ", offer.getParams(), [offerParams[k]]) == 1
    }
  }
  else {
    assert util.check("Service " + ID, [srv.getID(), srv.getTitle()], [ID, title]) == 1
  }    

/**********************************
           Parameters
 **********************************/
  for (j in 0 .. paramIDs.size()-1) {
    param = srv.getParam(paramIDs[j])

    if (i == 0) {
      assert util.check("Check param " + paramIDs[j], [param.getID(), param.getTitle(), param.getDataType(), param.getRequired()],
                                                      [paramIDs[j], paramTitles[j], paramDataTypes[j], paramRequired[j]]) == 1
    }
    else {
      assert util.check("Check param " + paramIDs[j], [param.getID(), param.getTitle()], [paramIDs[j], paramTitles[j]]) == 1
    }
    
    values = param.getValues()
    if (paramIDs[j] == 1826) {
      assert util.check("Values for param " + paramIDs[j], values, param1826Values, param1826Values)  == 1               
    }
    else if (paramIDs[j] == 1827) {
      assert util.check("Values for param " + paramIDs[j], values, param1827Values, param1827ValueTitles)  == 1     
    }
                
  }

}

/**********************************
           Offers in AMX
 **********************************/
for (k in 0 .. offerIDs.size()-1) {
    
  util.log("Get OFFER:" + offerIDs[k] + " from AMX")
  offer = util.amx.getLov("OFFER", offerIDs[k])
  assert offer != null
  assert util.check("Check AMX", [offer.getID(), offer.getTitle(), offer.getServiceLevel(), offer.getSocType()],
                                 [offerIDs[k], offerTitles[k], "C", offerSocTypes[k]]) == 1
                           
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

