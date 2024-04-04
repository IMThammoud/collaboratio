# Collaboratio

### WebApp with different approach to teamwork

#### Done:

##### -> Prepared some HTML Templates to be served with MVC
##### -> implemented Login logic
##### -> User Registration Works now
##### -> Session Creation works now
##### ---> Need to implement Environment variables, but DB-Connection is denied when using the ENVs. Maybe they are not recognized?
##### --> Used GSON-Library to Send JSON data to frontend
##### --> Used Plain JS to create Card-Elements based on how many Sessions an User has
##### --> Filled the Cards with the Data from the JSON-Objects
##### --> SessionHost and SessionID will be returned inside Session-JSON-Object after SessionCreation
____
## To-Do:
##### -> Use Environment Variables for DB Login
##### -> Fill Card Images with imageuploads from Sessioncreation or a Default one
##### -> Store IMGs on Server and safe the URL-Paths to the imgs in the DB instead of the img Blob
##### -> Dont allow Session Topic and Problem to be empty on creation
##### -> Write a Query to get the current host of session inside an opened Session and render it
##### -> Write a Query to Delete/Update a Session and render the option in "Sessions"
##### -> Add a Profile View and render User Info
##### -> Write a Query to get info about the opened Session and render info on view
##### -> T_sessions_created Table needs a Primary key to render info about a session precisely
