def ishashad(num):
    a = 0
    b = num
    while num!=0:
        a = a + num%10
        num = num/10
    if b % a == 0:
        return True
    else:
        return False
        
         
print ishashad(12)

