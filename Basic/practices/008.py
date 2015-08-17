m_string = 'Hello'

char_count = dict()
for char in m_string:
    count = char_count.get(char,0)
    count += 1
    char_count[char] = count
    
print char_count
