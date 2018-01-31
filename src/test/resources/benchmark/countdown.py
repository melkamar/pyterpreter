benchmark_name = "Benchmark countdown"


def run_benchmark():
    def do_stuff():
        x = 100000
        y = 0
        while x > 0:
            y = (y + 1) * 2
            x = x - 1
        return y

    def warmup():
        i = 0
        while i < 50:
            do_stuff()
            i = i + 1

    start_time = time()
    do_stuff()
    end_time = time()
    print(benchmark_name + " | no warmup | " + (end_time - start_time))

    warmup()
    start_time = time()
    do_stuff()
    end_time = time()
    print(benchmark_name + " | warmup    | " + (end_time - start_time))

run_benchmark()
