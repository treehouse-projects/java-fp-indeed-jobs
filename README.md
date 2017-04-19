# Indeed Jobs

This repository accompanies the [Treehouse](https://teamtreehouse.com) course [Introduction to Functional Programming](https://teamtreehouse.com/library/introduction-to-functional-programming/upcoming).

This codebase is used to explore Java 8 functional programming concepts.  It uses data from the job aggregator [Indeed](http://indeed.com).

Current data is cached and included in the repository.


### To refresh the job data
Sign up to be an [Indeed publisher](https://www.indeed.com/publisher).  Add your publisher key to the [config.properties](src/main/resources/config.properties) file,
and set `shouldRefresh` in [App.java](src/main/java/com/teamtreehouse/jobs/App.java#L13).

#### Using this code
You can jump to a certain point in the course by checking out a tag.  Tags are in the format of `sXvY` where,
`X` is the stage number and `Y` is the video number.  For instance to get your code set to the 4th video in stage 1,
you would perform the following git command:

`git checkout s1v4`

