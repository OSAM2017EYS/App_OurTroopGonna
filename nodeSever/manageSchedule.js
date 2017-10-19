var fs = require('fs');
var gt = require('./getTimes.js');


function getDescFileLoca(year,month,date,troop,callback)
{
	gt.getDay(year,month,date,function(momentDate){
		var stringForFileName = (momentDate.year().toString()+momentDate.week()).toString();
		var fileName = './schedules/'+troop+'/'+stringForFileName+'Desc.json';
		return callback(fileName,momentDate);
	});
	
}

function getTitleFileLoca(year,month,date,troop,callback)
{
	gt.getDay(year,month,date,function(momentDate){
		var stringForFileName = (momentDate.year().toString()+momentDate.week()).toString();
		var fileName = './schedules/'+troop+'/'+stringForFileName+'Title.json';
		return callback(fileName,momentDate);
	});
}

function writeScheduleDesc(year,month,date,troop,section,description,callback)
{
	
	//스켸줄 정보를 받아와 json으로 저장한다. 인자는 (연도,월,일자,부대코드,부서,내용)
	
	getDescFileLoca(year,month,date,troop,function(fileName,momentDate)
					   {
		//이부분은 입출력 스트림으로 변경해야될 듯. 입출력 스트림을 사용하지 못하면 async는 불가
		if(fs.existsSync(fileName)) //파일이 존재하는지 검사한다.
	
		{
			//파일이 존재하며, 해당 파일에 지속적으로 작성한다.
			
			var jsonString = fs.readFileSync(fileName);
			
					
			var jsonObject = JSON.parse(jsonString);
			if(!jsonObject[section])//부서의 일정이 비어있는 것으로 초기화시킨다.
			{
				jsonObject[section]=['','','','','','',''];
			}
			jsonObject[section][momentDate.day()] = description;
			fs.writeFileSync(fileName,JSON.stringify(jsonObject));
		}
		else
		{
			//파일이 존재하지 않으며, 새로 파일을 만들어 작성한다.
			var newJsonString = '{}';
			var newJsonObject = JSON.parse(newJsonString);
		
			newJsonObject[section]=['','','','','','',''];
		
			newJsonObject[section][momentDate.day()] = description;
			fs.writeFileSync(fileName,JSON.stringify(newJsonObject));
	}
	
		return callback();

	});
}

function modifySchedule(){
	//이건 클라이언트 쪽에서 수정한 description으로 writeSchedule()을 다시 호출해서 덮어쓰는게 더 나을거 같은데?
	
}

function eraseSchedule(year,month,date,troop,section,callback){
	//스켸줄 정보를 받아와 json으로 저장한다. 인자는 (연도,월,일자,부대코드,부서,내용)
	getDescFileLoca(year,month,date,troop,function(fileName,momentDate){
		if(fs.existsSync(fileName)) //파일이 존재하는지 검사한다.
		{
			//파일이 존재하며, 해당 파일에 지속적으로 작성한다.
			var jsonString = fs.readFileSync(fileName);
			var jsonObject = JSON.parse(jsonString);
			if(!jsonObject[section])//부서의 일정이 비어있는 것으로 초기화시킨다.
				{
					console.log('no schedule to erase');
				}
			jsonObject[section][momentDate.day()] = '';
			fs.writeFileSync(fileName,JSON.stringify(jsonObject));
		}
		else
		{
			//파일이 존재하지 않으며, 그냥 스켸줄이 없다고 알림만 띄운다.
			console.log('no schedule to erase');
		}
		
		getTitleFileLoca(year,month,date,troop,function(fileName,momentDate){
			if(fs.existsSync(fileName)) //파일이 존재하는지 검사한다.
			{		
				//파일이 존재하며, 해당 파일에 지속적으로 작성한다.
				var jsonString = fs.readFileSync(fileName);
				var jsonObject = JSON.parse(jsonString);
				if(!jsonObject[section])//부서의 일정이 비어있는 것으로 초기화시킨다.
				{
					console.log('no schedule to erase');
				}
				jsonObject[section][momentDate.day()] = '';
				fs.writeFileSync(fileName,JSON.stringify(jsonObject));
	
			}
			else
			{
				//파일이 존재하지 않으며, 그냥 스켸줄이 없다고 알림만 띄운다.	
				console.log('no schedule to erase');	
			}	
			
			return callback();
		});
	
	});
	
	
	
}


function writeScheduleTitle(year,month,date,troop,section,username,time,title,secret,callback)
{
	//보좌관님의 주문을 한 번 반영해보자
	//우리에게 필요한 건 기본적으로는 요약만 띄워주고 만약 요약을 클릭하면 세부사항을 보여주는 것이다.
	var formattedString = ' * '+title+' / '+time+' / '+username;
	var targetDir = './schedules/'+troop;
	getTitleFileLoca(year,month,date,troop,function(fileName,momentDate)
					{
		if(fs.existsSync(targetDir)) //파일이 존재하는지 검사한다.
	
		{
			//파일이 존재하며, 해당 파일에 지속적으로 작성한다.
			
			var jsonString = fs.readFileSync(fileName);
			
					
			var jsonObject = JSON.parse(jsonString);
			if(!jsonObject[section])//부서의 일정이 비어있는 것으로 초기화시킨다.
			{
				jsonObject[section]=['','','','','','',''];
			}
			jsonObject[section][momentDate.day()] = ['',4];
			jsonObject[section][momentDate.day()][0] = formattedString;
			jsonObject[section][momentDate.day()][1] = secret;
			fs.writeFileSync(fileName,JSON.stringify(jsonObject));
		}
		else
		{
			//파일이 존재하지 않으며, 새로 파일을 만들어 작성한다.
			

			fs.mkdir(targetDir);
			var newJsonString = '{}';
			var newJsonObject = JSON.parse(newJsonString);
		
			newJsonObject[section]=['','','','','','',''];
		
			jsonObject[section][momentDate.day()] = ['',4];
			jsonObject[section][momentDate.day()][0] = formattedString;
			jsonObject[section][momentDate.day()][1] = secret;
			fs.writeFileSync(fileName,JSON.stringify(newJsonObject));
		}
	
		return callback();

	});
	
	
}

function readScheduleTitle(year,month,date,troop,callback)
{
	
	var targetDir = './schedules/'+troop;
	getTitleFileLoca(year,month,date,troop,function(fileName,momentDate){
		if(fs.existsSync(targetDir)) //파일이 존재하는지 검사한다.
	
		{
			//파일이 존재하면 jsonObject로 반환
			var jsonString = fs.readFileSync(fileName);
			
					
			var jsonObject = JSON.parse(jsonString);
			return callback(jsonObject);
		}
		else
		{
			//파일이 존재하지 않으면 null 반환
			return callback(null);
		}
	
	
	});
}

function readScheduleDesc(year,month,date,troop,callback)
{
	
	var targetDir = './schedules/'+troop;
	getDescFileLoca(year,month,date,troop,function(fileName,momentDate){
		if(fs.existsSync(targetDir)) //파일이 존재하는지 검사한다.
	
		{
			//파일이 존재하면 jsonObject로 반환
			var jsonString = fs.readFileSync(fileName);
			
					
			var jsonObject = JSON.parse(jsonString);
			return callback(jsonObject);
		}
		else
		{
			//파일이 존재하지 않으면 null 반환
			return callback(null);
		}
	
	
	});
}




exports.writeScheduleDesc = writeScheduleDesc;
exports.writeScheduleTitle = writeScheduleTitle;
exports.eraseSchedule = eraseSchedule;
exports.readScheduleDesc = readScheduleDesc;
exports.readScheduleTitle = readScheduleTitle;

//eraseSchedule('2017','10','08','12345','insa');
/*
getInput('2017','10','08','12345','insa','insa test');
getInput('2017','10','08','12345','intel','intel test');
getInput('2017','10','09','12345','oper','oper test');
getInput('2017','10','10','12345','sign','sign test');
getInput('2017','10','11','12345','comm','comm test');
getInput('2017','10','12','12345','logi','logi test');
*/
/* getInput('2017','1','3','makkiato','12345','insa','for test');
	이녀석은 오류가 난다. 포맷이 YYYYMMDD이기 때문
*/