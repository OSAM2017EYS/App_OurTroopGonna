var express = require('express');
var app = express();
var bp = require('body-parser');
var gt = require('./getTimes.js');
var si = require('./signIn.js');
var su = require('./signUp.js');
var sa = require('./setAuth.js');
var ms = require('./manageSchedule.js');




/*
실제로 할 때는 sync와 async를 적절히 사용해보자. 이를테면, 파일을 쓰는거는 sync로 하는게 안전하겠지?
특히 stream을 다루는 법을 좀 더 공부했으면 좋겠다. 
*/



app.use(bp.json()); // for parsing application/json
app.use(bp.urlencoded({ extended: true })); // for parsing application/x-www-form-urlencoded

//사용자 등록 포트 리스닝 
app.post('/SU', function(req, res){

	console.log('we got req');
	console.log(req.body);
	var user = req.body.user;
	
	var pw = req.body.pw;
	var troop = req.body.troop;
	var name = req.body.name;
	
	
	console.log(user+' '+pw+' '+troop);
	console.log('connection built');
	su.signUp(user,pw,troop,name,function(valid){
		res.send(valid.toString());
});
});


app.get('/',function(req,res)
	   {
	console.log('test test');
	res.send('test test');
});


//로그인 포트 리스닝

app.get('/SI',function(req, res){
	console.log('got req');
	var user = req.query.user.toString();
	var pw = req.query.pw.toString();
	console.log(user);
	console.log(pw);
	
	
	si.signIn(user,pw,function(qual,jsonObj){
			console.log(qual);
			if(qual)
			{
				console.log('sign in success');
				console.log('we send '+jsonObj);
				return res.status(200).json(jsonObj);
			}

			else
			{
				return res.send("false");
				
			}
		});
});




//스켸줄 입력 포트 리스닝



app.post('/WS',function(req,res)
{
	var year = req.body.year.toString();
	var month = req.body.month.toString();
	var date = req.body.date.toString();
	var troop = req.body.troop.toString();
	var charge = req.body.charge.toString();
	var time = req.body.time.toString();
	var title = req.body.title.toString();
	var desc = req.body.desc.toString();
	var section = req.body.section.toString();
	var writer = req.body.writer.toString();
	var secret = req.body.secret.toString();
	

	ms.writeScheduleTitle(year,month,date,troop,section,charge,time,title,writer,secret,function(){
		
		ms.writeScheduleDesc(year,month,date,troop,section,desc,function(){
		
			gt.convertToWeekday(year,month,date,function(json){
				res.send(json);
			});
		});
	});	
});


//스켸줄 제목 출력 포트 리스닝



	
app.get('/RST',function(req,res)
{
	var year = req.query.year.toString();
	var week = req.query.week.toString();
	var troop = req.query.troop.toString();
	
	console.log(year+week+troop);
	
	ms.readScheduleTitle(year,week,troop,function(jsonObj){
		if(jsonObj === undefined)
		{
			console.log('send null');
			return res.send(null);
		}
		else
		{
			console.log('send '+jsonObj);
			return res.json(jsonObj);
		}
	});

});


//스켸줄 상세 출력 포트 리스닝



app.get('/RSD',function(req,res)
{
	var year = req.query.year.toString();
	var week = req.query.week.toString();
	var troop = req.query.troop.toString();
	
	ms.readScheduleDesc(year,week,troop,function(jsonObj){
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



app.post('/SAu',function(req,res)
{
	
	var user = req.body.user;
	var target = req.body.target;
	var sec = req.body.sec;
	
	
	
	sa.setAuth(user,target,sec,function(valid){
				return res.send(valid);
			
		
	});
});

//인사권 지정 리스닝


app.post('/SAd',function(req,res)
{
	
	var user = req.body.user;
	var target = req.body.target;
	var value = req.body.value;
	
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

app.post('/SSL',function(req,res)
{
	console.log('SSL running...');
	var user = req.body.user;
	var target = req.body.target;
	var secret = req.body.secret;
	sa.setSecretHandle(user,target,secret,function(valid)
		{
			console.log('SSL finish');
			res.send(valid);
		});
});

app.listen(5032,function(){
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
