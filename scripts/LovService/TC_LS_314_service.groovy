import com.bowman.autotester.Utilities

result = 0
Utilities util = new Utilities(logID)

/**********************************
           Data
 **********************************/
DBs = ["CLF", "DWH", "AP"]
lov = "SERVICE"
ID = 314
title = "Firemní řešení – Tiskárna pro SME"
paramIDs = [1818, 1819]
paramTitles = ["Černobílá tiskárna – počet kusů", "Barevná tiskárna – počet kusů"]
paramDataTypes = [0, 0]
paramRequired = [0, 0]
offerIDs = [500244973, 500244983]
offerTitles = ["Printer Black-White", "Printer Color"]

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
                                        srv.getNetworkIndicator(), srv.getBA()],
                                       [ID, title, 1, 0, 0, 1]) == 1
    for (k in 0 .. offerIDs.size()-1) {
      offer = srv.getOffer(offerIDs[k])
      assert util.check("Offer " + offerIDs[k], [offer.getID(), offer.getTitle()], [offerIDs[k], offerTitles[k]]) == 1
      assert util.check("Offer params ", offer.getParams(), ["Items count BA"]) == 1
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
                                 [offerIDs[k], offerTitles[k], "G", "U"]) == 1
                           
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

