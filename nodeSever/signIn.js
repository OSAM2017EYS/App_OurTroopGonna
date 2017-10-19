var fs = require('fs');

//async 완료



function signIn(username,password,callback){
	
	fs.readFile('./account.json',function(err,data){
		var parsedData = JSON.parse(data);
	
		for(var uName in parsedData)
		{

			var found =1; // 최초 발견시 바로 빠져나올 수 있게하자.

			if(found)
			{
				if(uName === username)
				{
					found=0;
					if(parsedData[uName][0]===password)
						{
							console.log('welcome '+username);
							if(username !== 'admin')
								{
									var currentData = parsedData[username];



									return callback(true,currentData);
									//로그인 한 뒤의 동작들을 넣는다.
								}
							
						}
					else
						{
							//로그인 실패시의 동작들을 넣는다.
							console.log('no '+username);
							return callback(false,null);
						}
					}
				}
			else break;
		}
		
	});
	
}


exports.signIn = signIn;
