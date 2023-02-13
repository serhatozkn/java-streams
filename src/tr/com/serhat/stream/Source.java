package tr.com.serhat.stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Source {

    public static void main(final String[] args) {
        final Source streamSource = new Source();
        streamSource.demoInitialization();
        streamSource.demoPrimitiveStreams();
        streamSource.demoFileAsStream();
    }

    public void demoInitialization() {

        // From collection
        // Important note: .stream() doesn't manipulate the source collection or array etc.
        final Stream<Integer> integerStream = List.of(1, 2, 3, 4, 5).stream();
        System.out.println("Integer stream: ");
        integerStream.forEach(System.out::println);

        // From Array
        final Stream<String> stringStream = Arrays.stream(new String[]{"abc", "abcd", "abcde", "abcdef"});
        System.out.println("String stream: ");
        stringStream.forEach(System.out::println);

        // Direct initialization
        final Stream<Double> doubleStream = Stream.of(3d, 4d, 5d, 6d, 7d);
        System.out.println("Double stream: ");
        doubleStream.forEach(System.out::println);

        // Concat two streams
        System.out.println("Concat stream: ");
        // The statement below will throw IllegalStateException; since the stream has already been operated
        //final Stream<Number> concat = Stream.concat(integerStream, doubleStream);
        final Stream<? extends Number> numbersStream = Stream.concat(
                Stream.of(1, 2, 3, 4),
                Stream.of(1d, 2d, 3d, 4d)
        );
        numbersStream.forEach(System.out::println);

        System.out.println("Stream Builder:");
        final Stream<Long> longStream = Stream.<Long>builder()
                .add(1L)
                .add(2L)
                .build();
        longStream.forEach(System.out::println);

        System.out.println("Stream generate:");
        // Memory will ran out if no limit is defined (be careful about this)
        Stream.generate(() -> ThreadLocalRandom.current().nextInt(1000)).limit(20).forEach(System.out::println);

        System.out.println("Stream iterate:");
        Stream.iterate(10, n -> n + 3).limit(5).forEach(System.out::println);

        System.out.println("Stream iterate with predicate:");
        Stream.iterate(10, i -> i <= 25, i -> i + 3).forEach(System.out::println);
    }

    private void demoPrimitiveStreams() {
        // Primitive streams are good for preventing unnecessary boxing to wrapper types
        // Since streams uses java-generics it is impossible to use it with primitive
        // 3 alternatives IntStream, DoubleStream, LongStream
        // Good for efficiency

        // Sample, could be written as (n * (n + 1)) / 2
        System.out.println("1 + 2 + 3 + .... + 15 = " + IntStream.rangeClosed(1, 15).sum());

        // Can be converted to boxed type with calling boxed to stream
        IntStream.rangeClosed(2, 5).boxed();
    }


    private void demoFileAsStream() {
        try {
            Files.lines(Path.of("./TestFile.txt")).forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
