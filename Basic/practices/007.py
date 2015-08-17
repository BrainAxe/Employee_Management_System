people = [['Tanzim', 24], ['Tisha', 12] ]
ages = []
for person in people:
    ages.append(person[1])
total = sum(ages)
avg = total / len(people)

print avg
