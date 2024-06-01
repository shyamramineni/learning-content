# Views

Jenkins Views allow you to organize your jobs based on various criteria including naming conventions or regular expression filters. This allows you to maintain your Jenkins jobs organized and easily accessible. 

![](/imgs/views.png)

- Name your new view as `freestyle-jobs`
- select _List view_
- click on _Create_
- select checkbox _Use a regular expression to include jobs into the view_
- enter `(freestyle).*`
- click Ok
- Verify your `freestyle-jobs` view list of all of your jobs that start with the `freestyle` name