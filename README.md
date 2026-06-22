# Big Data Engineering with Hadoop MapReduce

> Distributed batch processing on Hadoop MapReduce — a clean Mapper/Combiner/Reducer/Driver job that scales across a cluster and runs on AWS (EMR/EC2).

![Hadoop](https://img.shields.io/badge/Hadoop-66CCFF?style=flat&logo=apachehadoop&logoColor=black)
![Java](https://img.shields.io/badge/Java-007396?style=flat&logo=openjdk&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-232F3E?style=flat&logo=amazonaws&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green)

## Overview

A Hadoop **MapReduce** job (Java) that processes large text/log datasets in parallel across a cluster, reading from and writing to **HDFS**. It demonstrates the core data-engineering pattern — partition work, move computation to the data, pre-aggregate locally with a **Combiner** to cut shuffle cost, then reduce. Deployable on **AWS EMR** for multi-gigabyte workloads.

The example computes word frequencies (the canonical MapReduce workload); the same Mapper/Reducer structure generalizes to log analysis, event counting, and aggregation jobs.

## Key Features

- **Mapper / Combiner / Reducer / Driver** — idiomatic, well-separated components
- **Combiner = Reducer** for local pre-aggregation (associative + commutative sum) → less shuffle/network I/O
- **Token normalization** (lowercase + strip non-alphanumerics) for clean counts
- **HDFS in/out** via `FileInputFormat` / `FileOutputFormat`
- **AWS EMR-ready** for distributed, fault-tolerant runs on large data

## Tech Stack

Java · Apache Hadoop (MapReduce, HDFS) · Maven · AWS EMR/EC2

## How It Works

```
Input (HDFS) ──► Mapper: (word, 1)
                     │
                     ▼  Combiner: local partial sums
                Shuffle & Sort (by word)
                     │
                     ▼
                Reducer: (word, total) ──► Output (HDFS)
```

## Project Structure

```
.
├── src/main/java/com/kovi/wordcount/
│   ├── TokenizerMapper.java    # emits (word, 1)
│   ├── SumReducer.java         # sums counts (also the Combiner)
│   └── WordCountDriver.java    # job configuration + submission
├── data/
│   └── sample_input.txt        # tiny sample to test locally
├── pom.xml
└── README.md
```

## Build & Run

### Build the jar
```bash
mvn clean package          # produces target/wordcount.jar
```

### Run on Hadoop (local or cluster)
```bash
# Load input into HDFS
hdfs dfs -mkdir -p /input
hdfs dfs -put data/sample_input.txt /input

# Submit the job
hadoop jar target/wordcount.jar com.kovi.wordcount.WordCountDriver /input /output

# View results
hdfs dfs -cat /output/part-r-00000
```

### Run on AWS EMR
Upload the jar + data to S3, create an EMR cluster, and add a **Step** with the jar
and arguments `s3://your-bucket/input s3://your-bucket/output`.

## Expected Output (sample)

```
and       2
big       2
data      2
dog       2
fox       2
hadoop    2
the       4
...
```

## License

MIT
