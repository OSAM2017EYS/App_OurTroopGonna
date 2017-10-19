var express = require('express');
var app = express();
var bp = require('body-parser');

var si = require('./signIn.js');
var su = require('./signUp.js');
var sa = require('./setAuth.js');
var ms = require('./manageSchedule.js');




/*
실제로 할 때는 sync와 async를 적절히 사용해보자. 이를테면, 파일을 쓰는거는 sync로 하는게 안전하겠지?
특히 stream을 다루는 법을 좀 더 공부했으면 좋겠다. 
*/
app.use(bp.urlencoded({extended: false}));


//사용자 등록 포트 리스닝 
var SignUpConnect = express();
app.get('/SU', function(req, res){

	console.log('we got req');
	var user = req.query.user.toString();
	var pw = req.query.pw.toString();
	var troop = req.query.troop.toString().trim();
	var name = req.query.name.toString();
	
	console.log(user+' '+pw+' '+troop);
	console.log('connection built');
	su.signUp(user,pw,troop,name,function(){
		res.sendStatus(200).send(new Buffer('complete'.trim()));
});
});





//로그인 포트 리스닝

app.get('/SI',function(req, res){

	var user = req.query.user.toString();
	var pw = req.query.pw.toString();

	console.log('got req');
	
	si.signIn(user,pw,function(qual,jsonObj){
			if(qual)
			{
				console.log('sign in success');
				console.log('we send '+jsonObj);
				return res.send(jsonObj);
			}

			else
			{
				return res.send(null);
				
			}
		});
});




//스켸줄 입력 포트 리스닝



app.get('/WS',function(req,res)
{
	var year = req.query.year.toString();
	var month = req.query.month.toString();
	var date = req.query.date.toString();
	var troop = req.query.troop.toString();
	var user = req.query.user.toString();
	var time = req.query.time.toString();
	var title = req.query.title.toString();
	var desc = req.query.desc.toString();
	


	ms.WriteScheduleTitle(year,month,date,troop,section,username,time,title,function(){
		
		ms.WriteScheduleDesc(year,month,date,troop,section,description,function(){
		
		return res.send(true);

		});
	});	
});


//스켸줄 제목 출력 포트 리스닝



	
app.get('/RST',function(req,res)
{
	var year = req.query.year.toString();
	var month = req.query.month.toString();
	var date = req.query.date.toString();
	var troop = req.query.troop.toString();
	
	ms.readScehduleTitle(year,month,date,troop,function(jsonObj){
		if(jsonObject === null)
		{
			return res.send(null);
		}
		else
		{
			return res.json(jsonObj);
		}
	});

});


//스켸줄 상세 출력 포트 리스닝



app.get('/RSD',function(req,res)
{
	var year = req.query.year.toString();
	var month = req.query.month.toString();
	var date = req.query.date.toString();
	var troop = req.query.troop.toString();
	
	ms.readScehduleDesc(year,month,date,troop,function(jsonObj){
		if(jsonObject === null)
		{
			return res.send(null);
		}
		else
		{
			return res.json(jsonObj);
		}
	});

});



//부서 지정 리스닝



app.get('/SAu',function(req,res)
{
	
	var user = req.query.user;
	var target = req.query.target;
	var sec = req.query.sec;
	
	
	
	sa.setAuth(user,target,sec,function(valid){
				return res.send(valid);
			
		
	});
});

//인사권 지정 리스닝


app.get('/SAd',function(req,res)
{
	
	var user = req.query.user;
	var target = req.query.target;
	var value = req.query.value;
	
	sa.setAdmin(user,target,value,function(valid){
		
		
				return res.send(valid);
	
		
	});
});




//사용자 정보 조회


app.get('/GMI',function(req,res)
{
	var user = req.query.user;

	sa.getMyInfo(user,function(jsonObj){
		if(jsonObj)
		{
			res.status(200).json(jsonObj);
		}
		else
		{
			console.log('fail');
			res.send(null);
		}

	});
});

app.get('SSL',function(req,res)
{
	var user = req.query.user;
	var secret = req.query.secret;
	sa.setSecretLevel(user,secret,function(valid)
		{
			return valid;
		});
});

app.listen(3000,function(){
	console.log('server on!');
});



/*
function signInUpTest(){
	su.signUp('troop','module','3 21 121 003 000',function(){console.log('signed Up!');});
	//su.signUp('reciever','section','1 11 111 999 123');
	
	si.signIn('troop','module',function(data){console.log(data);});
	
}

function setAuthTest(){
	
	sa.setAuth('setter','reciever','intel',function(){console.log('setting author complete');});
	
	sa.setAdmin('setter','reciever','Y',function(){console.log('setting admin complete');});
	
}

//setAuthTest();
//calModuleTest();
//signInUpTest();
//TODO json으로 계정정보 검증 account.json과 userNumber.json 두개 파일을 이용해서 검증하고 (개개인의UID).json파일에 권한을 정해보자
//		+ (선택) Hash이용하여 암호화


//TODO 부대코드 검증 관련한 부분도 필요하지


TODO 부대코드 디자인
	1.군종별(육해공) 1자리 1육군 2해군 3공군 0국방부직속
	2.사령부,군단별 2자리 00각군본부 01 1군단 02 2군단...... 10 1군사 20 2작사 30 3야전사 40~ 기타 교육사 의무사 기무사
	3.사/여단 3자리 이거는 사/여단 번호 그대로 갖고 감 000긱사령부본부 032 32사단 203 203특공여단
	4.사/여단 예하 연대/여단 혹은 직할 3자리 사/여단참모부 000 직할대 999 연대/여단은 그대로 씀
	5.대대별 번호 3자리 임의 부여
	
	그래서 우리대대는 1 20 032 999 952
	1사단 참모부는 1 30 001 000 000

*/
