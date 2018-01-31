benchmark_name = "Benchmark fibonacci"


def run_benchmark():
    def fibonacci(number_idx, should_print):
        first = 1
        second = 1
        i = 0

        while i < number_idx:
            i = i + 1
            new = first + second
            if should_print:
                print(i + ". fib number: " + new)
            first = second
            second = new

        return second

    def warmup():
        i = 0
        while i < 50:
            fibonacci(10000, False)
            i = i + 1

    start_time = time()
    fibonacci(10000, False)
    end_time = time()
    no_warmup_time = end_time - start_time

    warmup()
    start_time = time()
    fibonacci(10000, False)
    end_time = time()

    print(benchmark_name + " | no warmup | " + str(no_warmup_time))
    print(benchmark_name + " | warmup    | " + str(end_time - start_time))


run_benchmark()
