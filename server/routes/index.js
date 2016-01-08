var express = require('express');
var router = express.Router();
var pgp = require('pg-promise')();
var fs = require('fs');
var multer = require('multer')
var body = require('body-parser');
var cn = 'postgres://localhost:5432/linkit';
var irc = require("irc");
var upload = multer( {dest: './q/' } )

var db = pgp(cn);

var client = new irc.Client('open.ircnet.net', 'vihreabotti', {
  channels: ['#ohrapuuro'],
  debug: true
});

//client.say('#ohrapuuro', "TestI"});

//servaa kuvat
router.use('/q',express.static('q'));

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });

});


//tee kuvan osoitteesta parametri requestiin
router.param('id', function(req, res, next,id){
  req.id = id;

  next();
})

router.get('/r/:id', function(req,res){
  res.render('q', {kuva: {id : 'q/' +req.id}});
})



// postaa kuva, upload poimii kuvan suoraan /q/ kansioon.

router.post('/post', upload.single('image') ,function (req, res){
//  console.log(req.file);
  osoite = req.file.filename;
  //poimi data
  var data = {
    nimi : req.file.originalname,
    kuvateksti : req.body.kuvateksti,
    kanava : req.body.kanava,
    paiva : new Date()
  };

//tallenna kuvan tiedot tietokantaan.

  db.none("INSERT INTO osoitteet (nimi,kuvateksti,kanava,paiva) values($1,$2,$3,$4)",
  [data.nimi, data.kuvateksti, data.kanava, data.paiva])
  .then(function(){
    client.say('#ohrapuuro', "http://localhost:3000/r/" + osoite);
    res.send('ok')
  })
  .catch(function(error){
    console.log(error);
  })
});

module.exports = router;
