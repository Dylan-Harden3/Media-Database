# titles intput format (no header): (title ID) (title type) (original title) (runtime minutes) (genres) (year) (average rating) (num votes)
# customer_ratings input format (no header): (customer ID) (rating) (date) (title Id)
# principals input format (no header): (title ID) (nconst) (category)
# removes titles from principalsNew and customer_ratingsNew that have been removed from titles
# keeps same format of files input

# reading through titlesNewNew (most recent version of titles) and inserting all titles into map for ease of access
titles = {}

# getting file names for input and output
print("NOTE: put path of file from data directory")
titlesInput = input("input name for titles input file: ")
customerInput = input("input name for customer_ratings input file: ")
customerOutput = input("input name for customer_ratings output file: ")
principalInput = input("input name for principals input file: ")
principalOutput = input("input name for principals output file: ")

# loading titles and years into dictionary
with open("../" + titlesInput, "r") as fileRead:
    for line in fileRead.readlines():
        line = line.split("\t")  # spliting line by tabs
        # title id is first entry in titles file
        titles[line[0]] = line[5]  # just need title id, year (cannot have review in year before movie was released)

# reading through customerRatings, removing lines where title is not present and 
# year of review is less than year movie was made
with open("../" + customerInput, "r") as fileRead:
    with open("../" + customerOutput, "w") as fileWrite:
        for line in fileRead.readlines():
            # if title and year are good >> write to newnew
            line = line.split()  # splitting line by whitespace
            if (line[3] in titles):
                # check year of review (movie year <= review year)
                if (int(titles[line[3]]) <= int(line[2][0:4])):
                    # need to add
                    fileWrite.write("\t".join(line) + "\n")

# reading through principals, removing lines where title id is not in titlesNewNew
with open("../" + principalInput, "r") as fileRead:
    with open("../" + principalOutput, "w") as fileWrite:
        for line in fileRead.readlines():
            # if title is in titles >> add to new file
            line = line.split()  # splitting by whitespace
            if (line[0] in titles):
                # have title >> can write to new file
                fileWrite.write("\t".join(line) + "\n")