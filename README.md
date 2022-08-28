# Media-Database

*Due to this project relying on a supplied database from the course, it will no longer work. Some imformation about the project can be found below*

## Data, DatacleaningScripts
The Data folder contains the cleaned versions of the data files that this project is based on. The original files were not uniformly formatted and had a lot of missing or useless data.
This is what the DatacleaningScripts are for, to format the data into a uniform way such that it can be parsed and inserted into DB. You can read more on the details of this in the readme inside th datacleaningScripts folder.

## populationScripts
This contains the scripts for populating the 5 tables (peoplenames, peoplemedia, customerratings, mediaratings, mediainfo).
The format of each table can be found below:
### peoplenames (personid TEXT PRIMARY KEY, name TEXT)
### peoplemedia (personid TEXT, job TEXT, titleid TEXT)
### customerratings (customerid INT, rating INT, date date, titleid TEXT)
### mediaratings (titleid TEXT PRIMARY KEY, year INT, avgrating FLOAT, numvotes INT, genre TEXT)
### mediainfo (titleid TEXT PRIMARY KEY, type TEXT, titlename TEXT, genre TEXT, runtime INT)

## src
This folder contains the code for our elementry swing GUI, as well as the algorithms for generating the results.
<br>
The user can login as either an **Analyst** or a **customer**: <br>
*The login logic can be seen in **Login.java***

### Analyst View

This view shows some imformation about the whole dataset and list of media as a whole. <br>

#### Cult Classics
Shows the top 10 titles that have the most 4 or 5/5 ratings.

#### Hollywood Pairs
Shows the actors/actresses that have worked together and had the most success (measured by avg rating of the title)

#### Shortest Path
Returns the *Shortest Path* between two movies, where the path is defined as the customers who have watched the movies.
Runs a DFS on a map of all the customers, and all the movies they have watched.

### Customer View
Here a user can view their watch history for any of (2000-2001,2002-2003,2004-2005, or all time), see a recommendation from their most viewed director, and receive 10 recommendations based on their top genres watched.
The logic for these commands can be seen in **customerGUI.java & DirectorsChoice.java**
