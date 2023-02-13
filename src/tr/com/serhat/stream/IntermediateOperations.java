package tr.com.serhat.stream;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IntermediateOperations {

    public static void main(final String[] args) {
        final IntermediateOperations intermediateOperations = new IntermediateOperations();
        intermediateOperations.demo();
    }

    final StringStreamSupplier stringStreamSupplier = new StringStreamSupplier();

    final ListOfListSupplier listOfListSupplier = new ListOfListSupplier();

    public void demo() {
        Stream<String> stream = stringStreamSupplier.get();

        // Filter takes Predicate as parameter
        final Optional<String> startsWithM = stream.filter(s -> s.startsWith("M")).findFirst();
        if (startsWithM.isPresent()) {
            System.out.println("Starts with M : " + startsWithM.get());
        }

        stream = stringStreamSupplier.get();
        // Map
        System.out.println("Upper-case names: ");
        stream.map(String::toUpperCase).forEach(System.out::println);

        // Flat map
        final List<List<Integer>> lists = listOfListSupplier.get();
        System.out.println("Before flatMap: " + lists);

        final List<Integer> integerList = lists.stream().flatMap(Collection::stream).toList();
        System.out.println("After flatmap: " + integerList);

        System.out.println("Sort natural order");
        // sorted
        IntStream.generate(() -> ThreadLocalRandom.current().nextInt(1000))
                .limit(10)
                .sorted()
                .forEach(System.out::println);

        System.out.println("Sort reverse order");
        stringStreamSupplier.get()
                .map(String::toUpperCase)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
    }

    private static class StringStreamSupplier implements Supplier<Stream<String>> {

        @Override
        public Stream<String> get() {
            return Stream.of("serhat", "serhat", "Mustafa", "ferhat", "ali haydar", "kübra", "anıl", "kubilay");
        }
    }

    private static class ListOfListSupplier implements Supplier<List<List<Integer>>> {

        @Override
        public List<List<Integer>> get() {
            final List<List<Integer>> listOfList = new ArrayList<>();
            listOfList.add(List.of(1, 2, 3, 4, 5));
            listOfList.add(List.of(6, 7, 8, 9, 10));
            listOfList.add(List.of(11, 12, 13, 14, 15));
            return listOfList;
        }
    }
}
