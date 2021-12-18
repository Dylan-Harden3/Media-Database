# takes in file formated as 
    # (line number) (title ID) (original title) (start year) (end year) (runtime minutes) (genres) (year) (average rating) (num votes)
# outputs file with titles in format
    # (title ID) (title type) (original title) (runtime minutes) (genres) (year) (average rating) (num votes)
    # titles missing any of the info that was to be output were excluded from output (not included in new file)

# getting files for input and output
print("NOTE: put path of file from data directory")
fileInput = input("input file to read from: ")
fileOutput = input("input file to write to: ")

# deleting index 0, 4, 5 from titles
with open('../' + fileInput, 'r') as fileRead:
    with open('../' + fileOutput, 'w') as fileWrite:
        badLines = 0  # number of lines missing data that we need
        # fileWrite.write(fileRead.readline())  # header
        for lineIdx, lineRead in enumerate(fileRead.readlines()):
            lineRead = lineRead.split("\t")
            count = 0  # number of items written per row
            itemsMiss = 0  # number of items on this row that are missing from data set
            writeStr = ""  # string to hold data to write to file
            for idx, item in enumerate(lineRead):
                if (idx != 0 and idx != 4 and idx != 5):
                    writeStr += (item + "\t")
                    count += 1
                    # if item was missing, count it
                    if (item == ""):
                        itemsMiss += 1
            
            # writing line to file if good
            if (not(itemsMiss)):
                fileWrite.write(writeStr)

            # checking number of items in row against expected amount
            if (count != 8): 
                print("line:", lineIdx, "-", count)

            # found bad line
            if (itemsMiss):
                badLines += 1
        print("lines deleted from missing data:", badLines)