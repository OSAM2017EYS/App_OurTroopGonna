var express = require('express'),
    mysql = require('mysql'),
    formidable = require('formidable'),
    fs = require('fs');

var app = express();
var connection = mysql.createConnection({
    host: 'localhost',
    query: {
        pool: true
    },
    user: 'root',
    password: 'hello1004',
    database: 'nextagram'
});

app.get('/', function(request, response) {
    response.send('hello world');
    response.end();
    console.log('test');
});

app.get('/loadData/', function(req, res) {

    var sql = 'select * from next_android_nextagram where ArticleNumber>' + req.query.ArticleNumber;
    connection.query(sql, function(err, rows, fields) {
        if (err) {
            res.sendStatus(400);
            return;
        }
        if (rows.length == 0) {
            res.sendStatus(204);
        } else {
            res.status(201).send(rows);
            res.end();
        }
    });

}); //loadData


var savePath = 'D://nodetutorial//express_test//upload//';

var isFormData = function(req) {
    var type = req.headers['content-type'] || '';
    return 0 == type.indexOf('multipart/form-data');
}

app.post('/upload', function(req, res) {
    var form = new formidable.IncomingForm();
    var body = {};

    if (!isFormData(req)) {
        res.status(400).end('Bad Request : expecting multipart/form-data');
        return;
    }

    form.on('field', function(name, value) {
        body[name] = value;
    });

    form.on('fileBegin', function(name, file) {
        file.path = savePath + file.name;
        console.log(file.path);
    });

    form.on('end', function(fields, files) {
        var sql = 'insert into next_android_nextagram' +
            '(Title, Writer, Id, Content, WriteDate, ImgName)' +
            'values(?,?,?,?,?,?)';
        var args = [body.title, body.WriterName, body.WriterID,
            body.Content, body.WriteDate, body.ImgName
        ];

        connection.query(sql, args, function(err, results, fields) {
            if (err) {
                res.sendStatus(500);
                console.log('error');
                return;
            }
            res.sendStatus(200);
        });
    });
    form.parse(req);
});

app.get('/image/:filename', function(req, res) {
    var path = savePath + req.params.filename;
    fs.exists(path, function(exists) {
        if (exists) {
            var stream = fs.createReadStream(savePath + req.params.filename);
            stream.pipe(res);
            stream.on('close', function() {
                res.end();
            });
        } else {
            res.sendStatus(204);
        }
    });
});

app.listen(5009);