var fs = require('fs');
var tco = require('./troopcordOperator.js');

function setAuth(solNumber,targetUser,section,callback){
	//같은 부대에 있는 인사담당 solNumber이 targetUser의 부서에 대한 권리를 입력
	checkHorRelation(solNumber,targetUser,function(data){
	
		if(data)
			{
				tco.getInfoByName(targetUser,function(data){
					
					var jsonObj = JSON.parse(data);
					jsonObj['section']=section;
					var jsonString = JSON.stringify(jsonObj);
					tco.setInfoByName(targetUser,jsonString,function()
									  {
						
						return callback(true);
									
									  });
				});
			}
		else
			{
				return callback(false);
			}
	});
	
}

function setAdmin(solNumber,targetUser,value,callback)
{
	//상급 부대에 있는 인사담당 solNumber이 targetUser의 하급부대에 대한 인사담당 권한을 입력
	//value === 'Y' or 'N'
	checkUDRelation(solNumber,targetUser,function(data){
	
		if(data)
			{
				tco.getInfoByName(targetUser,function(data){
					
					var jsonObj = JSON.parse(data);
					console.log(value);
					jsonObj['admin']=value;
					var jsonString = JSON.stringify(jsonObj);
					tco.setInfoByName(targetUser,jsonString,function()
									  {
						
						return callback(true);
									
									  });
				});
			}
		else
			{
				return callback(false);
			}
	});
}

function checkHorRelation(solNumber,targetUser,callback)
{
	//같은 부대 소속인지 검사한다.
	tco.getInfoByName(solNumber,function(data){
		if(JSON.parse(data)['admin'] === 'Y')
		{
			console.log("UD rel");
			tco.getCode(solNumber,function(data){
				
				var userCode = data;

				tco.getCode(targetUser,function(data){
					
					var targetCode = data;
					
					
					return callback(userCode === targetCode);
					
				});
			
			});
		}
		else
			{
				console.log('err');
				return callback(false);
			}
	});
}


function checkUDRelation(solNumber,targetUser,callback)
{
	//solNumber이 targetUser의 상급부대 사람인지 검사한다.
	tco.getInfoByName(solNumber,function(data){
		if(JSON.parse(data)['admin'] === 'Y')
		{
			console.log('you are admin');
			tco.getCode(solNumber,function(data){
				
				var userCode = data;
				console.log(userCode);
				tco.getCode(targetUser,function(data){
					
					var targetCode = data;
					console.log(targetCode);
					tco.isInUDRelation(userCode,targetCode,function(judge){
						console.log(judge);
						return callback(judge);
					});
				});
			
			});
			

		}
		else
			{
			
				console.log('err');
				return callback(false);
			
			}
	
	});

	
}


function getMyInfo(user,callback)
{
	tco.getInfoByName(user,function(data){
		
		var jsonObj = JSON.parse(data);
		return callback(jsonObj);		
	});
		

}

function setSecretHandle(user,target,level,callback)
{
	
	checkUDRelation(user,target,function(judge){
		if(judge)
			{
				console.log("UD rel");
				tco.getInfoByName(target,function(data)
								  {
					jsonObj=JSON.parse(data);
					jsonObj['secretLevel']=level;

					var jsonString = JSON.stringify(jsonObj);
							tco.setInfoByName(targetUser,jsonString,function(value)
											  {

								return callback(value);

											  });
				});
			}
		else
			{
				checkHorRelation(user,target,function(judge)
								{
					console.log("Hor rel");
			
					if(judge)
					{
		
						tco.getInfoByName(target,function(data)
										  {
							jsonObj=JSON.parse(data);
							jsonObj['secretLevel']=level;

							var jsonString = JSON.stringify(jsonObj);
									tco.setInfoByName(target,jsonString,function(value)
													  {
										console.log(value);
										
										return callback(value);

													  });
						});
					}
					else 
					{
						console.log("No rel");
						return callback(false);
					}
				});
			}
	});
	
}


exports.setSecretHandle = setSecretHandle;
exports.getMyInfo=getMyInfo;
exports.setAdmin = setAdmin;
exports.setAuth = setAuth;
exports.checkUDRelation = checkUDRelation;
exports.checkHorRelation = checkHorRelation;