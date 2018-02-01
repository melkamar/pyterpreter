benchmark_name = "Benchmark factorial_iterative"
fact_num = 500


def run_benchmark():
    def factorial_iterative(num):
        result = 1
        while num > 0:
            result = result * num
            num = num - 1

        return result

    def warmup():
        i = 0
        while i < 50:
            factorial_iterative(fact_num)
            i = i + 1

    start_time = time()
    factorial_iterative(fact_num)
    end_time = time()
    no_warmup_time = end_time - start_time

    warmup()
    start_time = time()
    res = factorial_iterative(fact_num)
    end_time = time()

    print(benchmark_name + " | no warmup | " + str(no_warmup_time))
    print(benchmark_name + " | warmup    | " + str(end_time - start_time))
    print(fact_num + "! = "+res)


run_benchmark()
