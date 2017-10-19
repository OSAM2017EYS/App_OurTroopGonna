//비동기 설계완료

var mo = require('moment');


mo.locale('ko', {
    weekdays: ["일요일","월요일","화요일","수요일","목요일","금요일","토요일"],
    weekdaysShort: ["일","월","화","수","목","금","토"],
});


function getDay(year,month,date,callback){
	//인자의 연,월,일을 이용해 moment객체를 반환한다.
	
	var dateString = year+month+date;
	
	var m = mo(dateString,'YYYYMMDD');
	return callback(m);
}


function convertToWeekday(year,month,date,callback)
{
	//moment객체의 포맷을 연도주차 로 변경하여 반환한다.
	var original = mo(year+month+date,'YYYYMMDD');
	
	var Cyear = original.year();
	var Cweek = original.week();
	var Cday = original.day();
	
	var jsonString = '{ "year" : "'+Cyear+'" , "week" : "'+Cweek+'" , "day" : "'+Cday+'" }';
	console.log(jsonString);
	var jsonObj = JSON.parse(jsonString);
	
	return callback(jsonObj);
}

function toStringWeekday(m,callback)
{
	return callback(m.format('gggg년 ww주차 dddd'));
}


exports.getDay = getDay;
exports.convertToWeekday = convertToWeekday;
exports.toStringWeekday = toStringWeekday;

//TODO moment 숙달

// http://pikabu.tistory.com/50
// http://blog.jeonghwan.net/momentjs/
// http://momentjs.com/docs/
