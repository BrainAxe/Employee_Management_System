l=raw_input("")
i=l.split(" ")
n=int(i[0])
m=int(i[1])
a=int(i[2])
 
if n%a == 0:
    c = n/a
   
else:
    c = int(n/a) + 1
   
if m%a == 0:
    d = m/a
   
else:
    d = int(m/a) + 1
   
print c*d
