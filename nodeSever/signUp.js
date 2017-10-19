var fs = require('fs');
var tco = require('./troopcordOperator');

//비동기화 완료 아마도.... 디렉토리와 이터레이터에 대해서 동기식으로 가야 안전할 듯



function signUp(solNumber,password,troopcode,realName,callback){
	//사용자 등록
	
	//fs.exists()를 사용하지 않는다. 어카운트.제이슨은 초기로 admin계정의 정보를 갖고 있다. 무조건
	fs.readFile('./account.json',function(err,data){
		if(err) throw err;
	
		var parsedOriginal = JSON.parse(data);
		if(parsedOriginal[solNumber] !== undefined) // 이미 등록한 사용자인 경우 등록하지 않는다.
			return callback(false);
		else
			{
		parsedOriginal[solNumber]=[password,troopcode,realName];
		var finalData = JSON.stringify(parsedOriginal);
	
		fs.writeFile('./account.json',finalData,function(err,data){
			if(err) throw err;
			//디렉토리를 만들기 위해 split한다.
	
			var splitedcode = tco.splitCord(troopcode);
			var fileLocation = './accounts';//이 디렉토리는 이미 정해져있다.
			for(var i = 0;i<5;i++)	//부대코드는 5단계로 구성
				{
					fileLocation+='/'+splitedcode[i];

					if((fs.existsSync(fileLocation)) === false)

					//디렉토리가 존재하지 않는다면 디렉토리를 만들어준다.
					//exists는 syncronous하게 가자 그러지 않으면 exists가 완료되기 전에 이터레이터가 먼저 넘어간다.
					{

						console.log(fileLocation);
						fs.mkdirSync(fileLocation);
					}
					if(splitedcode[i] === '0' || splitedcode[i] === '00' || splitedcode[i] === '000')
						//참모부면 중단
						break;
				}
			//마지막으로 파일명을 입혀주면 끝
			fileLocation += '/'+ solNumber+'.json';



			//정보들의 초기값 설정
			var accountString = '{}';
			var accountData = JSON.parse(accountString);
			accountData['section']='No Section';
			accountData['admin']='N';
			accountData['secretLevel'] = '4';
			//secret Level 1 -> 1급 비취, 2->2급, 3->3급, 4->대외비

			fs.writeFile(fileLocation,JSON.stringify(accountData),function(err){
					if(err) throw err;
					return callback(true);
				
			});
			
		});
		
	
			}

			
	});
	
}

exports.signUp=signUp;
	
	//먼저 account.json에 군번,비밀번호,부대번호를 넣는다
	
	
	//부대번호를 기초로 하여 각 계정의 상세정보를 담고 있는 파일을 쓴다.
	//이때 부대번호는 ' '를 기준으로 split한다는 것을 잊지말자.
	/*
`		0.군종별(육해공) 1자리 1육군 2해군 3공군 4국방부직속
		1.사령부,군단별 2자리 00각군본부 01 1군단 02 2군단...... 10 1군사 20 2작사 30 3야전사 40~ 기타 교육사 의무사 기무사
		2.사/여단 3자리 이거는 사/여단 번호 그대로 갖고 감 000긱사령부본부 032 32사단 203 203특공여단
		3.사/여단 예하 연대/여단 혹은 직할 3자리 사/여단참모부 000 직할대 999 연대/여단은 그대로 씀
		4.대대별 번호 3자리 임의 부여
		
		그래서 우리대대는 1 20 032 999 952
		1사단 참모부는 1 30 001 000 000
	*/
	
	
	
	

//signUp('justDoIt','kjo','1 20 032 999 922',function(){console.log('just did it!');});

//signUp('makkiato','kjo','1 20 032 999 952');
//signUp('juice','bae','1 30 001 000 000');