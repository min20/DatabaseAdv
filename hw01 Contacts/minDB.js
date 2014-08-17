DB_PATH = "./MIN_DB";
DB_ATTRS = ["id", "name", "phone", "address"];

var fs = require("fs");
var HashMap = require("hashmap").HashMap;
var map = new HashMap();

// MAIN START

console.log("Loading...");
readFile();
console.log("Fin\n");

/*
console.log(select("1510130000001"));
console.log(select("2507100050000"));
console.log(select("9406201234567"));
*/
console.log(select("911116-1000000"));

//testSearchAll1();
//testSearchAll2();
//console.log(testSearchAll2());

// MAIN END


/****************************************
 * DECLARE FUNCTIONS
 ****************************************/

function readFile() {
	if (map.count() <= 0) {
		var data = fs.readFileSync(DB_PATH, "UTF-8")
		data.split("\n").forEach(_dataToHashMap);
	}
}

function select(strKey) {
	return map.get(strKey);
}

function insert(stringData) {
	if (map.has(stringData.split(",")[0])) {
		console.log("KEY Already exists! Try with other key.");
		return;
	}
	fs.appendFile(DB_PATH, stringData + "\n", function(err) {});
	_dataToHashMap(stringData);
}

function _dataToHashMap(rawRow) {
	var arrRow = rawRow.split(",");
	var strKey = arrRow[0];
	var objValue = {};

	for (idx = 1; idx < DB_ATTRS.length; idx++) {
		objValue[DB_ATTRS[idx]] = arrRow[idx];
	}

	map.set(strKey, objValue);
}

function testSearchAll1() {
	var arrKeys = map.keys()
	console.log("num keys: " + arrKeys.length);
	arrKeys.forEach(function(key) {
		//console.log(key);
		select(key);
	});
}

function testSearchAll2() {
	return map.values();
}
