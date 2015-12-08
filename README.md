# pitch-server

### API
- register

`GET /register?param...` [e.g., `/pitch/register?username=Bob&password=123`]

return 200 if exist otherwise 404


`POST /register` add user
*username* and *password* fields are required

- pitch

`GET /pitch/pitch?pid=12`

```json
{
  "pitch":{
    "date":"2015-12-08",
    "comments":"[{\"user\":\"hi@hi.com\",\"comment\":\"test comment\"},{\"user\":\"hi@hi.com\",\"comment\":\"another test comment\"}]",
    "tittle":"tesgt",
    "likes":0
  }
}
```

`POST /pitch/pitch` create a pitch with email, title, description, tag video bytes from a **multipart form**

```html
<HTML>
<p>Example</p>
<FORM ACTION="http://127.0.0.1:8080/pitch/pitch" METHOD=POST ENCTYPE="multipart/form-data">
  title? <INPUT TYPE=TEXT NAME=title> <BR>
  description? <INPUT TYPE=TEXT NAME=submitter> <BR>
  date? <INPUT TYPE=TEXT NAME=date> <BR>
  email? <INPUT TYPE=TEXT NAME=email> <BR>
  tags? <INPUT TYPE=TEXT NAME=tags> <BR>
  Which file to upload? <INPUT TYPE=FILE NAME=file1> <BR>
  <INPUT TYPE=SUBMIT>
</FORM>
</HTML>
```
create a *like_count*=0

- like

`GET /pitch/like?pid=2` get the like count for pitch pid=2

```json
{"count":0,"pid":12,"Success":true}
```

`POST /pitch/like` with request in json format 
```json
{"pid": 2}
```
will increase like count for the pitch by one

`curl -X POST -H "Content-Type: application/json" -d '{"pid":"12"}' http://localhost:8080/pitch/like`

- comment

`POST /pitch/comment` with request in json format: `curl -X POST -H "Content-Type: application/json" -d '{"pid":"12","email":"hi@hi.com","comment":"test comment"}' http://localhost:8080/pitch/comment`
```json
{
  "pid":2,
  "email":"hi@hi.com",
  "comment":"test comment"
}
```
email serve as reference to **pitch_user**

- List

**GET** a list of meta data: `http://127.0.0.1:8080/pitch/list?email=hi@hi.com`

```json
{
  "meta":"[{\"pid\":12,\"title\":\"tesgt\",\"date\":\"2015-12-08 11:24:43.643\",\"email\":\"hi@hi.com\",\"like\":0}]"
}
```

- Video

**GET** video bytes in utf-8 format by pid: `http://127.0.0.1:8080/pitch/video?pid=12`
```json
{
  "Data":"?PNG\r\n\u001a\n\u0000\u0000...",
  "Success":true
}
```

