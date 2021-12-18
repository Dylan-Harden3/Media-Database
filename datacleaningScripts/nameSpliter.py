# takes in names file formated as 
    # <line number> <nconst> <primary name> <birth year> <death year> <primaryProfession>
# outputs file formated as (removes header as well) with only people who are actors, actresses, or directors
    # <nconst> <primary name>

# getting files for input and output
print("NOTE: put path of file from data directory")
fileInput = input("input file to read from: ")
fileOutput = input("input file to write to: ")

# deleting index 0, 3, 4 from titles
with open('../' + fileInput, 'r') as fileRead:
    with open('../' + fileOutput, 'w') as fileWrite:
        # fileWrite.write(fileRead.readline())  # header
        for lineIdx, lineRead in enumerate(fileRead.readlines()):
            lineRead = lineRead.split("\t")
            count = 0  # number of items written per row
            
            # checking that primary profession is at least actor, actress, or director
            profession = lineRead[5].split(',')
            keep = False  # set to true if actress, actor, or director
            for item in profession:
                if (item == "actress" or item == "actor" or item == "director"):
                    # keep this line 
                    keep = True
                    break
            # skipping if not right profession
            if (not(keep)): 
                continue

            # removing birth year and death year, line number
            for idx, item in enumerate(lineRead):
                if (idx != 0 and idx != 3 and idx != 4 and idx != 5):
                    fileWrite.write(item + "\t")
                    count += 1
            
            # writing to file, testing for right length of data
            fileWrite.write("\n")
            if (count != 2): 
                print("line:", lineIdx, "-", count)