def fact(x):
    if x:
        return fact(x-1) * x
    else:
        return 1

print(fact(5))
