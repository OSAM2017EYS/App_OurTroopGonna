//비동기 설계완료
fs = require('fs');

function splitCord(completeCode)
{
	
	return completeCode.split(' ');
}

function mergeCord(splitedCord)
{
	var mergedCord='';
	
	for(var eachcord in splitedCord)
		{
			mergedCord += eachcord+' ';
			
		}
	mergedCord.trim();
	
	return mergedCord;	
}

function getCode(username,callback)
{
	fs.readFile('./account.json',function(err,data){
		if(err) {throw err;}
		
		var jsonObj = JSON.parse(data);
		
		return callback(jsonObj[username][1]);
			
	});
}

function setInfoByName(username,info,callback)
{
	
	getCode(username,function(data){
		var splitedCode = splitCord(data);
	
		var fileLocation = './accounts';//이 디렉토리는 이미 정해져있다.
		for(var i = 0;i<5;i++)	//부대코드는 5단계로 구성
		{
			fileLocation+='/'+splitedCode[i];
			
			if((fs.existsSync(fileLocation)) === false)
			
			//디렉토리가 존재하지 않는다면 디렉토리를 만들어준다.
			//exists는 syncronous하게 가자 그러지 않으면 exists가 완료되기 전에 이터레이터가 먼저 넘어간다.
			{
				fs.mkdirSync(fileLocation);
			}
			
			if(splitedCode[i] === '0' || splitedCode[i] === '00' || splitedCode[i] === '000')
				//참모부면 중단
				break;
		}
		//마지막으로 파일명을 입혀주면 끝
		fileLocation += '/'+ username+'.json';
	
		fs.writeFile(fileLocation,info,function(err){
			if(err)
				{
					console.log('oh.... stream...');
					return callback(false);
				}
			console.log("Yap");
			return callback(true);
		});
	});
	
}

function getInfoByName(username,callback)
{
	getCode(username,function(data){
		var splitedCode = splitCord(data);
	
		var fileLocation = './accounts';//이 디렉토리는 이미 정해져있다.
		for(var i = 0;i<5;i++)	//부대코드는 5단계로 구성
		{
			fileLocation+='/'+splitedCode[i];
			
			
			if(splitedCode[i] === '0' || splitedCode[i] === '00' || splitedCode[i] === '000')
				//참모부면 중단
				break;
		}
		//마지막으로 파일명을 입혀주면 끝
		fileLocation += '/'+ username+'.json';
	
		fs.readFile(fileLocation,function(err,data){
			if(err) throw err;
			
			var jsonObj = JSON.parse(data);
			return callback(data);
		});
	});
}
function isInUDRelation(highcode,lowcode,callback)
{
	//high의 부대가 low의 부대보다 상위인지 검사한다.
	var spHigh = splitCord(highcode);
	var spLow = splitCord(lowcode);
	
	for(var i = 0 ; i < 5 ; i++ )
		{
			if(spLow[i] === spHigh[i]) //low가 high의 소속을 따라가는지 확인하다가.
				continue;
			else if(spHigh[i] === '000' || spHigh[i] === '00' || spHigh[i] === '0')
				{
				//high가 참모부면 true 반환
				console.log("Ya Im your boss");
				return callback(true);
				}
		}

	console.log("Im not");
	return callback(false);
		
	
}

function getTroopLevel(spCode,callback)
{
	
	
	for(var i = 0; i<5; i++)
		{
			if(spCode[i] === '0' || spCode[i] === '00' || spCode[i] === '000' || spCode[i] === '999')
				{
					return callback(i);
				}
		}
	return callback(4);
}

function getHighTroop(troopcode,callback)
{
	var spCode = troopcode.split(' ');
	
	getTroopLevel(spCode,function(level){
		var highCode=['0','00','000','000','000'];
		
		for(var i = 0; i < level; i++)
			{
				console.log('compare '+i+' ' +highCode[i]+' '+spCode[i]);
				highCode[i]=spCode[i];
			}
		
		return callback(highCode);
	});
}


//getHighTroop('1 30 203 099 012',function(code){console.log(code);});
/*
getCode('makkiato',function(data){
	getInfoByCode(data,function(data){
		console.log(data);
	});
});
*/


exports.isInUDRelation = isInUDRelation;

exports.getCode = getCode;
exports.setInfoByName = setInfoByName;
exports.getInfoByName = getInfoByName;
exports.splitCord=splitCord;
exports.mergeCord=mergeCord;
