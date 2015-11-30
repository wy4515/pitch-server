# pitch-server

### API
- register

`GET /register?param...` [e.g., `/pitch/register?username=Bob&password=123`]

return 200 if exist otherwise 404


`POST /register` add user
*username* and *password* fields are required

- pitch

`GET /pitch/pitch/`

will return all pitch create by this user. (need **session** data)

`POST /pitch/pitch` create a pitch with date, title, description, video url

- like

`GET /pitch/like?pid=2` get the like count for pitch pid=2

`POST /pitch/like` with request in json format 
```json
{"pid": 2}
```
will increase like count for the pitch by one

- comment

`GET /pitch/comment?pid=2` get comments for pitch pid=2

`POST /pitch/comment` with request in json format
```json
{
  "pid":2,
  "email":"hi@hi.com",
  "comment":"test comment"
}
```
email serve as reference to **pitch_user**
