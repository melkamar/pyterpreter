benchmark_name = "Benchmark factorial_recursive"
fact_num = 500


def run_benchmark():
    def factorial_recursive(num):
        if num == 1:
            return 1
        return num * factorial_recursive(num - 1)

    def warmup():
        i = 0
        while i < 50:
            factorial_recursive(fact_num)
            i = i + 1

    start_time = time()
    factorial_recursive(fact_num)
    end_time = time()
    no_warmup_time = end_time - start_time

    warmup()
    start_time = time()
    res = factorial_recursive(fact_num)
    end_time = time()

    print(benchmark_name + " | no warmup | " + str(no_warmup_time))
    print(benchmark_name + " | warmup    | " + str(end_time - start_time))
    print(fact_num + "! = "+res)


run_benchmark()
