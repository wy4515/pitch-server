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
