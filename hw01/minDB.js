DB_PATH = "./MIN_DB";
DB_ATTRS = ["id", "name", "phone", "address"];

var fs = require("fs");
var HashMap = require("hashmap").HashMap;
var map = new HashMap();

// MAIN START

search("1510130000001");
search("2507100050000");
insert("9406201234567,POPPI,01063710168,경기도 성남시 분당구 야탑동");
search("9406201234567");

// MAIN END


/****************************************
 * DECLARE FUNCTIONS
 ****************************************/

function search(strKey) {
	var strKeyLength = strKey.length;

	if (map.count() <= 0) {
		var data = fs.readFileSync(DB_PATH, "UTF-8")
		data.split("\n").forEach(dataToHashMap);
	}
	
	console.log(map.get(strKey));
}

function dataToHashMap(rawRow) {
	var arrRow = rawRow.split(",");
	var strKey = arrRow[0];
	var objValue = {};

	for (idx = 1; idx < DB_ATTRS.length; idx++) {
		objValue[DB_ATTRS[idx]] = arrRow[idx];
	}

	map.set(strKey, objValue);
}

function insert(stringData) {
	fs.appendFile(DB_PATH, stringData + "\n", function(err) {});
	dataToHashMap(stringData);
}

