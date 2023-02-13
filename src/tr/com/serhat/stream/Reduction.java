package tr.com.serhat.stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Reduction {

    public static void main(final String[] args) {

        final List<Student> students = new ArrayList<>() {{
            add(new Student(8));
            add(new Student(9));
            add(new Student(8));
            add(new Student(12));
            add(new Student(13));
            add(new Student(10));
            add(new Student(11));
        }};


        final double averageAgeOfStuds = students.stream()
                .map(Student::age)
                .mapToDouble(age -> age)
                .summaryStatistics()
                .getAverage();

        // Count / min / max / sum / average can be found at summary statics class
        System.out.println("Average age of students is " + averageAgeOfStuds);

        // Another way for finding the eldest student
        final Optional<Integer> max = students.stream().map(Student::age).max(Integer::compareTo);
        if (max.isPresent()) {
            System.out.println("Eldest students age is " + max.get());
        }

        final int sum = students.stream().map(Student::age).mapToInt(x -> x).sum();

        /**
         * Before we look deeper into using the Stream.reduce() operation, let's break down the operation's participant elements into separate blocks. That way, we'll understand more easily the role that each one plays.
         *
         * Identity – an element that is the initial value of the reduction operation and the default result if the stream is empty
         * Accumulator – a function that takes two parameters: a partial result of the reduction operation and the next element of the stream
         * Combiner – a function used to combine the partial result of the reduction operation when the reduction is parallelized or when there's a mismatch between the types of the accumulator arguments and the types of the accumulator implementation
         */

        final int sumWithReduce1 = students.stream().map(Student::age).reduce(0, (a, b) -> a + b);
        final int sumWithReduce2 = students.stream().map(Student::age).reduce(0, Integer::sum);

        System.out.println(".sum(): " + sum  + System.lineSeparator()
                + ".reduce(0, (a,b)->a+b): " + sumWithReduce1 + System.lineSeparator()
                + ".reduce(0, Integer::sum): " + sumWithReduce2);

        /**
         * Reduce with parallel streams
         */

        // Lets create a bigger list
        final List<Integer> values = IntStream.iterate(1, i -> i + 1).limit(2_000).boxed().toList();

        final Integer parallelResult = values.parallelStream().reduce(0, Integer::sum, Integer::sum);
        // Result must be (2000 * 2001) / 2
        System.out.println("Parallel stream result of 1 + 2 + 3 + ... + 2000 = " + parallelResult);
        if (parallelResult != (2000 * 2001) / 2) {
            throw new AssertionError("Wrong usage!!!");
        }

        // Find the longest string with reduce
        try {
            Optional<String> longestWord = Files.lines(Path.of("./TestFile.txt"))
                    .reduce((word1, word2) -> word1.length() > word2.length() ? word1 : word2);

            if (longestWord.isPresent()) {
                System.out.println("Longest word is " + longestWord.get());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private record Student(int age) {}
}
