# data info

## scripts

### nameSpliter.py:
<p>
    takes in names file formated as 
        (line number) (nconst) (primary name) (birth year) (death year) (primaryProfession) <br>
    outputs file formated as (removes header as well) with only people who are actors, actresses, or directors
        (nconst) (primary name)
</p>

### principalSpliter.py: 
<p>
    takes in file formated as 
        (line number) (title ID) (nconst) (category) (job) (characters) <br>
    outputs file (without header) with only actresses, actors, and directors formated as
        (title ID) (nconst) (category)
</p>

### titleSpliter.py: 
<p>
    takes in file formated as 
        (line number) (title ID) (original title) (start year) (end year) (runtime minutes) (genres) (year) (average rating) (num votes) <br>
    outputs file with titles in format
        (title ID) (title type) (original title) (runtime minutes) (genres) (year) (average rating) (num votes) <br>
        titles missing any of the info that was to be output were excluded from output (not included in new file)
</p>

### removeTitles.py:
<p>
    titles intput format (no header): (title ID) (title type) (original title) (runtime minutes) (genres) (year) (average rating) (num votes) <br>
    customer_ratings input format (no header): (customer ID) (rating) (date) (title Id) <br>
    principals input format (no header): (title ID) (nconst) (category) <br>
    removes titles from principalsNew and customer_ratingsNew that have been removed from titles <br>
    keeps same format of files input
</p>

### tabToComma.py:
<p>
    takes names file formatted as <nconst> <name> <br>
    outputs names file as <nconst>,<name>
</p>

### personalIDReducer.py:
<p>
    takes in names file formatted as <nconst>,<name> <br>
    takes in principals file formatted as <title id> <nconst> <category>
    outputs new names and principals file with only the people that occur in both files
</p>

## data files

### names.csv:
<p>
    original names file given as starter data

### namesIntermediate.csv:
<p> 
    names file without header with only actresses, actors, and directors formatted as nconst and primary name info 
</p>

### namesClean.csv:
<p> 
    names file without header with only actresses, actors, and directors formatted as nconst and primary name info + all people who are not in principals (do not have a role in any of the movies) have been removed
</p>

### principals.csv:
<p>
    original principals file given as starter data
</p>

### principalsIntermediate1.csv:
<p>
    principals file without header with only actresses, actors and directors formatted as titleID, nconst, and category info
</p>

### principalsIntermediate2.csv:
<p>
    principals file with only title ID, nconst, and category + all people who were not actors, actresses, or directors have been removed + all people who were apart of a movie that is not in titlesClean.csv have been removed
</p>

### principalsClean.csv:
<p>
    principals file with only title ID, nconst, and category + all people who were not actors, actresses, or directors have been removed + all people who were apart of a movie that is not in titlesClean.csv have been removed + all the people who are not in the names file have been removed
</p>

### titles.csv:
<p>
    original titles file given as starter data
</p>

### titlesIntermediate.csv:
<p>
    titles file with title ID, title type, orignial title, runtime minutes, genres, year, average rating and num votes without titles that are missing any of the stated output info
</p>

### customer_ratings.csv: 
<p>
    original customer ratings file given as starter code
</p>

### customer_ratingsIntermediate.csv: 
<p> 
    customer_ratings.csv without line number and file header
</p>

### customer_ratingsClean.csv:
<p>
   customer_ratings.csv without line number and file header + all people who were apart of a movie that is not in titlesClean.csv have been removed
</p>

### titlesClean.csv:
<p>
    titles file with title ID, title type, orignial title, runtime minutes, genres, year, average rating and num votes without titles that are missing any of the stated output info + any repeat titles have been removed with only the most recent kept
</p>